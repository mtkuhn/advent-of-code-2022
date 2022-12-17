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

    return findOptimalPathVal(mapOf(0 to you), valves, mins)
}

fun day16part2(input: List<String>): Int {
    val valves = input.map { Valve.fromString(it) }
    val mins = 26

    val init = valves.find { it.name == "AA" }!!
    val you = ValveWalker(init, 0)
    val elephant = ValveWalker(init, 0)

    return findOptimalPathVal(mapOf(0 to you, 1 to elephant), valves, mins)
}

fun findOptimalPathVal(workers: Map<Int, ValveWalker>,
                    valves: List<Valve>,
                    mins: Int): Int {
    val distMap = valves.toDistanceMap()
    var valvePaths = listOf(ValvePath(
        workers,
        valves.filter { it.flowRate > 0 }, //the 0 flow valves may as well be closed, and the stuck AA valve
        0,
        mins,
        distMap
    ))

    //todo: A* this. heur is pressRelieved + forAllUnclosed(flow*(maxMinsRemaining))

    var i = 0
    var workingBest: Int = 0
    while(valvePaths.any { vp -> vp.walkers.all { w -> w.value.minsUsed < mins } }) {
        i++

        valvePaths = valvePaths.flatMap { v -> v.viableMoves() }
        var split = valvePaths.groupBy { p -> p.walkers.all { w -> w.value.minsUsed == mins} }
        val thisBest = split[true]?.maxOfOrNull { it.pressureRelieved }?:0
        if(thisBest > workingBest) workingBest = thisBest
        valvePaths = split[false]?:emptyList()

        println("$i | pathnum: ${valvePaths.size} | $workingBest}")
    }

    return workingBest
}

fun List<Valve>.toDistanceMap(): Map<Pair<Valve, Valve>, Int> {
    return this.flatMap { v1 ->
        this.map { v2 -> v1 to v2 }
    }.associateWith { this.distanceBetween(it.first, it.second) }
}

data class ValveWalker(val currPos: Valve, val minsUsed: Int)

data class ValvePath(val walkers: Map<Int, ValveWalker>,
                     val remainingValves: List<Valve>,
                     val pressureRelieved: Int,
                     val minsAllowed: Int,
                     val distMap:  Map<Pair<Valve, Valve>, Int>) {

    //move to and open the valve that presents the highest reward (factoring in cost to reach it)
    //return multiple in the event of a tie
    fun viableMoves(): List<ValvePath> {
        val wId = walkers.minBy { it.value.minsUsed }.key
        return if(walkers[wId]!!.minsUsed == minsAllowed) listOf(this)
        else {
            val moves = remainingValves.map { rv -> walkers[wId]!!.currPos to rv }
                .map { rv -> moveTo(wId, rv.second, distMap[rv]!!) } //map out to these destinations
                .filter { it.walkers[wId]!!.minsUsed <= minsAllowed } //don't go over allowed mins

            moves.ifEmpty {
                //this means there's nothing else helpful to move to, just set workers to 30 mins to close the loop
                listOf(this.copy(walkers = walkers.map { it.key to it.value.copy(minsUsed = minsAllowed) }.toMap()))
            }
        }
    }

    private fun moveTo(walkerId: Int, newValve: Valve, distance: Int): ValvePath {
        val mm = walkers[walkerId]!!.minsUsed+distance+1
        val newPressure = newValve.flowRate*(minsAllowed-mm)
        val newWalkers = walkers.toMutableMap().apply { this[walkerId] =
            ValveWalker(newValve, mm)
        }

        return ValvePath(
            newWalkers,
            remainingValves-newValve,
            pressureRelieved + newPressure,
            minsAllowed,
            distMap
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