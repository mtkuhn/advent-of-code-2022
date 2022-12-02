package mkuhn.aoc

import readInput

fun main() {
    val input = readInput("Day02")
    println(day2part1(input))
    println(day2part2(input))
}

fun day2part1(input: List<String>): Int =
    input.map { l -> l.first().toRpsShape() to l.last().toRpsShape() }
        .sumOf { it.first.scoreAgainst(it.second) }

fun day2part2(input: List<String>): Int =
    input.map { l -> l.first().toRpsShape() to l.last() }
        .map { it.first to it.findPickToAchieveOutcome() }
        .sumOf { it.first.scoreAgainst(it.second) }

fun Char.toRpsShape(): RpsShape =
    RpsShape.values().find { it.pick == this || it.aliases.contains(this) } ?:error("invalid shape: $this")

fun Pair<RpsShape, Char>.findPickToAchieveOutcome(): RpsShape =
    when(this.second) {
        'X' /* lose */ -> this.first.defeats
        'Y' /* draw */ -> this.first.pick
        'Z' /* win */ -> this.first.defeatedBy
        else -> error("Invalid outcome ${this.second}")
    }.toRpsShape()

enum class RpsShape(val pick: Char, val aliases: List<Char>, val defeats: Char, val defeatedBy: Char, val value: Int) {
    ROCK('R', listOf('A', 'X'), 'S', 'P', 1),
    PAPER('P', listOf('B', 'Y'), 'R',  'S', 2),
    SCISSORS('S', listOf('C', 'Z'), 'P', 'R', 3);

    fun scoreAgainst(yourPick: RpsShape) =
        yourPick.value + when {
            yourPick.defeats == this.pick -> 6
            this.defeats == yourPick.pick -> 0
            else -> 3
        }
}
