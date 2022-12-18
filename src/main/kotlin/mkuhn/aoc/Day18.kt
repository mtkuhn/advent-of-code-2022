package mkuhn.aoc

import mkuhn.aoc.util.Point
import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day18")
    println(day18part1(input))
    println(day18part2(input))
}

fun day18part1(input: List<String>): Int {
    val cubeField = input.map { Point3D.fromString(it) }.toSet()
    return cubeField.sumOf { 6 - (it.getCardinalNeighbors().toSet() intersect cubeField).size }
}


fun day18part2(input: List<String>): Int {
    val cubeField = input.map { Point3D.fromString(it) }.toSet()

    val airCubes = cubeField.invertCubes()
    println(airCubes.getBounds().let { it.first.last*it.second.last*it.third.last })

    val startingPoint = airCubes.minBy { it.x } //something we know is external
    val externalAirCubes = startingPoint.findJoiningCubesBFS(airCubes)
    //val externalAirCubes = startingPoint.findJoiningCubesDFS(airCubes, mutableSetOf<Point3D>()) //186
    val airGaps = airCubes subtract externalAirCubes
    val filledCubeField = cubeField union airGaps //fill in original cubeField to simplify things
    println("extAirCubes=${externalAirCubes.size}, airGaps=${airGaps.size}, filledCubeField=${filledCubeField.size}")

    val facesCount = cubeField.sumOf {
        6 - (it.getCardinalNeighbors().toSet() intersect filledCubeField).size
    }

    return facesCount
}

fun Point3D.findJoiningCubesBFS(allPoints: Set<Point3D>): Set<Point3D> {
    val allFound = mutableSetOf<Point3D>()
    var n = listOf(this)
    while(n.isNotEmpty()) {
        allFound += n
        n = n.flatMap { c -> c.getCardinalNeighbors() }
            .filter { it !in allFound }
            .filter { it in allPoints }
    }

    return allFound
}

fun Point3D.findJoiningCubesDFS(allPoints: Set<Point3D>, visited: MutableSet<Point3D>): Set<Point3D> {
    return getCardinalNeighbors()
        .filter { it in allPoints }
        .filter { it !in visited }
        .apply { visited += this@findJoiningCubesDFS }
        .flatMap { it.findJoiningCubesDFS(allPoints, visited) }
        .toSet()
}

fun Set<Point3D>.invertCubes(): Set<Point3D> {
    val bounds = this.getBounds(1)
    return bounds.first.flatMap { x ->
        bounds.second.flatMap { y ->
            bounds.third.map { z ->
                Point3D(x, y, z)
            }
        }
    }.toSet() subtract this
}

fun Set<Point3D>.getBounds(buffer: Int = 0): Triple<IntRange, IntRange, IntRange> {
    val xBounds = minOf { it.x }-buffer .. maxOf { it.x }+buffer
    val yBounds = minOf { it.y }-buffer .. maxOf { it.y }+buffer
    val zBounds = minOf { it.z }-buffer .. maxOf { it.z }+buffer
    return Triple(xBounds, yBounds, zBounds)
}

data class Point3D(val x: Int, val y: Int, val z: Int) {
    fun getCardinalNeighbors(): Set<Point3D> =
        setOf(
            Point3D(x-1, y, z), Point3D(x+1, y, z),
            Point3D(x, y-1, z), Point3D(x, y+1, z),
            Point3D(x, y, z-1), Point3D(x, y, z+1)
        )

    fun isInBounds(bounds: Triple<IntRange, IntRange, IntRange>) =
        x in bounds.first && y in bounds.second && z in bounds.third

    companion object {
        fun fromString(str: String) = str.split(',')
            .map { it.toInt() }
            .let { (x, y, z) -> Point3D(x, y, z) }
    }
}