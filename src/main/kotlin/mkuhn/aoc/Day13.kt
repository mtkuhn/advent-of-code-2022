package mkuhn.aoc

import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.splitList

fun main() {
    val input = readInput("Day13")
    println(day13part1(input))
    println(day13part2(input))
}

fun day13part1(input: List<String>): Int =
    input.splitList("").asSequence()
        .map { l -> l.map { parseNestedListsOfInt(it.drop(1).dropLast(1)) } }
        .map { p -> p[0].comparePacketOrder(p[1]) == -1 }
        .withIndex()
        .filter { it.value }
        .sumOf { it.index+1 }

fun day13part2(input: List<String>): Int {
    val dividerPackets = listOf(listOf(listOf(2)), listOf(listOf(6)))
    return input.asSequence()
        .filter { it.isNotEmpty() }
        .map { l -> parseNestedListsOfInt(l.drop(1).dropLast(1)) }
        .plus(dividerPackets)
        .sortedWith(Any::comparePacketOrder)
        .withIndex()
        .filter { it.value in dividerPackets }
        .fold(1) { acc, i -> acc*(i.index+1) }
}

fun parseNestedListsOfInt(line: String): List<Any> {
    if (line.isEmpty()) return emptyList()

    var nestingLevel = 0
    val nestingChars = line.map { c ->
        if (c == '[') nestingLevel++
        else if (c == ']') nestingLevel--
        c to nestingLevel
    }

    return nestingChars.splitList(',' to 0).map { p ->
        p.map { it.first }
            .joinToString("")
            .let { it.toIntOrNull() ?: parseNestedListsOfInt(it.drop(1).dropLast(1)) }
    }
}

fun Any.comparePacketOrder(right: Any) =
    if(this is Int && right is Int) { compareTo(right) }
    else if (this is List<*> && right is List<*>) { comparePacketListOrder(right) }
    else { this.listIfNotList().comparePacketListOrder(right.listIfNotList()) }

fun Any.listIfNotList() = if(this is Int) listOf(this) else this as List<*>

fun List<*>.comparePacketListOrder(right: List<*>): Int =
    this.zip(right)
        .map { it.first!!.comparePacketOrder(it.second!!) } //hold my beer
        .firstOrNull { it != 0 }
        ?:this.size.compareTo(right.size)
