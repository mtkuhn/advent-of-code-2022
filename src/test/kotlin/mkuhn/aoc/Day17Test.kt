package mkuhn.aoc

import mkuhn.aoc.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day17Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day17_test")
        assertEquals(3068, day17part1(testInput))
    }

    @Test
    fun testPart1r() {
        val testInput = readInput("Day17")
        assertEquals(3181, day17part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day17_test")
        assertEquals(1514285714288, day17part2(testInput))
    }
}