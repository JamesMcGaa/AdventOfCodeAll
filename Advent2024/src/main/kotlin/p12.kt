import Utils.Coord
import Utils.Direction
import java.io.File

val INPUT_12 = File("inputs/input12.txt").readLines()

fun main() {
    val grid = mutableMapOf<Coord, Char>()
    for (i in INPUT_12.indices) {
        for (j in INPUT_12[0].indices) {
            grid[Coord(i, j)] = INPUT_12[i][j]
        }
    }

    val islands = mutableSetOf<MutableSet<Coord>>()
    val seen = mutableSetOf<Coord>()
    for (i in INPUT_12.indices) {
        for (j in INPUT_12[0].indices) {
            val current = Coord(i, j)
            if (current !in seen) {
                val island = mutableSetOf<Coord>()
                floodFillIsland(current, grid[current]!!, seen, island, grid)
                islands.add(island)
            }
        }
    }

    val priceA = islands.sumOf { island -> perimeter(island) * area(island) }
    println("Part A: $priceA")

    val priceB = islands.sumOf { island -> sides(island) * area(island) }
    println("Part B: $priceB")
}

fun sides(island: MutableSet<Coord>): Int {
    val seen = mutableSetOf<Pair<Coord, Coord>>()
    var sideCount = 0
    for (spot in island) {
        Direction.entries.forEach {
            dir ->
            val neighbor = when (dir) {
                Direction.UP -> spot.copy(x = spot.x-1)
                Direction.DOWN -> spot.copy(x = spot.x+1)
                Direction.LEFT -> spot.copy(y = spot.y-1)
                Direction.RIGHT -> spot.copy(y = spot.y+1)
            }

            if (neighbor !in island && Pair(spot, neighbor) !in seen) {
                sideCount++
                fillAlongEdge(spot, dir, seen, island)
            }
        }
    }
    return sideCount
}

fun fillAlongEdge(spot: Coord, dir: Direction, seen: MutableSet<Pair<Coord, Coord>>, island: MutableSet<Coord>) {
    val neighbor = when (dir) {
        Direction.UP -> spot.copy(x = spot.x-1)
        Direction.DOWN -> spot.copy(x = spot.x+1)
        Direction.LEFT -> spot.copy(y = spot.y-1)
        Direction.RIGHT -> spot.copy(y = spot.y+1)
    }
    if (spot in island && neighbor !in island && Pair(spot, neighbor) !in seen) { // Same check as before but we have to verify our start is an island now too
        seen.add(Pair(spot, neighbor))
        when (dir) {
            Direction.UP, Direction.DOWN -> {
                fillAlongEdge(spot.copy(y = spot.y - 1), dir, seen, island)
                fillAlongEdge(spot.copy(y = spot.y+ 1), dir, seen, island)
            }
            Direction.RIGHT, Direction.LEFT -> {
                fillAlongEdge(spot.copy(x = spot.x - 1), dir, seen, island)
                fillAlongEdge(spot.copy(x = spot.x + 1), dir, seen, island)
            }
        }
    } else {
        return
    }
}

fun perimeter(island: MutableSet<Coord>): Int {
    var touches = 0
    for (spot in island) {
        if (spot.copy(x = spot.x - 1) in island) touches++
        if (spot.copy(x = spot.x + 1) in island) touches++
        if (spot.copy(y = spot.y - 1) in island) touches++
        if (spot.copy(y = spot.y + 1) in island) touches++
    }
    return island.size * 4 - touches
}

fun area(island: MutableSet<Coord>): Int {
    return island.size
}

fun floodFillIsland(current: Coord, type: Char, seen: MutableSet<Coord>, island: MutableSet<Coord>, grid: Map<Coord, Char>) {
    if (current in seen || !grid.contains(current) || grid[current] != type) {
        return
    }
    seen.add(current)
    island.add(current)
    floodFillIsland(current.copy(x = current.x - 1), type, seen, island, grid)
    floodFillIsland(current.copy(x = current.x + 1), type, seen, island, grid)
    floodFillIsland(current.copy(y = current.y - 1), type, seen, island, grid)
    floodFillIsland(current.copy(y = current.y + 1), type, seen, island, grid)
}