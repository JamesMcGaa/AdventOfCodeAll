import java.io.File

fun main() {
    var grid = Grid(false)
    repeat(100) {
        grid.cycle()
    }
    println(grid.lights)

    // Part B
    grid = Grid(true)
    repeat(100) {
        grid.cycle()
    }
    println(grid.lights)
}

class Grid(val permaCorner: Boolean) {
    var grid = mutableMapOf<Pair<Int, Int>, Boolean>()

    val lights
        get() = grid.values.filter {it}.size

    val input = File("input/input18.txt").readLines()

    init {
        for (i in input.indices) {
            for (j in input[0].indices) {
                grid[Pair(i, j)] = input[i][j] == '#'
            }
        }
        if (permaCorner) {
            cornerFlipOn()
        }
    }

    fun cornerFlipOn() {
        grid[Pair(0, 0)] = true
        grid[Pair(0, input.lastIndex)] = true
        grid[Pair(input.lastIndex, 0)] = true
        grid[Pair(input.lastIndex, input.lastIndex)] = true
    }

    fun getNeighbors(coord: Pair<Int, Int>): Int {
        var counter = 0
        for (i in -1..1) {
            for (j in -1..1) {
                val neighbor = Pair(coord.first + i, coord.second + j)
                if (!(i == 0 && j == 0) && grid.containsKey(neighbor) && grid[neighbor]!!) {
                    counter += 1
                }
            }
        }
        return counter
    }

    fun cycle() {
        val newGrid = mutableMapOf<Pair<Int, Int>, Boolean>()
        for (coord in grid.keys) {
            val old = grid[coord]!!
            if (old) {
                newGrid[coord] = listOf(2, 3).contains(getNeighbors(coord))
            } else {
                newGrid[coord] = 3 == getNeighbors(coord)
            }
        }
        grid = newGrid
        if (permaCorner) {
            cornerFlipOn()
        }
    }
}

