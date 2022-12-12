package mkuhn.aoc

import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.splitList

fun main() {
    val input = readInput("Day11")
    println(day11part1(input))
    println(day11part2(input))
}

fun day11part1(input: List<String>): Long  {
    val monkeys = Monkey.fromInputList(input)
    (1 .. 20).forEach { round ->
        monkeys.forEach { monkey ->
            monkey.inspectItems(3, monkeys)
        }
    }
    return monkeys.map { it.inspectionCount }.sortedDescending().let { it[0] * it[1] }

}

fun day11part2(input: List<String>): Long {
    val monkeys = Monkey.fromInputList(input)
    val modulus = monkeys.map { it.testDivisor }.reduce(Long::times)
    (1 .. 10000).forEach { round ->
        monkeys.forEach { monkey ->
            monkey.inspectItems(1, monkeys, modulus)
        }
    }
    return monkeys.map { it.inspectionCount }.sortedDescending().let { it[0] * it[1] }

}

data class Monkey(val itemWorryLevels: MutableList<Long>,
                  val operation: (Long) -> (Long),
                  val testDivisor: Long,
                  val trueRecipientIndex: Int,
                  val falseRecipientIndex: Int,
                  var inspectionCount: Long = 0L) {
    companion object {
        fun fromInputList(input: List<String>) =
            input.splitList("").map { m ->
                Monkey(
                    m[1].substringAfter("items: ").split(", ").map { it.toLong() }.toMutableList(),
                    m[2].substringAfter("new = ").split(" ").let {
                        when {
                            it[1] == "*" && it[2] == "old" -> { old -> old * old }
                            it[1] == "+" && it[2] == "old" -> { old -> old + old }
                            it[1] == "+" -> { old -> old + it[2].toLong() }
                            it[1] == "*" -> { old -> old * it[2].toLong() }
                            else -> error("bad operation")
                        }
                    },
                    m[3].substringAfter("by ").toLong(),
                    m[4].substringAfter("monkey ").toInt(),
                    m[5].substringAfter("monkey ").toInt()
                )
            }
    }

    fun inspectItems(worryDivisor: Long, monkeys: List<Monkey>, modulus: Long? = null) {
        while(itemWorryLevels.isNotEmpty()) {
            val inWorryLevel = itemWorryLevels.removeFirst()
            val newWorryLevel = (operation(inWorryLevel)/worryDivisor).let { if(modulus != null) it % modulus else it }

            val recipient = if(newWorryLevel%testDivisor == 0L) trueRecipientIndex else falseRecipientIndex
            monkeys[recipient].itemWorryLevels += newWorryLevel

            inspectionCount += 1L
        }
    }
}