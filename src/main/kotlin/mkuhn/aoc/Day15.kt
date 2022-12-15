package mkuhn.aoc

import mkuhn.aoc.util.Point
import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day15")
    println(day15part1(input, 2000000))
    println(day15part2(input, 0..4000000, 0..4000000))
    //5256611 too low
}

fun day15part1(input: List<String>, yToCheck: Int): Int {
    val sensorPairs = input.parseInputToPointPairs()
    val notInRange = sensorPairs.notInRangeForRow(yToCheck, sensorPairs.maxWidthForPointPairs())
    val beaconsInRow = sensorPairs.map { it.second }.distinct().count { it.y == yToCheck }
    return notInRange.size - beaconsInRow
}


fun day15part2(input: List<String>, xBounds: IntRange, yBounds: IntRange): Int {
    val sensorPairs = input.parseInputToPointPairs()
    val knownBeacons = sensorPairs.map { it.second }.toSet()
    val sensors = sensorPairs.map { it.first }.toSet()
    val possibleBeacons = yBounds.flatMap { y ->
        sensorPairs.inRangeForRow(y, xBounds).toSet()
    }

    val foundBeacon = possibleBeacons.minus(knownBeacons).minus(sensors).first()

    return (foundBeacon.x*4000000)+foundBeacon.y
}

val sensorRegex = """Sensor at x=([0-9-]+), y=([0-9-]+): closest beacon is at x=([0-9-]+), y=([0-9-]+)""".toRegex()

fun List<String>.parseInputToPointPairs(): List<Pair<Point, Point>> =
    mapNotNull { line ->
        sensorRegex.matchEntire(line)?.destructured?.let { (x1, y1, x2, y2) ->
            Point(x1.toInt(), y1.toInt()) to Point(x2.toInt(), y2.toInt())
        }
    }

fun List<Pair<Point, Point>>.notInRangeForRow(y: Int, xBounds: IntRange): List<Point> {
    return xBounds.map { Point(it, y) }.filter { p -> !p.outOfRange(this) }
}

fun List<Pair<Point, Point>>.inRangeForRow(y: Int, xBounds: IntRange): List<Point> {
    return xBounds.map { Point(it, y) }.filter { p -> p.outOfRange(this) }
}

fun List<Pair<Point, Point>>.maxWidthForPointPairs() =
    (minOfOrNull { it.first.x - it.first.manhattanDistance(it.second) }?:0)..
            (maxOfOrNull { it.first.x + it.first.manhattanDistance(it.second) }?:0)

fun Point.outOfRange(sensors: List<Pair<Point, Point>>): Boolean {
    //todo: maybe cache manhattan distance?
    return sensors.none { it.first.manhattanDistance(this) <= it.first.manhattanDistance(it.second) }
}