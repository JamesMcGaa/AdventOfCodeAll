@file:Suppress("unused")

import java.io.File
import kotlin.collections.first
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed
import kotlin.collections.isNotEmpty
import kotlin.collections.mapKeys
import kotlin.collections.maxOf
import kotlin.collections.minOf
import kotlin.collections.plus
import kotlin.collections.set
import kotlin.io.readLines
import kotlin.math.absoluteValue
import kotlin.text.forEachIndexed

object Utils {
    data class Coord(
        var x: Int,
        var y: Int,
    ) {
        val manhattanDist = x.absoluteValue + y.absoluteValue

        val fullNeighbors: Set<Coord>
            get() = manhattanNeighbors + diagonalNeighbors

        val diagonalNeighbors: Set<Coord>
            get() = setOf(
                Coord(x = x + 1, y = y + 1), // DOWN-RIGHT
                Coord(x = x - 1, y = y - 1), // UP-LEFT
                Coord(x = x + 1, y = y - 1), // DOWN-LEFT
                Coord(x = x - 1, y = y + 1), // DOWN-RIGHT
            )

        val manhattanNeighbors: Set<Coord>
            get() = setOf(
                Coord(x = x - 1, y = y), // UP
                Coord(x = x + 1, y = y), // DOWN
                Coord(x = x, y = y - 1), // LEFT
                Coord(x = x, y = y + 1), // RIGHT
            )

        operator fun plus(other: Coord): Coord {
            return Coord(x + other.x, y + other.y)
        }

        operator fun minus(other: Coord): Coord {
            return Coord(x - other.x, y - other.y)
        }

        fun moveDir(dir: Direction): Coord {
            return when (dir) {
                Direction.UP -> this.copy(x = this.x - 1)
                Direction.RIGHT -> this.copy(y = this.y + 1)
                Direction.DOWN -> this.copy(x = this.x + 1)
                Direction.LEFT -> this.copy(y = this.y - 1)
            }
        }
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun clockwise(): Direction {
            return when (this) {
                UP -> RIGHT
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
            }
        }

        fun ccw(): Direction {
            return when (this) {
                UP -> LEFT
                DOWN -> RIGHT
                LEFT -> DOWN
                RIGHT -> UP
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> readAsGrid(
        inputFilename: String,
        range: IntRange? = null,
        transform: (Char) -> T = { c: Char -> c as T }
    ): MutableMap<Coord, T> {
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

    fun <T : Any> printGrid(grid: Map<Coord, T>) {
        val minX = grid.keys.minOf { it.x }
        val maxX = grid.keys.maxOf { it.x }
        val minY = grid.keys.minOf { it.y }
        val maxY = grid.keys.maxOf { it.y }
        println()
        for (i in minX..maxX) {
            var row = ""
            for (j in minY..maxY) {
                row += grid[Coord(i, j)] ?: " "
            }
            println(row)
        }
        println()
    }

    fun <T : Any> findCoord(target: T, grid: MutableMap<Coord, T>): Coord {
        return grid.keys.first { grid[it] == target }
    }

    data class CircularLinkedListNode<T>(
        val value: T,
    ) {
        private var _next: CircularLinkedListNode<T>? = this
        private var _prev: CircularLinkedListNode<T>? = this

        companion object {
            fun <T> initFromList(inp: List<T>): CircularLinkedListNode<T> {
                val newHead = CircularLinkedListNode(inp.first())
                var prev = newHead
                for (i in inp.subList(1, inp.size)) {
                    val new = CircularLinkedListNode(i)
                    prev.next = new
                    new.prev = prev
                    prev = new
                }
                prev.next = newHead
                newHead.prev = prev
                return newHead
            }
        }

        fun toList(): List<T> {
            val ret = mutableListOf<T>()
            ret.add(this.value)
            var current = this.next
            while (current != this) {
                ret.add(current.value)
                current = current.next
            }
            return ret
        }

        fun find(target: T): CircularLinkedListNode<T> {
            if (this.value == target) {
                return this
            }
            var current = this.next
            while (current != this) {
                if (current.value == target) {
                    return current
                }
                current = current.next
            }
            throw Exception("Value not found in CLL")
        }

        var next: CircularLinkedListNode<T>
            get() = _next!!
            set(value) {
                _next = value
            }

        var prev: CircularLinkedListNode<T>
            get() = _prev!!
            set(value) {
                _prev = value
            }

        /**
         * Removes the current node from the CLL
         * Returns a pair of (the spliced node, the next node in the old list)
         */
        fun splice(): Pair<CircularLinkedListNode<T>, CircularLinkedListNode<T>> {
            prev.next = next
            next.prev = prev
            val replacementCCW = next
            this._next = null
            this._prev = null
            return Pair(this, replacementCCW)
        }

        /**
         * Inserts the new node CW of the current node
         * Returns the new node
         */
        fun insert(new: CircularLinkedListNode<T>): CircularLinkedListNode<T> {
            val oldNext = next
            new.next = oldNext
            new.prev = this
            this.next = new
            oldNext.prev = new
            return new
        }
    }

    fun <T> rotateClockwise(grid: Map<Coord, T>): Map<Coord, T> {
        var newGrid = mutableMapOf<Coord, T>()
        grid.forEach { coord, value ->
            newGrid[Coord(coord.y, -coord.x)] = value
        }
        val minX = newGrid.keys.minOf { it.x }
        val minY = newGrid.keys.minOf { it.y }
        return newGrid.mapKeys { (coord, _) -> Coord(coord.x - minX, coord.y - minY) }
    }

    fun <T> flip(grid: Map<Coord, T>): Map<Coord, T> {
        var newGrid = mutableMapOf<Coord, T>()
        val maxY = grid.keys.maxOf { it.y }
        grid.forEach { coord, value ->
            newGrid[Coord(coord.x, maxY - coord.y)] = value
        }
        return newGrid
    }

    /**
     * Returns a map of dists from the start coord, only works for generalized 1 weight edges
     * as a normal BFS does
     */
    fun <T> generalizedBFS(
        grid: MutableMap<Coord, T>,
        start: Coord,
        isLegal: (Coord, MutableMap<Coord, T>) -> Boolean,
        neighbors: (Coord, MutableMap<Coord, T>) -> Set<Coord>,
    ): MutableMap<Coord, Int> {
        var frontier = mutableSetOf(start)
        val seen = mutableSetOf<Coord>()
        val dists = mutableMapOf<Coord, Int>()
        var iterations = 0
        while (frontier.isNotEmpty()) {
            val newFrontier = mutableSetOf<Coord>()
            frontier.forEach { frontierNode ->
                if (frontierNode !in seen && isLegal(frontierNode, grid)) {
                    seen.add(frontierNode)
                    dists[frontierNode] = iterations
                    newFrontier.addAll(neighbors(frontierNode, grid))
                }
            }
            iterations++
            frontier = newFrontier
        }
        return dists
    }

    fun <T> Iterable<T>.freqCount(): Map<T, Int> {
        return this.groupingBy { it }.eachCount()
    }
}