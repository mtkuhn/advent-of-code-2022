package mkuhn.aoc

import mkuhn.aoc.util.Point
import mkuhn.aoc.util.overlaps
import mkuhn.aoc.util.readInput
import kotlin.math.abs

fun main() {
    val input = readInput("Day15")
    println(day15part1(input, 2000000))
    println(day15part2(input, 0..4000000, 0..4000000))
}

fun day15part1(input: List<String>, yToCheck: Int): Int {
    val sensorPairs = input.parseInputToPointPairs()
    val sensorsToDist = sensorPairs.map { it.first to it.first.manhattanDistance(it.second) }
    val notInRange = sensorsToDist.pointsInRangeOfSensorsAtY(yToCheck, sensorPairs.maxWidthForPointPairs())
    val beaconsInRow = sensorPairs.map { it.second }.distinct().count { it.y == yToCheck }
    return notInRange.size - beaconsInRow
}


fun day15part2(input: List<String>, xBounds: IntRange, yBounds: IntRange): Long {
    val sensorPairs = input.parseInputToPointPairs()
    val sensorsToDist = sensorPairs.map { it.first to it.first.manhattanDistance(it.second) }
    val foundBeacon = yBounds.asSequence()
        .flatMap { y -> sensorsToDist.pointsNotInRangeOfSensorsAtY(y, xBounds) }
        .first()

    return (foundBeacon.x.toLong()*4000000L)+foundBeacon.y.toLong()
}

val sensorRegex = """Sensor at x=([0-9-]+), y=([0-9-]+): closest beacon is at x=([0-9-]+), y=([0-9-]+)""".toRegex()

fun List<String>.parseInputToPointPairs(): List<Pair<Point, Point>> =
    mapNotNull { line ->
        sensorRegex.matchEntire(line)?.destructured?.let { (x1, y1, x2, y2) ->
            Point(x1.toInt(), y1.toInt()) to Point(x2.toInt(), y2.toInt())
        }
    }

fun List<Pair<Point, Int>>.pointsInRangeOfSensorsAtY(y: Int, xBounds: IntRange): List<Point> {
    return xBounds.map { Point(it, y) }.filter { p -> !p.outOfRange(this) }
}

fun List<Pair<Point, Int>>.pointsNotInRangeOfSensorsAtY(y: Int, xBounds: IntRange): List<Point> =
    this.map { it.findSensorXRangeAtY(y) }
        .fold(listOf(xBounds)) { acc, r -> acc.removeRange(r) } //remove beacon ranges from full x range
        .flatMap { it.toSet() } //extract remaining x
        .map { Point(it, y) }

fun Pair<Point, Int>.findSensorXRangeAtY(y: Int): IntRange {
    val xDistance = abs(this.second - abs(this.first.y - y))
    val leftX = this.first.x - xDistance
    val rightX = this.first.x + xDistance
    return if(Point(leftX, y).manhattanDistance(this.first) > this.second) IntRange.EMPTY
    else leftX .. rightX
}

fun List<IntRange>.removeRange(removeRange: IntRange): List<IntRange> =
    this.flatMap { r ->
        if(removeRange overlaps r) {
            val newRanges = mutableListOf<IntRange>()
            if(removeRange.last > r.first) { newRanges += removeRange.last+1 .. r.last }
            if(removeRange.first < r.last) { newRanges += r.first until removeRange.first }
            newRanges
        }
        else { listOf(r) }
    }

fun List<Pair<Point, Point>>.maxWidthForPointPairs() =
    (minOfOrNull { it.first.x - it.first.manhattanDistance(it.second) }?:0)..
            (maxOfOrNull { it.first.x + it.first.manhattanDistance(it.second) }?:0)

fun Point.outOfRange(sensors: List<Pair<Point, Int>>): Boolean {
    return sensors.none { it.first.manhattanDistance(this) <= it.second }
}