package mkuhn.aoc

import mkuhn.aoc.util.Point
import mkuhn.aoc.util.readInput
import java.math.BigInteger

fun main() {
    val input = readInput("Day17")
    println(day17part1(input))
    println(day17part2(input))
}

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
    val rockSeq = getRockSequence(rockCount) //2022
    val gasSeq = getGasSequence(input.first()).iterator()

    val xBounds = (0 .. 6)
    var rockHeight = 0
    var rockMass = mutableSetOf<Point>()

    val caveHistory = mapOf<Set<Point>, Pair<Int, Int>>() //todo: needs more data

    rockSeq.forEach { rr ->

        var isResting = false
        var workingRock = rr

        workingRock = workingRock.moveByVector(Point(2, -3+rockHeight)) //center on correct origin

        while(!isResting) {
            val jettedRock = workingRock.moveByVector(Point(gasSeq.next(), 0)) //move by gas jet
            if(jettedRock.none { it.x !in xBounds } && jettedRock notintersects rockMass) { workingRock = jettedRock }

            val fallingRock = workingRock.moveByVector(Point(0, 1)) //drop
            if(fallingRock.none { it.y > 0 } && fallingRock notintersects rockMass) { workingRock = fallingRock }
            else { isResting = true }
        }


        val thisHeight = workingRock.minOf { it.y }-1
        if(thisHeight < rockHeight) { rockHeight = thisHeight }

        rockMass += workingRock

        //cut off useless points
        val floor = rockMass.groupBy { it.x }.map { l -> l.value.minOf { it.y } }.max()
        if(floor > 0) {
            rockMass = rockMass.filter { it.y >= floor }.toMutableSet()
        }
    }

    //rockMass.print(rockHeight-5)

    return -rockHeight.toLong()
}

infix fun Set<Point>.notintersects(otherSet: Set<Point>) = none { it in otherSet }

fun Set<Point>.print(height: Int) {
    (height .. 0).map { y ->
        (0..6).map { x ->
            if(Point(x, y) in this) "#"
            else '.'
        }.joinToString("")
    }.joinToString("\n").apply { println(this); println() }
}

fun Set<Point>.moveByVector(v: Point): Set<Point> = map { Point(it.x + v.x, it.y + v.y) }.toSet()

data class Cave(val landedRock: Set<Point>, val lastRockType: RockType, val lastGasJet: Int) {

}

enum class RockType (val points: Set<Point>) {
    MINUS(setOf(Point(0,0), Point(1, 0), Point(2, 0), Point(3, 0))),
    PLUS(setOf(Point(0, -1), Point(1, -1), Point(2, -1), Point(1, -2), Point(1, 0))),
    BACKWARD_L(setOf(Point(0,0), Point(1,0), Point(2,0), Point(2,-1), Point(2,-2))),
    TOWER(setOf(Point(0,0), Point(0,-1), Point(0,-2), Point(0,-3))),
    SQUARE(setOf(Point(0,0), Point(1,0), Point(0,-1), Point(1,-1)))
}

//thanks stackoverflow
fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

fun getRockSequence(count: Long) = (0 until count).asSequence()
    .map { RockType.values()[(it%RockType.values().size).toInt()] }
    .map { it.points }


fun getGasSequence(input: String) =
    input.asSequence().map { if(it == '<') -1 else 1 }.repeat()


