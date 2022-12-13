package mkuhn.aoc

import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.splitList

fun main() {
    val input = readInput("Day13")
    println(day13part1(input))
    println(day13part2(input))
}

fun day13part1(input: List<String>): Int =
    input.splitList("").map { l -> l.map{ parseNestedListsOfInt(it.drop(1).dropLast(1)) } }
        .mapIndexed { idx, ele -> idx+1 to isRightOrder(ele[0] to ele[1]) }
        .filter { it.second == Result.ORDERED }
        .sumOf { it.first }

fun day13part2(input: List<String>): Int = 1

fun parseNestedListsOfInt(line: String): List<Any> {
    if (line.isEmpty()) return emptyList()

    var nestingLevel = 0
    val nestingChars = line.map { c ->
        if (c == '[') {
            nestingLevel++
        } else if (c == ']') {
            nestingLevel--
        }
        c to nestingLevel
    }

    return nestingChars.splitList(',' to 0).map { p ->
        p.map { it.first }.joinToString("").let {
            it.toIntOrNull() ?: parseNestedListsOfInt(it.drop(1).dropLast(1))
        }
    }
}

fun isRightOrder(pair: Pair<Any, Any>): Result =
    if(pair.first is Int && pair.second is Int) {
        isRightOrderInts(pair.first as Int to pair.second as Int)
    }
    else if (pair.first is List<*> && pair.second is List<*>) {
        isRightOrderLists(pair.first as List<*> to pair.second as List<*>)
    }
    else {
        isRightOrderLists(
            (if(pair.first is Int) listOf(pair.first) else pair.first as List<*>) to
            (if(pair.second is Int) listOf(pair.second) else pair.second as List<*>)
        )
    }//.apply { println("eval $pair | $this") }

fun isRightOrderInts(pair: Pair<Int, Int>): Result =
    if (pair.first < pair.second) Result.ORDERED
    else if (pair.first > pair.second) Result.UNORDERED
    else Result.INCONCLUSIVE

fun isRightOrderLists(pair: Pair<List<*>, List<*>>): Result =
    pair.first.zip(pair.second)//.apply { println("  checking $this") }
        .map { isRightOrder(it as Pair<Any, Any>) }
        .firstOrNull { it != Result.INCONCLUSIVE}?:(
            if(pair.first.size > pair.second.size) Result.UNORDERED//.apply { println("   no match!") }
            else if(pair.first.size < pair.second.size) Result.ORDERED//.apply { println("   no match!") }
            else Result.INCONCLUSIVE)//.apply { println("   match!") })

enum class Result { ORDERED, UNORDERED, INCONCLUSIVE }