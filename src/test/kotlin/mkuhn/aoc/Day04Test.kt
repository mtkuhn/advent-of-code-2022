package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import readTestInput

internal class Day04Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day04_test")
        assertEquals(2, day4part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day04_test")
        assertEquals(4, day4part2(testInput))
    }
}