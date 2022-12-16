package mkuhn.aoc

import mkuhn.aoc.util.readInput

const val MINS = 30

fun main() {
    val input = readInput("Day16")
    println(day16part1(input))
    println(day16part2(input))
}

fun day16part1(input: List<String>): Int {
    val valves = input.map { Valve.fromString(it) }

    valves.filter { it.flowRate > 0 }.forEach { println(it) }

    //cache of distance values between all nodes
    val distMap = valves.flatMap { v1 -> valves.map { v2 -> v1 to v2 } }
        .associateWith { valves.distanceBetween(it.first, it.second) }

    val init = valves.find { it.name == "AA" }!!

    var valvePaths = listOf(ValvePath(
        init,
        valves.filter { it.flowRate == 0 } + init, //the 0 flow valves may as well be closed, and the stuck AA valve
        0,
        0,
        emptyList()))
    var i = 0 //for debugging
    while(valvePaths.any { it.minsUsed < MINS }) {
        i++
        valvePaths = valvePaths.flatMap { v ->
            v.viableMoves(distMap)
        }
        //println("$i | pathnum: ${valvePaths.size} | ${valvePaths.maxOf { it.pressureRelieved }}")
    }

    println(valvePaths.maxBy { it.pressureRelieved }.let { it.history + it.curr.name })

    return valvePaths.maxOf { it.pressureRelieved }
}

fun day16part2(input: List<String>): Int =
    2

data class ValvePath(val curr: Valve,
                     val closedValves: List<Valve>,
                     val minsUsed: Int,
                     val pressureRelieved: Int,
                     val history: List<String>) {

    //move to and open the valve that presents the highest reward (factoring in cost to reach it)
    //return multiple in the event of a tie
    fun viableMoves(distMap:  Map<Pair<Valve, Valve>, Int>): List<ValvePath> =
        if(this.minsUsed == MINS) listOf(this)
        else {
            var moves = distMap.filter { it.key.first == curr }
                .filter { it.key.second !in closedValves }
                .map { distMapEntry -> this.moveTo(distMapEntry.key.second, distMapEntry.value) }
                .filter { it.minsUsed <= 30 }
                .filter { it.pressureRelieved > this.pressureRelieved }

            //todo: how else can we filter this?
            //is the distance to the furthest node (with a flow) further than remaining minutes?

            moves.ifEmpty {
                //this means there's nothing else helpful to move to, just use up the minutes
                listOf(this.copy(minsUsed = MINS))
            }
        }

    fun moveTo(newValve: Valve, distance: Int): ValvePath {
        val minsUsed = minsUsed+distance+1
        val newPressure = pressureRelieved + (newValve.flowRate*(MINS-minsUsed))
        return ValvePath(
            newValve,
            closedValves+newValve,
            minsUsed,
            newPressure,
            this.history + this.curr.name
        )
    }
}

data class Valve(val name: String, val flowRate: Int, val paths: List<String>) {
    companion object {
        private val valveRegex = """Valve (.+) has flow rate=(\d+); tunnels? leads? to valves? (.+)""".toRegex()

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