import Utils.Coord
import Utils.Direction

data class DirectionCoord(
    val coord: Coord,
    val dir: Direction
) {
    fun neighbors(): Set<DirectionCoord> {
        return when (dir) {
            Direction.UP -> setOf(
                copy(dir = Direction.LEFT),
                copy(dir = Direction.RIGHT),
                copy(coord = coord.copy(x = coord.x - 1)),
            )
            Direction.DOWN -> setOf(
                copy(dir = Direction.LEFT),
                copy(dir = Direction.RIGHT),
                copy(coord = coord.copy(x = coord.x + 1)),
            )
            Direction.RIGHT -> setOf(
                copy(dir = Direction.UP),
                copy(dir = Direction.DOWN),
                copy(coord = coord.copy(y = coord.y + 1)),
            )
            Direction.LEFT -> setOf(
                copy(dir = Direction.UP),
                copy(dir = Direction.DOWN),
                copy(coord = coord.copy(y = coord.y - 1)),
            )
        }
    }

    fun dist(other: DirectionCoord): Int {
        if (other.dir != this.dir) {
            return 1000
        }
        return 1
    }
}

fun main() {
    val grid = Utils.readAsGrid("inputs/input16.txt", null, { it })
    val nodes = grid.filterValues { it != '#' }.keys
    val start = Utils.findFirstIdx('S', grid)
    val end = Utils.findFirstIdx('E', grid)
    Utils.printGrid(grid)
    val INFINITY = Int.MAX_VALUE - 10000
    val memo = mutableMapOf<DirectionCoord, Int>()
    val seen = mutableSetOf<DirectionCoord>()

    fun dijkstra() {
        val dist = mutableMapOf<DirectionCoord, Int>()
        val prev = mutableMapOf<DirectionCoord, DirectionCoord?>()
        val allNodes = mutableSetOf<DirectionCoord>()
        for (dir in Direction.entries) {
            nodes.forEach {
                allNodes.add(DirectionCoord(it, dir))
                dist[DirectionCoord(it, dir)] = INFINITY
                prev[DirectionCoord(it, dir)] = null
            }
            dist[DirectionCoord(end, dir)] = 0
        }
        val Q = allNodes.toMutableSet()


        while (Q.isNotEmpty()) {
            val min = Q.minBy { dist[it]!! }
            Q.remove(min)
            (min.neighbors() intersect Q).forEach { unrelaxedNeighbor ->
                val altDist = dist[min]!! + min.dist(unrelaxedNeighbor)
                if (altDist < dist[unrelaxedNeighbor]!!) {
                    dist[unrelaxedNeighbor] = altDist
                    prev[unrelaxedNeighbor] = min
                }
            }

        }
    }

    fun helper(current: DirectionCoord): Int {
//        println(current.coord)
        if (current.coord == end) {
//            println(current)
            return 0
        }


//        if (current in memo) {
//            return memo[current]!!
//        }

        // Prevent looping back on oneself, keep track of positions on the stack but not memoized yet
        if (current in seen || grid[current.coord] == null || grid[current.coord] == '#') {
            return INFINITY
        }
        seen.add(current)

        // Recurse
        val recurse = when (current.dir) {
            Direction.UP -> listOf(
                1000 + helper(current.copy(dir = Direction.LEFT)),
                1000 + helper(current.copy(dir = Direction.RIGHT)),
                1 + helper(
                    current.copy(coord = current.coord.copy(x = current.coord.x - 1)),
                )
            )

            Direction.DOWN -> listOf(
                1000 + helper(current.copy(dir = Direction.LEFT)),
                1000 + helper(current.copy(dir = Direction.RIGHT)),
                1 + helper(
                    current.copy(coord = current.coord.copy(x = current.coord.x + 1)),
                )
            )

            Direction.RIGHT -> listOf(
                1000 + helper(current.copy(dir = Direction.UP)),
                1000 + helper(current.copy(dir = Direction.DOWN)),
                1 + helper(
                    current.copy(coord = current.coord.copy(y = current.coord.y + 1)),
                )
            )

            Direction.LEFT -> listOf(
                1000 + helper(current.copy(dir = Direction.UP)),
                1000 + helper(current.copy(dir = Direction.DOWN)),
                1 + helper(
                    current.copy(coord = current.coord.copy(y = current.coord.y - 1)),
                )
            )
        }
        memo[current] = recurse.minOrNull() ?: INFINITY
//        seen.remove(current)
        return memo[current]!!
    }
    println(helper(DirectionCoord(Coord(7, 14), Direction.RIGHT)))
    println(memo)
}