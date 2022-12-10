package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day10")
    println(day10part1(input))
    println(day10part2(input))
}

fun day10part1(input: List<String>): Int =
    input.asSequence()
        .map { it.parseInstruction() }
        .padCyclesWithFalseInstruction()
        .foldIntoCycleState()
        .filter { it.cycle in listOf(20, 60, 100, 140, 180, 220)}
        .sumOf { it.cycle * it.xDuring }

fun day10part2(input: List<String>): String =
    input.asSequence()
        .map { it.parseInstruction() }
        .padCyclesWithFalseInstruction()
        .foldIntoCycleState()
        .map { it.drawPixel }
        .chunked(40).joinToString("\n") { it.joinToString("") }

data class CycleState(val cycle: Int, val xDuring: Int, val xAfter: Int, val drawPixel: Char)

fun String.parseInstruction() = this.substringBefore(" ") to this.substringAfter(" ", "0").toInt()

fun Sequence<Pair<String, Int>>.padCyclesWithFalseInstruction() =
    this.flatMap { instr ->
        when(instr.first) {
            "addx" -> listOf("addx-working" to 0, instr)
            "noop" -> listOf(instr)
            else -> error("bad instruction")
        }
    }

fun Sequence<Pair<String, Int>>.foldIntoCycleState() =
    this.runningFold(CycleState(0, 1, 1,'#')) { acc, instr ->
        CycleState(
            acc.cycle + 1,
            acc.xAfter,
            acc.xAfter + if (instr.first == "addx") instr.second else 0,
            if ((acc.cycle) % 40 in (acc.xAfter - 1..acc.xAfter + 1)) '#' else '.'
        )
    }.drop(1)