package mkuhn.aoc.util

data class Point(val x: Int, val y: Int)

open class Grid<T>(private val grid: List<List<T>>) {

    fun allNorth(pos: Point) =
        (0 until pos.y).map { Point(pos.x, it) }.sortedByDescending { it.y }

    fun allSouth(pos: Point) =
        (pos.y+1 until grid.first().size).map { Point(pos.x, it) }.sortedBy { it.y }

    fun allWest(pos: Point) =
        (0 until pos.x).map { Point(it, pos.y) }.sortedByDescending { it.x }

    fun allEast(pos: Point) =
        (pos.x+1 until grid.size).map { Point(it, pos.y) }.sortedBy { it.x }

    fun isInBounds(pos: Point) = pos.x in xBounds() && pos.y in yBounds()

    fun isEdge(pos: Point) =
        when {
            pos.x == 0 -> true
            pos.y == 0 -> true
            pos.x == xBounds().last -> true
            pos.y == yBounds().last -> true
            else -> false
        }

    fun allPoints() = grid.indices.flatMap { x ->
        (grid.first().indices).map { y -> Point(x, y) }
    }

    fun valueAt(pos: Point) = grid[pos.x][pos.y]

    fun xBounds() = grid.indices

    fun yBounds() = grid.first().indices
}