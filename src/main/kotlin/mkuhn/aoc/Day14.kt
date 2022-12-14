package mkuhn.aoc

import mkuhn.aoc.util.Point
import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day14")
    println(day14part1(input))
    println(day14part2(input))
}

fun day14part1(input: List<String>): Int {
    val sandOrigin = Point(500, 0)
    val rockPoints = input.parseInputToRockPoints()
    return rockPoints.fillSandUntil(sandOrigin) { it == null }.size
}

fun day14part2(input: List<String>): Int {
    val sandOrigin = Point(500, 0)
    val rockPoints = input.parseInputToRockPoints()

    val floorY = rockPoints.maxOf { it.y }+2
    rockPoints += (Point(-5000, floorY) to Point(5000, floorY)).segmentToPoints()

    return rockPoints.fillSandUntil(sandOrigin) { it == sandOrigin }.size+1
}

fun Set<Point>.fillSandUntil(sandOrigin: Point, goal: (Point?) -> Boolean): List<Point> {
    val obstructionHeightMap = mutableMapOf<Int, MutableList<Int>>()
    this.forEach { r -> obstructionHeightMap.addPoint(r) }
    return generateSequence { sandOrigin }
        .map { obstructionHeightMap.dropSandOrNull(it) }
        .takeWhile { !goal(it) }
        .filterNotNull()
        .toList()
}

fun List<String>.parseInputToRockPoints() =
    flatMap { line -> line.parseLineToPoints()
        .zipWithNext()
        .flatMap { it.segmentToPoints() }
    }.toMutableSet()

fun String.parseLineToPoints() = split("""\D+""".toRegex()).chunked(2).map { Point(it[0].toInt(), it[1].toInt()) }

fun Pair<Point, Point>.segmentToPoints(): Set<Point> =
    (first.x.progressBetween(second.x)).flatMap { xx ->
        (first.y.progressBetween(second.y)).map { yy ->
            Point(xx, yy)
        }
    }.toSet()

fun Int.progressBetween(i: Int) = IntProgression.fromClosedRange(this, i, if(this > i) -1 else 1)

fun MutableMap<Int, MutableList<Int>>.addPoint(p: Point) {
    if(this[p.x] == null) { this[p.x] = mutableListOf(p.y) }
    else { this[p.x]?.add(p.y) }
}
fun MutableMap<Int, MutableList<Int>>.dropSandOrNull(sandOrigin: Point): Point? {
    val newSand = findSandRestingPoint(sandOrigin)
    if(newSand != null) addPoint(newSand)
    return newSand
}

fun MutableMap<Int, MutableList<Int>>.findSandRestingPoint(sandOrigin: Point): Point? {
    val yIntersect = this[sandOrigin.x]?.filter { it > sandOrigin.y }?.minOrNull()
    return when {
        yIntersect == null -> null
        isOpen(Point(sandOrigin.x-1, yIntersect)) -> findSandRestingPoint(Point(sandOrigin.x-1, yIntersect))
        isOpen(Point(sandOrigin.x+1, yIntersect)) -> findSandRestingPoint(Point(sandOrigin.x+1, yIntersect))
        else -> Point(sandOrigin.x, yIntersect-1)
    }
}

fun MutableMap<Int, MutableList<Int>>.isOpen(p: Point) = this[p.x]?.none { it == p.y }?:true