import Utils.Coord
import kotlin.math.max


fun main() {
    val coords = mutableSetOf<Coord>()
    for (i in 1..300) {
        for (j in 1..300) {
            coords.add(Coord(i, j))
        }
    }

    val grid = coords.associate { coord -> Pair(coord, powerLevel(coord)) }

    val maxEntryA = grid.maxBy { entry ->
        if (entry.key.x in 2..299 && entry.key.y in 2..299) {
            (setOf(entry.key) + entry.key.fullNeighbors).sumOf { grid[it]!! }
        } else {
            0
        }
    }
    println("Part A: ${maxEntryA.key.x - 1},${maxEntryA.key.y - 1}")

    val coordToMaxPower = mutableMapOf<Pair<Coord, Int>, Long>()
    // For each coord
    grid.keys.forEach { coord ->
        coordToMaxPower[Pair(coord, 1)] = grid[coord]!!
        val diameter = 300 - max(coord.x, coord.y) + 1
        // For each grid size
        for (d in 2..diameter) {
            var delta = 0L
            for (i in coord.x..coord.x + (d - 1)) {
                delta += grid[Coord(i, coord.y + (d - 1))]!!
            }
            for (j in coord.y..coord.y + (d - 1)) {
                delta += grid[Coord(coord.x + (d - 1), j)]!!
            }
            delta -= grid[Coord(coord.x + (d - 1), coord.y + (d - 1))]!!
            coordToMaxPower[Pair(coord, d)] = coordToMaxPower[Pair(coord, d - 1)]!! + delta
        }
    }
    val maxEntryB = coordToMaxPower.maxBy { it.value }.key
    println("Part B: ${maxEntryB.first.x},${maxEntryB.first.y},${maxEntryB.second}")
}

fun powerLevel(coord: Coord): Long {
    val serialNumber = 1308L
    val rackID = coord.x + 10L
    var powerLevel = rackID * coord.y
    powerLevel += serialNumber
    powerLevel *= rackID
    powerLevel = (powerLevel / 100) % 10
    powerLevel -= 5
    return powerLevel
}