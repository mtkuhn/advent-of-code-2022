package mkuhn.aoc

import mkuhn.aoc.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day16Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day16_test")
        assertEquals(1651, day16part1(testInput))
    }

    @Test
    fun testPart1r() {
        val testInput = readInput("Day16")
        assertEquals(1792, day16part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day16_test")
        assertEquals(1707, day16part2(testInput))
    }

    @Test
    fun testPart2r() {
        val testInput = readInput("Day16")
        assertEquals(0, day16part2(testInput))
    }
}