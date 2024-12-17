import Utils.Coord

data class RecursiveCoord(
    val coord: Coord,
    val depth: Int,
)

fun main() {
    val grid = Utils.readAsGrid("inputs/input20.txt", null) { it }
    Utils.printGrid(grid)
    val coordToKey = mutableMapOf<Coord, String>()
    grid.entries.forEach { entry ->
        if (entry.value == '.') {
            entry.key.manhattanNeighbors.forEach { neighCoord ->
                if (grid[neighCoord]?.isUpperCase() == true) {
                    val secondMarkerCoord = neighCoord + (neighCoord - entry.key)

                    val word = if (secondMarkerCoord.x == neighCoord.x) {
                        listOf(neighCoord, secondMarkerCoord).sortedBy { it.y }.map { grid[it]!! }.joinToString("")
                    } else {
                        listOf(neighCoord, secondMarkerCoord).sortedBy { it.x }.map { grid[it]!! }.joinToString("")
                    }

                    coordToKey[entry.key] = word
                }
            }
        }
    }
    println("coordToKey: $coordToKey")

    val teleportMap = mutableMapOf<Coord, Coord>()
    coordToKey.entries.forEach { e1 ->
        coordToKey.entries.forEach { e2 ->
            if (e1 != e2 && e1.value == e2.value) {
                teleportMap[e1.key] = e2.key
            }
        }
    }
    println("teleportMap: $teleportMap")

    val start = coordToKey.filter { it.value == "AA" }.keys.first()
    val end = coordToKey.filter { it.value == "ZZ" }.keys.first()
    println("Start: $start")
    println("End: $end")
    var frontier = mutableSetOf(start)
    var iterations = 0
    val seen = mutableSetOf<Coord>()
    bfsA@ while (frontier.isNotEmpty()) {
        val newFronter = mutableSetOf<Coord>()
        iterations++
        for (element in frontier) {
            val neighbors = element.manhattanNeighbors.toMutableSet().apply {
                teleportMap[element]?.let {
                    add(it)
                }
            }
            neighbors.forEach { neighbor ->
                if (neighbor !in seen && grid[neighbor] == '.') {
                    newFronter.add(neighbor)
                    seen.add(neighbor)
                    if (neighbor == end) {
                        println("Part A: $iterations")
                        break@bfsA
                    }
                }
            }
        }
        frontier = newFronter
    }

    val maxX = grid.keys.maxOf { it.x }
    val maxY = grid.keys.maxOf { it.y }
    val outer = teleportMap.filterKeys {
        it.x == 2 || it.y == 2 || it.x == maxX - 2 || it.y == maxY - 2
    }
    val inner = teleportMap.filterKeys { it !in outer }
    val startB = RecursiveCoord(coordToKey.filter { it.value == "AA" }.keys.first(), 0)
    val endB = RecursiveCoord(coordToKey.filter { it.value == "ZZ" }.keys.first(), 0)
    var frontierB = mutableSetOf(startB)
    var iterationsB = 0
    val seenB = mutableSetOf<RecursiveCoord>(startB)
    // We could potentially keep track of the lowest depth per coord, of course
    // that would not account for cases where we have to "build up" depth
    while (frontierB.isNotEmpty()) {
        val newFronterB = mutableSetOf<RecursiveCoord>()
        iterationsB++
        for (element in frontierB) {
            val neighbors = element.coord.manhattanNeighbors.map {
                RecursiveCoord(it, element.depth)
            }.toMutableSet().apply {
                // We could probably add a heuristic here to limit the max depth
                inner[element.coord]?.let {
                    add(RecursiveCoord(it, element.depth + 1))
                }
                if (element.depth != 0) {
                    outer[element.coord]?.let {
                        add(RecursiveCoord(it, element.depth - 1))
                    }
                }
            }

            neighbors.forEach { neighbor ->
                if (neighbor !in seenB && grid[neighbor.coord] == '.') {
                    newFronterB.add(neighbor)
                    seenB.add(neighbor)
                    if (neighbor == endB) {
                        println("Part B: $iterationsB")
                        return
                    }
                }
            }
        }
        frontierB = newFronterB
    }
}