package mkuhn.aoc

import org.junit.jupiter.api.Test
import readTestInput
import splitList

internal class Day01Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day01_test").splitList("")
        check(day1part1(testInput) == 24000)
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day01_test").splitList("")
        check(day1part2(testInput) == 45000)
    }
}