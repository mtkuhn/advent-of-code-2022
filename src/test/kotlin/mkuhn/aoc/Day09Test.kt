package mkuhn.aoc

import mkuhn.aoc.util.readTestInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day09Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day09_test")
        assertEquals(13, day09part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day09_test")
        assertEquals(1, day09part2(testInput))
    }

    @Test
    fun testPart2b() {
        val testInput = readTestInput("Day09_testb")
        assertEquals(36, day09part2(testInput))
    }
}