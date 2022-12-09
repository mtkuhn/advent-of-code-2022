package mkuhn.aoc

import mkuhn.aoc.util.Point
import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.splitToPair

fun main() {
    val input = readInput("Day09")
    println(day09part1(input))
    println(day09part2(input))
}

fun day09part1(input: List<String>): Int {
    val moves = input.map { it.lineToMovement() }
    val rope = Rope(listOf(Point(0, 0), Point(0, 0)))
    return applyMovementAndGetTailPositions(rope, moves).count()
}

fun day09part2(input: List<String>): Int {
    val moves = input.map { it.lineToMovement() }
    val rope = Rope(List(10) { Point(0, 0) })
    return applyMovementAndGetTailPositions(rope, moves).count()
}

fun String.lineToMovement() = this.splitToPair(' ').let { it.first.first() to it.second.toInt() }

fun applyMovementAndGetTailPositions(rope: Rope, moves: List<Pair<Char, Int>>): Set<Point> =
    moves.flatMap { m -> (0 until m.second).map { m.first } }
        .fold(rope to setOf(rope.tail)) { acc, d ->
            acc.first.moveRopeInDirection(d).let { it to acc.second.plus(it.tail) }
        }.second

class Rope(val knots: List<Point>) {

    val head = knots.first()
    val tail = knots.last()

    fun moveRopeInDirection(direction: Char): Rope =
        mutableListOf(head.moveInDirection(direction))
            .apply { knots.drop(1).forEach { k -> this += k.moveToward(this.last()) } }
            .let { Rope(it) }

    private fun Point.moveInDirection(direction: Char) =
        when(direction) {
            'U' -> Point(x, y-1)
            'D' -> Point(x, y+1)
            'R' -> Point(x+1, y)
            'L' -> Point(x-1, y)
            else -> error("invalid movement")
        }

    private fun Point.moveToward(target: Point): Point =
        if(isTouching(target)) this
        else Point(x - x.compareTo(target.x), y - y.compareTo(target.y))
    
    private fun Point.isTouching(target: Point) =
        target.x in (this.x-1 .. this.x+1) && target.y in (this.y-1 .. this.y+1)
}