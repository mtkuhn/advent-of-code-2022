package mkuhn.aoc

import readInput

fun main() {
    val input = readInput("Day07")
    println(day07part1(input))
    println(day07part2(input))
}

fun day07part1(input: List<String>): Int {
    val commands = input.associateCommandsToOutput()
    val fileMap = FileNode("/", 0, true, null)
    val dirList = mutableListOf<FileNode>(fileMap)
    var currentNode = fileMap
    commands.drop(1).forEach { command ->
        when {
            command.first == "ls" -> command.second.forEach {
                currentNode.addFromLsResult(it).apply { if(this.isDirectory) dirList += this }
            }
            command.first.startsWith("cd") -> currentNode =
                when(command.first.splitToPair(' ').second) {
                    "/" -> fileMap
                    ".." -> currentNode.parent!!
                    else -> currentNode.children.first { it.name == command.first.splitToPair(' ').second }
                }
        }
    }

    return dirList.map { it.tallySize() }
        .filter { it <= 100000 }
        .sum()
}


fun day07part2(input: List<String>): Int {
    val commands = input.associateCommandsToOutput()
    val fileMap = FileNode("/", 0, true, null)
    val dirList = mutableListOf<FileNode>(fileMap)
    var currentNode = fileMap
    commands.drop(1).forEach { command ->
        when {
            command.first == "ls" -> command.second.forEach {
                currentNode.addFromLsResult(it).apply { if(this.isDirectory) dirList += this }
            }
            command.first.startsWith("cd") -> currentNode =
                when(command.first.splitToPair(' ').second) {
                    "/" -> fileMap
                    ".." -> currentNode.parent!!
                    else -> currentNode.children.first { it.name == command.first.splitToPair(' ').second }
                }
        }
    }

    val totals = dirList.map { it.name to it.tallySize() }.toMap()
    val spaceToFree = 30000000-(70000000-totals["/"]!!)
    return totals.values.filter { it >= spaceToFree }.min()
}

fun List<String>.associateCommandsToOutput(): List<Pair<String, List<String>>> =
    this.fold(mutableListOf<Pair<String, MutableList<String>>>()) { acc, a ->
        if (a.startsWith("$")) acc += a.drop(2) to mutableListOf<String>()
        else acc.last().second += a
        acc
    }

fun String.splitToPair(separator: Char) = this.substringBefore(separator) to this.substringAfter(separator)

data class FileNode(val name: String, val size: Int, val isDirectory: Boolean,
                    val parent: FileNode?, val children: MutableList<FileNode> = mutableListOf()) {
    fun addFromLsResult(lsResult: String): FileNode =
        lsResult.splitToPair(' ')
            .let { result ->
                when(result.first) {
                    "dir" -> FileNode(result.second, 0, true, this)
                    else -> FileNode(result.second, result.first.toInt(), false, this)
                }
            }.apply { this@FileNode.children += this }

    fun tallySize(): Int = (this.size + children.sumOf { it.tallySize() }).apply { "$name size=$this" }
}