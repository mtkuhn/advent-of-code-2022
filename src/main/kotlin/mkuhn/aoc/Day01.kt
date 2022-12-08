package mkuhn.aoc

import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.splitList

fun main() {
    val input = readInput("Day01").splitList("")
    println(day1part1(input))
    println(day1part2(input))
}

fun day1part1(input: List<List<String>>): Int {
    return input.maxOf { l -> l.sumOf(String::toInt) }
}

fun day1part2(input: List<List<String>>): Int {
    return input.map { it.sumOf(String::toInt) }
        .sorted()
        .takeLast(3)
        .sum()
}