package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day13Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day13_test")
        assertEquals(13, day13part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day13_test")
        assertEquals(0, day13part2(testInput))
    }
}