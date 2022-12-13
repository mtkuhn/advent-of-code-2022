package mkuhn.aoc

import mkuhn.aoc.util.readInput
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
        assertEquals(140, day13part2(testInput))
    }

    @Test
    fun testPart1r() {
        val testInput = readInput("Day13")
        assertEquals(6235, day13part1(testInput))
    }

    @Test
    fun testPart2r() {
        val testInput = readInput("Day13")
        assertEquals(22866, day13part2(testInput))
    }
}