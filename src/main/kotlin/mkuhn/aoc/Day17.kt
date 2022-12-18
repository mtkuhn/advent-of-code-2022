package mkuhn.aoc

import mkuhn.aoc.util.Point

fun day17part1(input: List<String>): Long  = runRockSimulation(input, 2022)
fun day17part2(input: List<String>): Long  = runRockSimulation(input, 1000000000000L)

//todo: there should be a repeating pattern to this. The length of rock pattern to length of move pattern should cause a repeat
//but where does the pattern start from?
fun day17part2x(input: List<String>): Long {
    val patternLength = input.first().length.toLong() * RockType.values().size.toLong()
    val runCount = 1000000000000L
    val repeats = runCount/patternLength
    val remainder = runCount%patternLength

    (0..50).map { i ->
        runRockSimulation(input, i*patternLength)
    }.zipWithNext().map { it.first - it.second }.forEach { println(it) }

    return 1L
}

fun runRockSimulation(input: List<String>, rockCount: Long): Long {
    val gasJets = input.first().map { if(it == '<') -1 else 1 }
    val xBounds = (0 .. 6)
    val seen = mutableMapOf<CaveState, Pair<Int, Long>>()
    var runningHeight = 0 //todo: Long?
    var runningRockPile = mutableSetOf<Point>()
    var gasJetIndex = 0

    (0 until rockCount).forEach { ri ->
        val cave = CaveState(runningRockPile.toZeroBottom(), (ri%RockType.values().size).toInt(), gasJetIndex)
        if(seen.contains(cave)) {
            //short-circuit because we have a repeating pattern now
            val cycleLength = ri - seen[cave]!!.second
            val iterationsLeft = rockCount/cycleLength
            val heightPerCycle = runningHeight - seen[cave]!!.first
            val fullCyclicalHeight = iterationsLeft*heightPerCycle
            println("pattern: $cycleLength, $iterationsLeft, $heightPerCycle, $fullCyclicalHeight")
            return -(seen[cave]!!.first + fullCyclicalHeight)
        } else {
            //seen[cave] = runningHeight to ri

            var isResting = false
            var workingRock = getRockAt(cave.rockIndex).moveByVector(Point(2, -3+runningHeight)) //new rock at correct origin

            while(!isResting) {
                val jettedRock = workingRock.moveByVector(Point(gasJets[gasJetIndex], 0)) //move by gas jet
                if(jettedRock.none { it.x !in xBounds } && jettedRock notIntersects runningRockPile) { workingRock = jettedRock }
                gasJetIndex = (gasJetIndex+1)%gasJets.size

                val fallingRock = workingRock.moveByVector(Point(0, 1)) //drop
                if(fallingRock.none { it.y > 0 } && fallingRock notIntersects runningRockPile) { workingRock = fallingRock }
                else { isResting = true }
            }

            runningRockPile += workingRock
            runningRockPile = runningRockPile.trimToAccessibleFloor().toMutableSet()
            runningHeight = runningRockPile.minOf { it.y }-1

        }
    }

    //rockMass.print(rockHeight-5)

    return -runningHeight.toLong()
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


