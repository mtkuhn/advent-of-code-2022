package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day11Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day11_test")
        assertEquals(10605L, day11part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day11_test")
        assertEquals(2713310158L, day11part2(testInput))
    }
}