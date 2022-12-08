package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val ranges = readInput("Day04")
    println(day4part1(ranges))
    println(day4part2(ranges))
}

fun day4part1(input: List<String>): Int =
    input.map { it.toRangePair() }
        .count { it.first.contains(it.second) || it.second.contains(it.first) }

fun day4part2(input: List<String>): Int =
    input.map { it.toRangePair() }
        .count { it.first.overlaps(it.second) }

fun String.toRangePair() = this.substringBefore(",").toRange() to this.substringAfter(",").toRange()

fun String.toRange(): IntRange = this.substringBefore("-").toInt() .. this.substringAfter("-").toInt()

fun IntRange.contains(range: IntRange) = range.minus(this).isEmpty()

fun IntRange.overlaps(range: IntRange) = range.intersect(this).isNotEmpty()