package mkuhn.aoc

import intersectAll
import org.junit.jupiter.api.Test
import splitList
import kotlin.test.assertEquals

internal class UtilsTest {

    @Test
    fun splitListTest() {
        val list = listOf("a", "b", "1", "c", "1", "d", "e")
        val splitList = list.splitList("1")

        check(splitList.size == 3)
        check(splitList.elementAt(0) == listOf("a", "b"))
        check(splitList.elementAt(1) == listOf("c"))
        check(splitList.elementAt(2) == listOf("d", "e"))
    }

    @Test
    fun intersectAllTest() {
        val lists = listOf<Set<Char>>("abc".toSet(), "ade".toSet())
        assertEquals('a', lists.intersectAll().first())
    }

}