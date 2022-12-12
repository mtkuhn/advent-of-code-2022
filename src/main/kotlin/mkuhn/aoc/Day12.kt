package mkuhn.aoc

import mkuhn.aoc.util.Grid
import mkuhn.aoc.util.Point
import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.transpose

fun main() {
    val input = readInput("Day12")
    println(day12part1(input))
    println(day12part2(input))
}

fun day12part1(input: List<String>): Int =
    (ElevationGrid(input.map { it.toList() }.transpose()).findShortestPath()?.size?:Int.MAX_VALUE)-1

fun day12part2(input: List<String>): Int {
    val grid = ElevationGrid(input.map { it.toList() }.transpose())
    return grid.allPoints()
        .filter { grid.height(it) == 0 }
        .map { grid.findShortestPath(fromPoint = it) }
        .minOf { it?.size?:Int.MAX_VALUE }-1
}


data class GraphNode(val currPos: Point, val visited: List<Point>)

class ElevationGrid(g: List<List<Char>>): Grid<Char>(g) {
    fun height(pos: Point): Int =
        when {
            valueAt(pos) == 'S' -> 'a'.code
            valueAt(pos) == 'E' -> 'z'.code
            else -> valueAt(pos).code
        } - 'a'.code

    fun findShortestPath(
        fromPoint: Point = allPoints().first { valueAt(it) == 'S' },
        toPoint: Point = allPoints().first { valueAt(it) == 'E' }
    ): List<Point>? {
        val root = GraphNode(fromPoint, listOf(fromPoint))
        var nodes = mutableListOf(root)
        val evaluatedPoints = mutableMapOf<Point, Int>()

        while (nodes.isNotEmpty() && !nodes.any { it.currPos == toPoint }) {
            //pop off our favorite node via A* search
            val best = nodes.minBy { it.visited.size + ('z'.code-height(it.currPos)) }
            nodes.remove(best)
            //println("nodes=${nodes.size}, eval ${best.currPos}, size ${best.visited.size} | ${best.visited}")

            //let's track points we've seen before, and keep only the most efficient nodes at that point
            if((evaluatedPoints[best.currPos]?:Int.MAX_VALUE) > best.visited.size) {
                evaluatedPoints[best.currPos] = best.visited.size
                nodes = nodes.filter { (evaluatedPoints[it.currPos]?:Int.MAX_VALUE) > it.visited.size }.toMutableList()
            }

            //expand to adjacent nodes and filter out bad choices
            nodes += cardinalAdjacentTo(best.currPos)
                .filter { height(it) <= height(best.currPos)+1 } //can't climb more than 1 height
                .filter { (evaluatedPoints[it]?:Int.MAX_VALUE) > best.visited.size+1 } //make sure we're not covering ground that we already did more efficiently
                .filter { it !in best.visited } //no looping around
                .map { p -> GraphNode(p, best.visited + p) }
        }

        return nodes.firstOrNull { it.currPos == toPoint }?.visited
    }
}

