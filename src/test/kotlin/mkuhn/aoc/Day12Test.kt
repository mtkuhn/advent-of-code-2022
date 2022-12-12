package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day12Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day12_test")
        assertEquals(31, day12part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day12_test")
        assertEquals(29, day12part2(testInput))
    }
}