package mkuhn.aoc

import mkuhn.aoc.util.readInput


fun main() {
    val input = readInput("Day16")
    //println(day16part1(input))
    println(day16part2(input))
}


fun day16part1(input: List<String>): Int {
    val valves = input.map { Valve.fromString(it) }
    val mins = 30

    val init = valves.find { it.name == "AA" }!!
    val you = ValveWalker(init, 0)

    return findOptimalPath(mapOf(0 to you), valves, mins, init).pressureRelieved
}

fun day16part2(input: List<String>): Int {
    val valves = input.map { Valve.fromString(it) }
    val mins = 26

    val init = valves.find { it.name == "AA" }!!
    val you = ValveWalker(init, 0)
    val elephant = ValveWalker(init, 0)

    return findOptimalPath(mapOf(0 to you, 1 to elephant), valves, mins, init).pressureRelieved
}

fun findOptimalPath(workers: Map<Int, ValveWalker>,
                    valves: List<Valve>,
                    mins: Int, initialValve: Valve): DoubleValvePath {
    val distMap = valves.toDistanceMap()
    var valvePaths = listOf(DoubleValvePath(
        workers,
        valves.filter { it.flowRate == 0 } + initialValve, //the 0 flow valves may as well be closed, and the stuck AA valve
        0,
        mins))

    var i = 0
    while(valvePaths.any { vp -> vp.walkers.all { w -> w.value.minsUsed < mins } }) {
        i++
        valvePaths = valvePaths.flatMap { v ->
            v.viableMoves(distMap)
        }
        println("$i | pathnum: ${valvePaths.size} | ${valvePaths.maxOf { it.pressureRelieved }}")
    }

    return valvePaths.maxBy { it.pressureRelieved }
}

fun List<Valve>.toDistanceMap(): Map<Pair<Valve, Valve>, Int> {
    return this.flatMap { v1 ->
        this.map { v2 -> v1 to v2 }
    }.associateWith { this.distanceBetween(it.first, it.second) }
}

data class ValveWalker(val currPos: Valve, val minsUsed: Int)

data class DoubleValvePath(val walkers: Map<Int, ValveWalker>,
                           val closedValves: List<Valve>,
                           val pressureRelieved: Int,
                           val minsAllowed: Int) {

    //move to and open the valve that presents the highest reward (factoring in cost to reach it)
    //return multiple in the event of a tie
    fun viableMoves(distMap:  Map<Pair<Valve, Valve>, Int>): List<DoubleValvePath> {
        val wId = walkers.minBy { it.value.minsUsed }.key
        return if(walkers[wId]!!.minsUsed == minsAllowed) listOf(this)
        else {
            var moves = distMap.filter { distEntry -> distEntry.key.first == walkers[wId]!!.currPos }
                .filter { distEntry -> distEntry.key.second !in closedValves }
                .map { distEntry -> moveTo(wId, distEntry.key.second, distEntry.value) }
                .filter { it.walkers[wId]!!.minsUsed <= minsAllowed }
                .filter { it.pressureRelieved > this.pressureRelieved }

            moves.ifEmpty {
                //this means there's nothing else helpful to move to, just set to 30 mins
                listOf(this.copy(walkers = walkers.map { it.key to it.value.copy(minsUsed = minsAllowed) }.toMap()))
            }
        }
    }

    fun moveTo(walkerId: Int, newValve: Valve, distance: Int): DoubleValvePath {
        val mm = walkers[walkerId]!!.minsUsed+distance+1
        val newPressure = newValve.flowRate*(minsAllowed-mm)
        val newWalkers = walkers.toMutableMap().apply { this[walkerId] =
            ValveWalker(newValve, mm)
        }

        return DoubleValvePath(
            newWalkers,
            closedValves+newValve,
            pressureRelieved + newPressure,
            minsAllowed
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