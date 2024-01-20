import java.io.File

data class GraphCoord(var x: Int, var y: Int) {
    fun getNeighborsUnfiltered(): List<GraphCoord> {
        val ret = mutableListOf<GraphCoord>()

        val offsets = mutableListOf(
                GraphCoord(0, 1), GraphCoord(0, -1), GraphCoord(1, 0), GraphCoord(-1, 0)
            )

        for (offset in offsets) {
            ret.add(GraphCoord(x + offset.x, y + offset.y))

        }
        return ret
    }
}

data class ReflectedCoord(var x: Int, var y: Int, var xr: Int, var yr: Int)

data class Walk(
    val current: GraphCoord,
    val dist: Int,
    val xReflection: Int,
    val yReflection: Int
) {


    val reflected = ReflectedCoord(current.x, current.y, xReflection, yReflection)

    fun getNeighbors(grid: Map<GraphCoord, Char>): List<Walk> {

        val ret = mutableListOf<Walk>()
        for (offset in mutableListOf(
            GraphCoord(0, 1), GraphCoord(0, -1), GraphCoord(1, 0), GraphCoord(-1, 0)
        )) {
            var potential = GraphCoord(current.x + offset.x, current.y + offset.y)
            var xr = xReflection
            var yr = yReflection
            if (grid[potential] == null) {
                if (offset.x == 1) {
                    potential = potential.copy(x = 0)
                    xr += 1
                }
                if (offset.x == -1) {
                    potential = potential.copy(x = grid.keys.map {it.x}.max())
                    xr -= 1
                }
                if (offset.y == 1) {
                    potential = potential.copy(y = 0)
                    yr += 1
                }
                if (offset.y == -1) {
                    potential = potential.copy(y = grid.keys.map {it.y}.max())
                    yr -= 1
                }
            }
            if (setOf('.', 'S').contains(grid[potential])) {
                ret.add(this.copy(current = potential,
                    dist = dist + 1, xReflection = xr, yReflection = yr))
            }
        }
        return ret
    }
}

fun main() {
    lateinit var start: GraphCoord
    val grid = mutableMapOf<GraphCoord, Char>()
    val input = File("inputs/input21.txt").readLines()
    for (x in input.indices) {
        for (y in input[x].indices) {
            grid[GraphCoord(x, y)] = input[x][y]
            if (input[x][y] == 'S') {
                start = GraphCoord(x, y)
            }
        }
    }

    var walks = mutableSetOf<Walk>(
        Walk(
            start,
            0,
            0,
            0
        )
    )

    val walkIdxToCount = mutableMapOf<Int, Int>()
    val base = 65 // Time to fill full grid
    val period = 131 // Time grid width
    for (i in 1..base + 2 * period) {
        val nextWalks = mutableSetOf<Walk>()
        for (walk in walks) {
            for (neighbor in walk.getNeighbors(grid)) {
                    nextWalks.add(neighbor)
            }
        }
        walks = nextWalks
        walkIdxToCount[i] = walks.size
    }

    println(walkIdxToCount[base])
    println(walkIdxToCount[base + period])
    println(walkIdxToCount[base + 2 * period])

    // {3738, 33270, 92194}
    // 3738 + 14836 x + 14696 x^2
}

/**
 * Note that the input is just a nice diamond shape, thus it must repeat in a nice pattern
 *
 * Note 26501365 = 202300 * 131 + 65
 *
 * Thus be solving for x = 0,1,2 corresponding to base, base + period, base + 2*period steps we determine a quadratic,
 * x = 202300 gives us our final answer
 *
 * This required a lot of help - many others found a lot of observations that lead to this clean recurrence
 */
