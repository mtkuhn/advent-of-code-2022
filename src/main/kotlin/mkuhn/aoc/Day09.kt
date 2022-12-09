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
    return applyMovementAndGetTailHistory(rope, moves).count()
}

fun day09part2(input: List<String>): Int {
    val moves = input.map { it.lineToMovement() }
    val rope = Rope(List(10) { Point(0, 0) })
    return applyMovementAndGetTailHistory(rope, moves).count()
}

fun String.lineToMovement() = this.splitToPair(' ').let { it.first.first() to it.second.toInt() }

fun applyMovementAndGetTailHistory(rope: Rope, moves: List<Pair<Char, Int>>): Set<Point> {
    var workingRope = rope
    val tailHistory = mutableSetOf(rope.tail)
    moves.forEach { m ->
        repeat(m.second) {
            workingRope = workingRope.moveRopeInDirection(m.first)
            tailHistory += workingRope.tail
        }
    }
    return tailHistory
}

data class Rope(val knots: List<Point>) {

    val head = knots.first()
    val tail = knots.last()

    fun moveRopeInDirection(direction: Char): Rope {
        val newHead = when(direction) {
            'U' -> Point(head.x, head.y-1)
            'D' -> Point(head.x, head.y+1)
            'R' -> Point(head.x+1, head.y)
            'L' -> Point(head.x-1, head.y)
            else -> error("invalid movement")
        }
        return mutableListOf(newHead).apply {
            knots.drop(1).forEach { k ->
                this += k.moveToward(this.last())
            }
        }.let { Rope(it) }
    }

    private fun Point.moveToward(target: Point): Point {
        var newX = this.x
        var newY = this.y
        if(!this.isTouching(target)) {
            if(this.y > target.y) { newY = this.y - 1 }
            if(this.y < target.y) { newY = this.y + 1 }
            if(this.x > target.x) { newX = this.x - 1 }
            if(this.x < target.x) { newX = this.x + 1 }
        }
        return Point(newX, newY)
    }
    
    private fun Point.isTouching(target: Point) =
        target.x in (this.x-1 .. this.x+1) && target.y in (this.y-1 .. this.y+1)
}