package mkuhn.aoc

import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.splitToPair

fun main() {
    val input = readInput("Day07")
    println(day07part1(input))
    println(day07part2(input))
}

fun day07part1(input: List<String>): Int =
    FileStructure().apply {
        expandByCommands(input.associateCommandsToOutput().drop(1))
    }.dirList.map { it.tallySize() }
        .filter { it <= 100000 }
        .sum()


fun day07part2(input: List<String>): Int =
    FileStructure().apply {
        expandByCommands(input.associateCommandsToOutput().drop(1))
    }.let { structure ->
        structure.dirList.map { it.tallySize() }
        .filter { it >= 30000000 - (70000000 - structure.root.tallySize()) }
        .min()
    }

fun List<String>.associateCommandsToOutput(): List<Pair<String, List<String>>> =
    this.fold(mutableListOf<Pair<String, MutableList<String>>>()) { acc, a ->
        if (a.startsWith("$")) acc += a.drop(2) to mutableListOf<String>()
        else acc.last().second += a
        acc
    }

class FileStructure(val root: FileNode = FileNode("/", 0, true, null),
                    val dirList: MutableList<FileNode> = mutableListOf(root)) {
    fun expandByCommands(commands: List<Pair<String, List<String>>>) {
        var currentNode = root
        commands.forEach { command ->
            when {
                command.first == "ls" -> command.second.forEach {
                    currentNode.addFromLsResult(it).apply { if(this.isDirectory) dirList += this }
                }
                command.first.startsWith("cd") ->
                    currentNode = when(command.first.splitToPair(' ').second) {
                        "/" -> root
                        ".." -> currentNode.parent!!
                        else -> currentNode.children.first { it.name == command.first.splitToPair(' ').second }
                    }
            }
        }
    }
}

data class FileNode(val name: String, val size: Int, val isDirectory: Boolean,
                    val parent: FileNode?, val children: MutableList<FileNode> = mutableListOf(), var totalSize: Int? = null) {
    fun addFromLsResult(lsResult: String): FileNode =
        lsResult.splitToPair(' ')
            .let { result ->
                when(result.first) {
                    "dir" -> FileNode(result.second, 0, true, this)
                    else -> FileNode(result.second, result.first.toInt(), false, this)
                }
            }.apply { this@FileNode.children += this }

    fun tallySize(): Int = totalSize?:(this.size + children.sumOf { it.tallySize() }).apply { totalSize = this }
}