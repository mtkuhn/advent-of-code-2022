package mkuhn.aoc

import mkuhn.aoc.util.*

fun main() {
    val input = readInput("Day08")
    println(day08part1(input))
    println(day08part2(input))
}

fun day08part1(input: List<String>): Int =
    TreeGrid(input.map { l -> l.map { it.digitToInt() } }.transpose())
        .findVisibleTrees()
        .count()

fun day08part2(input: List<String>): Int =
    TreeGrid(input.map { l -> l.map { it.digitToInt() } }.transpose())
        .findHighestScenicScore()

class TreeGrid(grid: List<List<Int>>): Grid<Int>(grid) {

    enum class Direction {NORTH, SOUTH, EAST, WEST}

    fun findVisibleTrees() =
        allPoints().filter { isTreeVisible(it) }

    fun findHighestScenicScore() =
        allPoints().maxOf { scenicScore(it) }

    fun scenicScore(pos: Point) =
        Direction.values().fold(1) { acc, direction ->
            acc * viewingDistance(pos, direction)
        }

    fun viewingDistance(pos: Point, direction: Direction) =
        when(direction) {
            Direction.NORTH -> allNorth(pos)
            Direction.SOUTH -> allSouth(pos)
            Direction.EAST -> allEast(pos)
            Direction.WEST -> allWest(pos)
        }.takeWhileInclusive { valueAt(it) < valueAt(pos) }.count()

    fun isTreeVisible(pos: Point) =
        when {
            !isInBounds(pos) -> error("invalid coordinates $pos")
            isEdge(pos) -> true
            allWest(pos).all { valueAt(it) < valueAt(pos) } -> true
            allEast(pos).all { valueAt(it) < valueAt(pos) } -> true
            allNorth(pos).all { valueAt(it) < valueAt(pos) } -> true
            allSouth(pos).all { valueAt(it) < valueAt(pos) } -> true
            else -> false
        }
}