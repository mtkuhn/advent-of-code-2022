package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day14Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day14_test")
        assertEquals(24, day14part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day14_test")
        assertEquals(93, day14part2(testInput))
    }
}