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
    val moves = input.map { it.splitToPair(' ') }
        .map { it.first.first() to it.second.toInt() }

    val history = mutableListOf(Rope())
    moves.forEach { m ->
        repeat(m.second) {
            history += history.last().moveRopeInDirection(m.first)
        }
        history.last()
    }
    return history.distinctBy { it.tail }.count()
}

fun day09part2(input: List<String>): Int =
    2

data class Rope(val head: Point = Point(0, 0), val tail: Point = Point(0, 0)) {
    fun moveRopeInDirection(direction: Char): Rope {
        val newHead = when(direction) {
            'U' -> Point(head.x, head.y-1)
            'D' -> Point(head.x, head.y+1)
            'R' -> Point(head.x+1, head.y)
            'L' -> Point(head.x-1, head.y)
            else -> error("invalid movement")
        }
        return Rope(newHead, tail.moveToward(newHead))
    }

    fun Point.moveToward(target: Point): Point {
        var newX = this.x
        var newY = this.y
        if(!this.isTouchingVertical(target)) {
            if(this.y > target.y) { newY = this.y - 1 }
            if(this.y < target.y) { newY = this.y + 1 }
        }
        if(!this.isTouchingHorizontal(target)) {
            if(this.x > target.x) { newX = this.x - 1 }
            if(this.x < target.x) { newX = this.x + 1 }
        }
        return Point(newX, newY)
    }
    
    fun Point.isTouchingHorizontal(target: Point) =
        target.x in (this.x-1 .. this.x+1) && target.y in (this.y-1 .. this.y+1)
    fun Point.isTouchingVertical(target: Point) =
        target.x in (this.x-1 .. this.x+1) && target.y in (this.y-1 .. this.y+1)
}