package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.readTestInput

internal class Day07Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day07_test")
        assertEquals(95437, day07part1(testInput))
    }

    @Test
    fun testPart1b() {
        val testInput = readInput("Day07")
        assertEquals(1749646, day07part1(testInput))
    }


    @Test
    fun testPart2() {
        val testInput = readTestInput("Day07_test")
        assertEquals(24933642, day07part2(testInput))
    }
}