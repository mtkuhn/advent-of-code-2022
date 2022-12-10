package mkuhn.aoc

import mkuhn.aoc.util.readTestInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day10_test")
        assertEquals(13140, day10part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day10_test")
        val expected = """
            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######.....
        """.trimIndent()
        assertEquals(expected, day10part2(testInput).apply { println(this) })
    }
}