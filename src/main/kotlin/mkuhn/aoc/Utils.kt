import java.io.File

fun readInput(name: String) = File("src/main/resources/", "$name.txt")
    .readLines()

fun readTestInput(name: String) = File("src/test/resources/", "$name.txt")
    .readLines()

fun List<String>.splitList(separator: String): List<List<String>> =
    this.fold(mutableListOf(mutableListOf<String>())) { acc, a ->
        if (a == separator) acc += mutableListOf<String>()
        else acc.last() += a
        acc
    }

fun <T> Collection<Collection<T>>.intersectAll(): Set<T> =
    this.fold(this.first().toSet()) { acc, e -> acc intersect e.toSet() }

fun <T> List<List<T>>.transpose(filterCondition: (T) -> Boolean = { true }): MutableList<MutableList<T>> =
    mutableListOf<MutableList<T>>().apply {
        repeat(this@transpose.first().size) { this += mutableListOf<T>() }
        this@transpose.forEach { r ->
            r.forEachIndexed { i, c ->
                if(filterCondition(c)) { this[i] += c }
            }
        }
    }

inline fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (!predicate(item))
            break
    }
    return list
}
