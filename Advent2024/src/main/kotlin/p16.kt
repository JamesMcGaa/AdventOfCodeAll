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
    val grid = Utils.readAsGrid("inputs/input16.txt", null) { it }
    val nodes = grid.filterValues { it != '#' }.keys
    val start = Utils.findFirstIdx('S', grid)
    val end = Utils.findFirstIdx('E', grid)
    Utils.printGrid(grid)
    val infinity = Int.MAX_VALUE

    fun dijkstra(start: DirectionCoord): Int {
        val dist = mutableMapOf<DirectionCoord, Int>()
        val prev = mutableMapOf<DirectionCoord, MutableSet<DirectionCoord>>()
        val allNodes = mutableSetOf<DirectionCoord>()
        for (dir in Direction.entries) {
            nodes.forEach {
                allNodes.add(DirectionCoord(it, dir))
                dist[DirectionCoord(it, dir)] = infinity
                prev[DirectionCoord(it, dir)] = mutableSetOf()
            }
            dist[DirectionCoord(end, dir)] = 0
        }
        val queue = allNodes.toMutableSet()


        while (queue.isNotEmpty()) {
            val min = queue.minBy { dist[it]!! }
            queue.remove(min)
            (min.neighbors() intersect queue).forEach { unrelaxedNeighbor ->
                val altDist = dist[min]!! + min.dist(unrelaxedNeighbor)
                if (altDist < dist[unrelaxedNeighbor]!!) {
                    dist[unrelaxedNeighbor] = altDist
                    prev[unrelaxedNeighbor]!!.clear()
                    prev[unrelaxedNeighbor]!!.add(min)
                } else if (altDist == dist[unrelaxedNeighbor]!!) {
                    prev[unrelaxedNeighbor]!!.add(min)
                }
            }
        }

        val output = mutableListOf<DirectionCoord>()
        var stack = mutableListOf<DirectionCoord>(start)
        val seen = mutableSetOf<DirectionCoord>(start)

        while (stack.isNotEmpty()) {
            val last = stack.removeLast()
            output.add(last)
            val new = (prev[last] ?: mutableSetOf())
            stack.addAll(new - seen)
            seen.addAll(new)
        }
        println("Part B: ${output.map { it.coord }.toSet().size}")
        return dist[start]!!
    }

    println("Part A: ${dijkstra(DirectionCoord(start, Direction.RIGHT))}")
}