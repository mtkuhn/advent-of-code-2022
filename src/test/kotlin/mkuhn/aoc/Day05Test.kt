package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import readTestInput

internal class Day05Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day05_test")
        assertEquals("CMZ", day05part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day05_test")
        assertEquals("MCD", day05part2(testInput))
    }
}