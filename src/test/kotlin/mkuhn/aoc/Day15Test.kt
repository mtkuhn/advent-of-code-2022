package mkuhn.aoc

import mkuhn.aoc.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day15Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day15_test")
        assertEquals(26, day15part1(testInput, 10))
    }

    @Test
    fun testPart1r() {
        val testInput = readInput("Day15")
        assertEquals(5256611, day15part1(testInput, 2000000))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day15_test")
        assertEquals(56000011, day15part2(testInput, 0..20, 0..20))
    }
}