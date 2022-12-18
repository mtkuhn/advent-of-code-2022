package mkuhn.aoc

import mkuhn.aoc.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day18Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day18_test")
        assertEquals(64, day18part1(testInput))
    }

    @Test
    fun testPart1r() {
        val testInput = readInput("Day18")
        assertEquals(3470, day18part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day18_test")
        assertEquals(58, day18part2(testInput))
    }

    @Test
    fun testPart2r() {
        val testInput = readInput("Day18")
        assertEquals(3470, day18part2(testInput))
        //3260 too high
    }
}