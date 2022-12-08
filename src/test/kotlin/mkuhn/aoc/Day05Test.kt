package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.readTestInput

internal class Day05Test {

    @Test
    fun testPart1Sample() {
        val testInput = readTestInput("Day05_test")
        assertEquals("CMZ", day05part1(testInput))
    }

    @Test
    fun testPart2Sample() {
        val testInput = readTestInput("Day05_test")
        assertEquals("MCD", day05part2(testInput))
    }

    @Test
    fun testPart1() {
        val testInput = readInput("Day05")
        assertEquals("FCVRLMVQP", day05part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readInput("Day05")
        assertEquals("RWLWGJGFD", day05part2(testInput))
    }
}