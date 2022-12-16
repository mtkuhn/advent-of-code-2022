package mkuhn.aoc

import mkuhn.aoc.util.readInput
import kotlin.math.min

fun main() {
    val input = readInput("Day16")
    println(day16part1(input))
    println(day16part2(input))

    //11 | num: 75801 | best: 1356
}

fun day16part1(input: List<String>): Int {
    val valves = input.map { Valve.fromString(it) }
    val initOpenState = valves.associateWith { false }
    var valvePaths = listOf(ValvePath(valves.first(), initOpenState))

    //todo: create distance map
    val distMap = valves.flatMap { v1 -> valves.map { v2 -> v1 to v2 } }
        //.filter { p -> p.first.name.compareTo(p.second.name) < -1 } } //alphbetical order only
        .associateWith { valves.distanceBetween(it.first, it.second) }

    (0..30).forEach { i ->
        valvePaths = valvePaths
            .flatMap { it.expandChoices() }
            .groupBy { it.curr to it.valvesOpen }.map { m -> m.value.maxBy { it.pressureRelieved } } //an easy performance improvement is to kill off nodes that are objectively worse

        val best = valvePaths.maxBy { it.pressureRelieved }

        valvePaths = valvePaths.filter { it.maxReliefStillPossible(distMap) >= best.pressureRelieved } //attempt to remove those that can never catch up

        println("$i | num: ${valvePaths.size} | best: ${best.pressureRelieved}")
    }

    return valvePaths.maxOf { it.pressureRelieved }
}

fun day16part2(input: List<String>): Int =
    2

data class ValvePath(val curr: Valve,
                     val valvesOpen: Map<Valve, Boolean>,
                     val minsUsed: Int = 0,
                     val pressureRelieved: Int = 0) {

    fun expandChoices(): List<ValvePath> {
        val movements = curr.paths.map { p ->
            valvesOpen.entries
                .first { m -> m.key.name == p }.key
        }.map { v -> moveTo(v)}.toMutableList()

        if(!valvesOpen[curr]!!) { movements += openThisValve() }
        return movements
    }

    fun moveTo(v: Valve): ValvePath =
        ValvePath(
            v,
            valvesOpen.toMap(),
            minsUsed+1,
            pressureRelieved
        )

    fun openThisValve(): ValvePath =
        ValvePath(
            curr,
            valvesOpen.toMutableMap().apply { this[curr] = true },
            minsUsed+1,
            pressureRelieved + (curr.flowRate * (29-minsUsed))
        )

    fun maxReliefStillPossible(distMap:  Map<Pair<Valve, Valve>, Int>): Int =
        pressureRelieved + valvesOpen.filter { !it.value }.keys
            .filter { (30 - minsUsed) > (distMap[this.curr to it] ?: 0) }
            .sumOf { it.flowRate * (30 - minsUsed - (distMap[this.curr to it]?:0)) }
}

data class Valve(val name: String, val flowRate: Int, val paths: List<String>) {
    companion object {
        val valveRegex = """Valve (.+) has flow rate=(\d+); tunnels? leads? to valves? (.+)""".toRegex()

        fun fromString(s: String): Valve =
            valveRegex.matchEntire(s)?.destructured?.let {
                    (n, r, p) -> Valve(n, r.toInt(), p.split(", "))
            }?: error("invalid input: $s")
    }
}

fun List<Valve>.distanceBetween(a: Valve, b: Valve): Int {
    var nodes = listOf(a)
    val nodesVisited = mutableSetOf(a)
    var dist = 0
    while(b !in nodes) {
        nodes = nodes.flatMap { curr ->
            curr.paths.map { n -> this.find { it.name == n }!! }
                .filter { it !in nodesVisited }
        }
        nodesVisited += nodes
        dist++
    }
    return dist
}