package mkuhn.aoc

import mkuhn.aoc.util.Point
import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day17")
    println(day17part1(input))
    println(day17part2(input))
}

fun day17part1(input: List<String>): Int {
    val rockSeq = getRockSequence(2022) //2022
    val gasSeq = getGasSequence(input.first()).iterator()

    val xBounds = (0 .. 6)
    val yMin = 0
    var rockHeight = 0
    val rockMass = mutableSetOf<Point>()
    var i = 0

    rockSeq.forEach { rr ->

        var isResting = false
        var workingRock = rr

        workingRock = workingRock.moveByVector(Point(2, -3+rockHeight)) //center on correct origin

        while(!isResting) {
            val jettedRock = workingRock.moveByVector(Point(gasSeq.next(), 0)) //move by gas jet
            if(jettedRock.points.none { it.x !in xBounds } && jettedRock.points.none { it in rockMass }) { workingRock = jettedRock }

            val fallingRock = workingRock.moveByVector(Point(0, 1)) //drop
            if(fallingRock.points.none { it.y > yMin } && fallingRock.points.none { it in rockMass }) { workingRock = fallingRock }
            else { isResting = true }
        }


        val thisHeight = workingRock.points.minOf { it.y }-1
        if(thisHeight < rockHeight) { rockHeight = thisHeight }

        rockMass += workingRock.points

        //cut off useless points
        //val floor = rockMass.groupBy { it.x }.map { l -> l.value.minOf { it.y } }.max()
        //if(floor > 0) rockMass.removeIf { it.y < floor }
    }

    //rockMass.print(rockHeight-5)

    return -rockHeight
}

fun Set<Point>.print(height: Int) {
    (height .. 0).map { y ->
        (0..6).map { x ->
            if(Point(x, y) in this) "#"
            else '.'
        }.joinToString("")
    }.joinToString("\n").apply { println(this); println() }
}

fun day17part2(input: List<String>): Int =
    2

class Rock(val points: Set<Point>) {
    fun moveByVector(v: Point): Rock {
        return Rock(points.map { Point(it.x + v.x, it.y + v.y) }.toSet())
    }
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

fun getRockSequence(count: Int) = (0 until count).asSequence()
    .map { RockType.values()[it%RockType.values().size] }
    .map { Rock(it.points) }


fun getGasSequence(input: String) =
    input.asSequence().map { if(it == '<') -1 else 1 }.repeat()


