@file:Suppress("UNCHECKED_CAST")

package mkuhn.aoc

import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.transpose

fun main() {
    val input = readInput("Day05")
    println(day05part1(input))
    println(day05part2(input))
}

fun day05part1(input: List<String>): String {
    val crates: MutableList<MutableList<Char>> = input.takeWhile { it.isNotEmpty() }.linesToCrateStacks().apply { print(this) }
    val moves = input.dropWhile { it.isNotEmpty() }.drop(1).map { CrateMove.fromString(it) }

    moves.forEach { move ->
        repeat(move.count) {
            crates[move.destination-1].add(0, crates[move.source-1].removeFirst())
        }
    }

    return crates.map { it.first() }.joinToString("")
}

fun day05part2(input: List<String>): String {
    val crates = input.takeWhile { it.isNotEmpty() }.linesToCrateStacks()
    val moves = input.dropWhile { it.isNotEmpty() }.drop(1).map { CrateMove.fromString(it) }

    moves.forEach { move ->
        crates[move.destination-1].addAll(0, crates[move.source-1].takeAndRemove(move.count))
    }

    return crates.map { it.first() }.joinToString("")
}

data class CrateMove(val count: Int, val source: Int, val destination: Int) {
    companion object {
        private val moveRegex = """^move (\d+) from (\d+) to (\d+)$""".toRegex()

        fun fromString(str: String): CrateMove = moveRegex.find(str)?.destructured?.let { (c, s, d) ->
            CrateMove(c.toInt(), s.toInt(), d.toInt())
        }?:error("bad input")
    }
}

fun String.lineToCrateStackRow(): List<Char?> =
    this.drop(1).filterIndexed { i, _ -> i%4 == 0 }.map {
        if(it.isWhitespace()) null
        else it
    }

fun List<String>.linesToCrateStacks(): MutableList<MutableList<Char>> =
    this.dropLast(1).map { it.lineToCrateStackRow() }.transpose { it != null } as MutableList<MutableList<Char>>

fun MutableList<Char>.takeAndRemove(count: Int): List<Char> =
    this.take(count).apply { repeat(count) { this@takeAndRemove.removeFirst() } }