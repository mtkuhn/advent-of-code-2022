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
    val rock = input.flatMap { line -> line.parseToPoints().segmentsToPoints() }.toMutableSet()
    return rock.fillSandUntil(sandOrigin) { it == null }.size
}

fun day14part2(input: List<String>): Int {
    val sandOrigin = Point(500, 0)
    val rock = input.flatMap { line -> line.parseToPoints().segmentsToPoints() }.toMutableSet()

    val floorY = rock.maxOf { it.y }+2
    rock += listOf(Point(-5000, floorY), Point(5000, floorY)).segmentsToPoints()

    return rock.fillSandUntil(sandOrigin) { it == sandOrigin }.size
}

fun Set<Point>.fillSandUntil(sandOrigin: Point, goal: (Point?) -> Boolean): List<Point> {
    val obstructionHeightMap = mutableMapOf<Int, MutableList<Int>>()
    this.forEach { r -> obstructionHeightMap.addPoint(r) }
    val sand = mutableListOf<Point>()

    var newSand = obstructionHeightMap.findSandRestingPoint(sandOrigin)
    while(!goal(newSand)) {
        if(newSand == null) error("something went wrong")
        obstructionHeightMap.addPoint(newSand)
        sand += newSand
        newSand = obstructionHeightMap.findSandRestingPoint(sandOrigin)
    }
    if(newSand != null) sand += newSand

    return sand
}

fun String.parseToPoints() = split("""\D+""".toRegex()).chunked(2).map { Point(it[0].toInt(), it[1].toInt()) }

fun List<Point>.segmentsToPoints(): Set<Point> = this.zipWithNext().flatMap { it.segmentToPoints() }.toSet()

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

fun MutableMap<Int, MutableList<Int>>.findSandRestingPoint(sandOrigin: Point): Point? {
    val yIntersect = this[sandOrigin.x]?.filter { it > sandOrigin.y }?.minOrNull()
    return if(yIntersect == null) null //the abyss
    else {
        val leftDiagBlocked = this[sandOrigin.x-1]?.any { it == yIntersect }?:false
        val rightDiagBlocked = this[sandOrigin.x+1]?.any { it == yIntersect }?:false

        if(!leftDiagBlocked) findSandRestingPoint(Point(sandOrigin.x-1, yIntersect))
        else if(!rightDiagBlocked) findSandRestingPoint(Point(sandOrigin.x+1, yIntersect))
        else Point(sandOrigin.x, yIntersect-1)
    }
}