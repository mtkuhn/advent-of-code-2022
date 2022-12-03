package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import readTestInput

internal class Day03Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day03_test")
        assertEquals(157, day3part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day03_test")
        assertEquals(70, day3part2(testInput))
    }

    @Test
    fun testRucksackPriority() {
        assertEquals(1, 'a'.toRucksackPriority())
        assertEquals(2, 'b'.toRucksackPriority())
        assertEquals(26, 'z'.toRucksackPriority())
        assertEquals(27, 'A'.toRucksackPriority())
        assertEquals(28, 'B'.toRucksackPriority())
        assertEquals(52, 'Z'.toRucksackPriority())
    }
}