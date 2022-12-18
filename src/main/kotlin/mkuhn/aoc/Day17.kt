package mkuhn.aoc

import mkuhn.aoc.util.Point

fun day17part1(input: List<String>): Long  = runRockSimulation(input, 2022)
fun day17part2(input: List<String>): Long  = runRockSimulation(input, 1000000000000L)

fun runRockSimulation(input: List<String>, rockCount: Long): Long {

    val seen = mutableMapOf<CaveState, Pair<Int, Int>>()
    var cave = CaveState(emptySet(), 0, 0)
    (0 until rockCount).forEach { ri ->
        cave = cave.applyNextBlock(input)

        val height = cave.height()
        val foundCave = seen[cave.toZeroBottom()]
        if(foundCave != null) {
            val cycleLength = ri - foundCave.second
            val remainingCycles = ((rockCount - ri)/cycleLength)
            val heightPerCycle = height - foundCave.first
            val fullCyclicalHeight = (remainingCycles)*heightPerCycle-1
            val remainder = ((rockCount - foundCave.second)%cycleLength)-1
            println("$ri | seen ${cave.rockIndex}, ${cave.jetIteration} | ${foundCave} | pattern: currH=$height, frq=$cycleLength, cyc=$remainingCycles, h/c=$heightPerCycle, fh=$fullCyclicalHeight, rem=$remainder")

            val heightAtEndOfCycle = (height + fullCyclicalHeight)

            var remHeight = 0
            if(remainder > 0) {
                var remCave = cave
                (0 until remainder).forEach { remCave = remCave.applyNextBlock(input) }
                remHeight = remCave.height()+1 - height+1
            }
            println("remH=${remHeight}, hend=$heightAtEndOfCycle")

            return remHeight+heightAtEndOfCycle
        }

        seen[cave.toZeroBottom()] = height to ri.toInt()
    }

    return cave.height().toLong()

}

fun CaveState.height() = -(rockMass.minOf { it.y })

fun CaveState.toZeroBottom() = CaveState(rockMass.toZeroBottom(), rockIndex, jetIteration)

fun CaveState.applyNextBlock(input: List<String>): CaveState {
    val gasJets = input.first().map { if(it == '<') -1 else 1 } //todo: pull out of here
    val xBounds = (0 .. 6)
    var newJetIteration = jetIteration
    var isResting = false
    val height = (rockMass.minOfOrNull { it.y }?:1)-1
    var workingRock = getRockAt(rockIndex).moveByVector(Point(2, -3+height)) //new rock at correct origin

    while(!isResting) {
        val jettedRock = workingRock.moveByVector(Point(gasJets[newJetIteration], 0)) //move by gas jet
        if(jettedRock.none { it.x !in xBounds } && jettedRock notIntersects rockMass) { workingRock = jettedRock }
        newJetIteration = (newJetIteration+1)%gasJets.size

        val fallingRock = workingRock.moveByVector(Point(0, 1)) //drop
        if(fallingRock.none { it.y > 0 } && fallingRock notIntersects rockMass) { workingRock = fallingRock }
        else { isResting = true }
    }

    val newRockPile = (rockMass + workingRock).trimToAccessibleFloor().toMutableSet()
    var newRockIndex = ((rockIndex+1)%RockType.values().size)

    return CaveState(newRockPile, newRockIndex, newJetIteration)
}

data class CaveState(val rockMass: Set<Point>, val rockIndex: Int, val jetIteration: Int)

infix fun Set<Point>.notIntersects(otherSet: Set<Point>) = none { it in otherSet }

fun Set<Point>.trimToAccessibleFloor(): Set<Point> {
    val floor = groupBy { it.x }.map { l -> l.value.minOfOrNull { it.y }?:0 }.maxOrNull()?:0
    return filter { it.y <= floor+4 }.toSet() //offset by 4 because there could be a hanging shelf
}

fun Set<Point>.toZeroBottom(): Set<Point> = moveByVector(Point(0, -(maxOfOrNull { it.y }?:0)))

fun Set<Point>.print(height: Int) {
    (height .. 0).map { y ->
        (0..6).map { x ->
            if(Point(x, y) in this) "#"
            else '.'
        }.joinToString("")
    }.joinToString("\n").apply { println(this); println() }
}

fun Set<Point>.moveByVector(v: Point): Set<Point> = map { Point(it.x + v.x, it.y + v.y) }.toSet()

enum class RockType (val points: Set<Point>) {
    MINUS(setOf(Point(0,0), Point(1, 0), Point(2, 0), Point(3, 0))),
    PLUS(setOf(Point(0, -1), Point(1, -1), Point(2, -1), Point(1, -2), Point(1, 0))),
    BACKWARD_L(setOf(Point(0,0), Point(1,0), Point(2,0), Point(2,-1), Point(2,-2))),
    TOWER(setOf(Point(0,0), Point(0,-1), Point(0,-2), Point(0,-3))),
    SQUARE(setOf(Point(0,0), Point(1,0), Point(0,-1), Point(1,-1)))
}

fun getRockAt(i: Int) = RockType.values()[i].points


