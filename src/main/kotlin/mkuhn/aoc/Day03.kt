package mkuhn.aoc

import mkuhn.aoc.util.intersectAll
import mkuhn.aoc.util.readInput

fun main() {
    val rucksacks = readInput("Day03")
    println(day3part1(rucksacks))
    println(day3part2(rucksacks))
}

fun day3part1(input: List<String>): Int =
    input.map { it.take(it.length/2).toSet() to it.takeLast(it.length/2).toSet() }
        .map { it.first intersect it.second }
        .sumOf { it.first().toRucksackPriority() }

fun day3part2(input: List<String>): Int =
    input.map { it.toSet() }
        .chunked(3)
        .map { it.intersectAll().first() }
        .sumOf { it.toRucksackPriority() }

fun Char.toRucksackPriority(): Int =
    if(this.isUpperCase()) {
        this.code - 'A'.code + 27
    } else {
        this.code - 'a'.code + 1
    }