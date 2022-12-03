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
