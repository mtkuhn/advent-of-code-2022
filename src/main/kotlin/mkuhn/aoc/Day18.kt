package mkuhn.aoc

fun day18part1(input: List<String>): Int {
    val cubeField = input.map { Point3D.fromString(it) }.toSet()
    return cubeField.countExposedFaces()
}


fun day18part2(input: List<String>): Int {
    val cubeField = input.map { Point3D.fromString(it) }.toSet()
    val airCubes = cubeField.invertCubes()
    return airCubes.minBy { it.x } //a point where know is not in an air bubble
        .findJoiningCubes(airCubes)  //search for all linked cubes outside of the structure
        .let { airCubes subtract it } //subtract those linked cubes from all air cubes, to leave just bubbles
        .union(cubeField) //go back to the rock points, but fill in all the bubbles
        .countExposedFaces()
}

fun Set<Point3D>.countExposedFaces() = sumOf { 6 - (it.getCardinalNeighbors().toSet() intersect this).size }

fun Point3D.findJoiningCubes(allPoints: Set<Point3D>): Set<Point3D> {
    val allFound = mutableSetOf<Point3D>()
    var n = listOf(this)
    while(n.isNotEmpty()) {
        allFound += n
        n = n.flatMap { c -> c.getCardinalNeighbors() }.toSet()
            .filter { it !in allFound }
            .filter { it in allPoints }
    }

    return allFound
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

    companion object {
        fun fromString(str: String) = str.split(',')
            .map { it.toInt() }
            .let { (x, y, z) -> Point3D(x, y, z) }
    }
}