package mkuhn.aoc

import readInput

fun main() {
    val input = readInput("Day05")
    println(day05part1(input))
    println(day05part2(input))
}

fun day05part1(input: List<String>): String {
    val crates = input.takeWhile { it.isNotEmpty() }.linesToCrateStacks()
    val moves = input.dropWhile { it.isNotEmpty() }.drop(1).map { it.toCrateMove() }

    moves.forEach { move ->
        repeat(move.count) {
            crates[move.destination-1].add(0, crates[move.source-1].removeFirst())
        }
    }

    return crates.map { it.first() }.joinToString("")
}

fun List<String>.linesToCrateStacks(): MutableList<MutableList<Char>> {
    val stackCount = this.last().takeLast(2).trim().toInt()
    val stacks = mutableListOf<MutableList<Char>>().apply {
        (0 until stackCount).forEach { this += mutableListOf<Char>() }
    }
    this.dropLast(1).map { line ->
        (0 until stackCount).forEach { i ->
            line[1+i*4].apply {
                if(!this.isWhitespace()) stacks.elementAt(i) += line[1+i*4]
            }
        }
    }
    return stacks
}

data class CrateMove(val count: Int, val source: Int, val destination: Int)
val moveRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()

fun String.toCrateMove(): CrateMove =
    moveRegex.find(this)!!.destructured
        .let { (c, s, d) -> CrateMove(c.toInt(), s.toInt(), d.toInt()) }

fun day05part2(input: List<String>): String {
    val crates = input.takeWhile { it.isNotEmpty() }.linesToCrateStacks()
    val moves = input.dropWhile { it.isNotEmpty() }.drop(1).map { it.toCrateMove() }

    moves.forEach { move ->
        crates[move.destination-1].addAll(0, crates[move.source-1].removeFirst(move.count))
    }

    return crates.map { it.first() }.joinToString("")
}

fun MutableList<Char>.removeFirst(count: Int) = this.take(count)
    .apply { repeat(count) { this@removeFirst.removeFirst() } }