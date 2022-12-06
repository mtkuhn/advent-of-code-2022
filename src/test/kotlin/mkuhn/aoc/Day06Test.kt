package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day06Test {

    @Test
    fun testPart1() {
        assertEquals(7, day06part1("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
        assertEquals(5, day06part1("bvwbjplbgvbhsrlpgdmjqwftvncz"))
        assertEquals(6, day06part1("nppdvjthqldpwncqszvftbrmjlhg"))
    }

    @Test
    fun testPart2() {
        assertEquals(19, day06part2("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
    }
}