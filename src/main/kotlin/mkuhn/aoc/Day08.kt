package mkuhn.aoc

import readInput
import takeWhileInclusive
import transpose

fun main() {
    val input = readInput("Day08")
    println(day08part1(input))
    println(day08part2(input))
}

fun day08part1(input: List<String>): Int =
    IntGrid(input.map { l -> l.map { it.digitToInt() } }.transpose())
        .findVisibleTrees()
        .count()

fun day08part2(input: List<String>): Int =
    IntGrid(input.map { l -> l.map { it.digitToInt() } }.transpose())
        .findHighestScenicScore()

class IntGrid(private val grid: List<List<Int>>) {

    enum class Direction {NORTH, SOUTH, EAST, WEST}

    fun findVisibleTrees() =
        allPositions().filter { isTreeVisible(it) }

    fun findHighestScenicScore() =
        allPositions().map { scenicScore(it) }.apply { println(this) }
            .max()

    fun scenicScore(pos: Pair<Int, Int>) =
        Direction.values().fold(1) { acc, direction ->
            acc * viewingDistance(pos, direction)
        }

    fun viewingDistance(pos: Pair<Int, Int>, direction: Direction) =
        when(direction) {
            Direction.NORTH -> allNorth(pos)
            Direction.SOUTH -> allSouth(pos)
            Direction.EAST -> allEast(pos)
            Direction.WEST -> allWest(pos)
        }.takeWhileInclusive { at(it) < at(pos) }.count()

    fun isTreeVisible(pos: Pair<Int, Int>) =
        when {
            !isInBounds(pos) -> error("invalid coordinates $pos")
            isEdge(pos) -> true
            allWest(pos).all { at(it) < at(pos) } -> true
            allEast(pos).all { at(it) < at(pos) } -> true
            allNorth(pos).all { at(it) < at(pos) } -> true
            allSouth(pos).all { at(it) < at(pos) } -> true
            else -> false
        }

    fun allNorth(pos: Pair<Int, Int>) = (0 until pos.second).map { pos.first to it }.reversed()

    fun allSouth(pos: Pair<Int, Int>) = (pos.second+1 until grid.first().size).map { pos.first to it }

    fun allWest(pos: Pair<Int, Int>) = (0 until pos.first).map { it to pos.second }.reversed()

    fun allEast(pos: Pair<Int, Int>) = (pos.first+1 until grid.size).map { it to pos.second }

    fun isInBounds(pos: Pair<Int, Int>) = pos.first in xBounds() && pos.second in yBounds()

    fun isEdge(pos: Pair<Int, Int>) =
        when {
            pos.first == 0 -> true
            pos.second == 0 -> true
            pos.first == xBounds().last -> true
            pos.second == yBounds().last -> true
            else -> false
        }

    fun allPositions() = grid.indices.flatMap { x ->
        (grid.first().indices).map { y -> x to y }
    }

    fun at(pos: Pair<Int, Int>) = grid[pos.first][pos.second]

    fun xBounds() = grid.indices

    fun yBounds() = grid.first().indices
}