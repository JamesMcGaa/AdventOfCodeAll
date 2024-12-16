@file:Suppress("unused")

import java.io.File

object Utils {
    data class Coord(
        var x: Int,
        var y: Int,
    ) {
        val fullNeighbors: Set<Coord>
            get() = manhattanNeighbors + diagonalNeighbors

        val diagonalNeighbors: Set<Coord>
            get() = setOf(
                Coord(x = x+1, y = y+1), // DOWN-RIGHT
                Coord(x = x-1, y = y-1), // UP-LEFT
                Coord(x = x+1, y = y-1), // DOWN-LEFT
                Coord(x = x-1, y = y+1), // DOWN-RIGHT
            )

        val manhattanNeighbors: Set<Coord>
            get() = setOf(
                Coord(x = x-1, y = y), // UP
                Coord(x = x+1, y = y), // DOWN
                Coord(x = x, y = y-1), // LEFT
                Coord(x = x, y = y+1), // RIGHT
            )

        operator fun plus(other: Coord): Coord {
            return Coord(x + other.x, y + other.y)
        }

        operator fun minus(other: Coord): Coord {
            return Coord(x - other.x, y - other.y)
        }
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    fun <T> readAsGrid(inputFilename: String, range: IntRange?, transform: (Char) -> T): MutableMap<Coord, T> {
        val grid = mutableMapOf<Coord, T>()
        var input = File(inputFilename).readLines()
        if (range != null) {
            input = input.subList(range.first, range.last + 1)
        }
        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, ch ->
                grid[Coord(i, j)] = transform(ch)
            }
        }
        return grid
    }

    fun  <T : Any> printGrid(grid: MutableMap<Coord, T>) {
        val minX = grid.keys.minOf { it.x }
        val maxX = grid.keys.maxOf { it.x }
        val minY = grid.keys.minOf { it.y }
        val maxY = grid.keys.maxOf { it.y }
        println()
        for (i in minX..maxX) {
            var row = ""
            for (j in minY..maxY) {
                row += grid[Coord(i, j)]
            }
            println(row)
        }
        println()
    }

    fun  <T : Any> findFirstIdx(target: T, grid: MutableMap<Coord, T>): Coord {
        return grid.keys.first { grid[it] == target }
    }
}