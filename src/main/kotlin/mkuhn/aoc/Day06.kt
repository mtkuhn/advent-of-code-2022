package mkuhn.aoc

import readInput

fun main() {
    val input = readInput("Day06").first()
    println(day06part1(input))
    println(day06part2(input))
}

fun day06part1(input: String): Int = input.indexOfDistinctChars(4)

fun day06part2(input: String): Int = input.indexOfDistinctChars(14)

fun String.indexOfDistinctChars(length: Int) : Int =
    this.windowed(length).indexOfFirst { it.toSet().size == length } + length