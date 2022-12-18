package mkuhn.aoc

fun day16part1(input: List<String>): Int {
    val valves = input.map { Valve.fromString(it) }
    val mins = 30

    val init = valves.find { it.name == "AA" }!!
    val you = ValveWalker(init, 0)

    val finder = ValvePathFinder(mins, valves)
    return finder.findOptimalPathValDFS(listOf(you), valves)
}

fun day16part2(input: List<String>): Int {
    val valves = input.map { Valve.fromString(it) }
    val mins = 30

    val init = valves.find { it.name == "AA" }!!
    val you = ValveWalker(init, 4)
    val elephant = ValveWalker(init, 4)

    val finder = ValvePathFinder(mins, valves)
    return finder.findOptimalPathValDFS(listOf(you, elephant), valves)
}


class ValvePathFinder(private val minsAllowed: Int, valves: List<Valve>) {

    private val distMap:  Map<Pair<Valve, Valve>, Int> = valves.toDistanceMap()

    fun findOptimalPathValDFS(workers: List<ValveWalker>,
                              valves: List<Valve>): Int {
        val valvePath = ValvePath(
            workers,
            valves.filter { it.flowRate > 0 }, //the 0 flow valves may as well be closed, and the stuck AA valve
            0
        )

        return valvePath.findBestPathScore()
    }

    private fun ValvePath.findBestPathScore(): Int {
        val paths = this.viableMoves()
        return if(paths.isEmpty()) 0
        else if(paths.all { p -> p.walkers.all { it.minsUsed == minsAllowed } }) paths.maxOf { it.pressureRelieved }
        else paths.maxOf { it.findBestPathScore() }
    }

    private fun List<Valve>.toDistanceMap(): Map<Pair<Valve, Valve>, Int> {
        return this.flatMap { v1 ->
            this.map { v2 -> v1 to v2 }
        }.associateWith { this.distanceBetween(it.first, it.second) }
    }

    private fun ValvePath.viableMoves(): List<ValvePath> {
        val walker = walkers.first()
        return if(walker.minsUsed == minsAllowed) listOf(this)
        else {
            val moves = remainingValves.map { rv -> walker.currPos to rv }
                .map { rv -> moveTo(walker, rv.second, distMap[rv]!!) } //map out to these destinations
                .plus(this.copy(walkers = (walkers-walker+ValveWalker(walker.currPos, minsAllowed)).sortedBy { it.minsUsed })) //or maybe just do nothing?
                .filter { it.walkers.all { w -> w.minsUsed <= minsAllowed } }//don't go over allowed mins

            moves.ifEmpty {
                //this means there's nothing else helpful to move to, just set workers to 30 mins to close the loop
                listOf(this.copy(walkers = walkers.map { ValveWalker(it.currPos, minsAllowed) }))
            }
        }
    }

    private fun ValvePath.moveTo(walker: ValveWalker, newValve: Valve, distance: Int): ValvePath {
        val mm = walker.minsUsed+distance+1
        val newPressure = newValve.flowRate*(minsAllowed-mm)
        val newWalkers = (walkers - walker + ValveWalker(newValve, mm)).sortedBy { it.minsUsed }
        return ValvePath(
            newWalkers,
            remainingValves-newValve,
            pressureRelieved + newPressure
        )
    }

    private fun List<Valve>.distanceBetween(a: Valve, b: Valve): Int {
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
}

data class ValveWalker(val currPos: Valve, val minsUsed: Int)
data class ValvePath(val walkers: List<ValveWalker>, val remainingValves: List<Valve>, val pressureRelieved: Int)

data class Valve(val name: String, val flowRate: Int, val paths: List<String>) {
    companion object {
        private val valveRegex = """Valve (.+) has flow rate=(\d+); tunnels? leads? to valves? (.+)""".toRegex()

        fun fromString(s: String): Valve =
            valveRegex.matchEntire(s)?.destructured?.let {
                    (n, r, p) -> Valve(n, r.toInt(), p.split(", "))
            }?: error("invalid input: $s")
    }
}