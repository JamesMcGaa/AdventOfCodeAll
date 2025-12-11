@file:Suppress("unused")

import com.google.common.collect.Collections2
import com.google.common.collect.Sets
import java.io.File
import java.util.*
import kotlin.math.absoluteValue

object Utils {
    data class ZCoord(
        var x: Int,
        var y: Int,
        var z: Int,
    ) {

        val manhattanDist = x.absoluteValue + y.absoluteValue + z.absoluteValue

        val manhattanNeighbors: Set<ZCoord>
            get() = setOf(
                LEFT_COORD,
                RIGHT_COORD,
                UP_COORD,
                DOWN_COORD,
                FORWARD_COORD,
                BACKWARD_COORD
            ).map { this + it }.toSet()

        companion object {
            val LEFT_COORD = ZCoord(0, -1, 0)
            val RIGHT_COORD = ZCoord(0, 1, 0)
            val UP_COORD = ZCoord(-1, 0, 0)
            val DOWN_COORD = ZCoord(1, 0, 0)
            val FORWARD_COORD = ZCoord(0, 0, 1)
            val BACKWARD_COORD = ZCoord(0, 0, -1)

        }

        operator fun plus(other: ZCoord): ZCoord {
            return ZCoord(x + other.x, y + other.y, z + other.z)
        }

        operator fun minus(other: ZCoord): ZCoord {
            return ZCoord(x - other.x, y - other.y, z - other.z)
        }
    }

    enum class ZDirection {
        UP, DOWN, LEFT, RIGHT, FORWARDS, BACKWARDS
    }

    data class Coord(
        var x: Int,
        var y: Int,
    ) {

        companion object {
            val ORIGIN = Coord(0, 0)
            val LEFT_COORD = Coord(0, -1)
            val RIGHT_COORD = Coord(0, 1)
            val UP_COORD = Coord(-1, 0)
            val DOWN_COORD = Coord(1, 0)
        }

        val up
            get() = Coord(x = x - 1, y = y)
        val down
            get() = Coord(x = x + 1, y = y)
        val left
            get() = Coord(x = x, y = y - 1)
        val right
            get() = Coord(x = x, y = y + 1)
        val downRight
            get() = Coord(x = x + 1, y = y + 1)
        val upLeft
            get() = Coord(x = x - 1, y = y - 1)
        val downLeft
            get() = Coord(x = x + 1, y = y - 1)
        val upRight
            get() = Coord(x = x - 1, y = y + 1)

        val manhattanDist = x.absoluteValue + y.absoluteValue

        val fullNeighbors: Set<Coord>
            get() = manhattanNeighbors + diagonalNeighbors

        val diagonalNeighbors: Set<Coord>
            get() = setOf(
                downRight, upLeft, downLeft, upRight
            )

        val manhattanNeighbors: Set<Coord>
            get() = setOf(up, down, left, right)


        operator fun plus(other: Coord): Coord {
            return Coord(x + other.x, y + other.y)
        }

        operator fun minus(other: Coord): Coord {
            return Coord(x - other.x, y - other.y)
        }

        operator fun times(other: Int): Coord {
            return Coord(x * other, y * other)
        }

        override fun toString(): String {
            return "($x, $y)"
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
        inputFilename: String, range: IntRange? = null, transform: (Char) -> T = { c: Char -> c as T }
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

    fun <T : Any> printGrid(grid: Map<Coord, T>, fileName: String? = null) {
        val minX = grid.keys.minOf { it.x }
        val maxX = grid.keys.maxOf { it.x }
        val minY = grid.keys.minOf { it.y }
        val maxY = grid.keys.maxOf { it.y }
        var outputStr = "\n"
        for (i in minX..maxX) {
            var row = ""
            for (j in minY..maxY) {
                row += grid[Coord(i, j)] ?: " "
            }
            outputStr += row
            outputStr += '\n'
        }
        if (fileName != null) {
            val file = File(fileName)
            file.appendText(outputStr)
        } else {
            println(outputStr)
        }
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

    fun <T> rotateClockwise(grid: Map<Coord, T>, shouldNormalize: Boolean = true): Map<Coord, T> {
        var newGrid = mutableMapOf<Coord, T>()
        grid.forEach { coord, value ->
            newGrid[Coord(coord.y, -coord.x)] = value
        }
        if (!shouldNormalize) {
            return newGrid
        }
        val minX = newGrid.keys.minOf { it.x }
        val minY = newGrid.keys.minOf { it.y }
        return newGrid.mapKeys { (coord, _) -> Coord(coord.x - minX, coord.y - minY) }
    }

    fun <T> rotateCounterclockwise(grid: Map<Coord, T>, shouldNormalize: Boolean = true): Map<Coord, T> {
        return rotateClockwise(
            rotateClockwise(rotateClockwise(grid, shouldNormalize), shouldNormalize),
            shouldNormalize
        )
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
        return generalizedBFS(grid, setOf(start), isLegal, neighbors)
    }

    fun <T> generalizedBFS(
        grid: MutableMap<Coord, T>,
        startSet: Collection<Coord>,
        isLegal: (Coord, MutableMap<Coord, T>) -> Boolean,
        neighbors: (Coord, MutableMap<Coord, T>) -> Set<Coord>,
    ): MutableMap<Coord, Int> {
        var frontier = startSet.toMutableSet()
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

    /**
     * Removes grid from above
     */
    fun <T> generalizedBFSV2(
        start: T,
        isLegal: (T) -> Boolean,
        neighbors: (T) -> Set<T>,
    ): MutableMap<T, Int> {
        var frontier = mutableSetOf(start)
        val seen = mutableSetOf<T>()
        val dists = mutableMapOf<T, Int>()
        var iterations = 0
        while (frontier.isNotEmpty()) {
            val newFrontier = mutableSetOf<T>()
            frontier.forEach { frontierNode ->
                if (frontierNode !in seen && isLegal(frontierNode)) {
                    seen.add(frontierNode)
                    dists[frontierNode] = iterations
                    newFrontier.addAll(neighbors(frontierNode))
                }
            }
            iterations++
            frontier = newFrontier
        }
        return dists
    }


    /**
     * Credits to wikipedia,
     *
     * T is your state variable (generally Coord or similar)
     */
    fun <T> generalizedDijkstra(
        start: T,
        nodes: Set<T>,
        neighbors: (T) -> Set<T>,
        edgeWeight: (T, T) -> Int,
    ): Pair<Map<T, Int>, Map<T, Set<T>>> {
        val dist = mutableMapOf<T, Int>()
        val prev = mutableMapOf<T, MutableSet<T>>()
        nodes.forEach {
            dist[it] = Int.MAX_VALUE / 2 // arbitrary
            prev[it] = mutableSetOf()
        }
        dist[start] = 0
        val queue = PriorityQueue<T> { v1, v2 -> dist[v1]!! - dist[v2]!! }
        queue.addAll(nodes)
        while (queue.isNotEmpty()) {
            val min = queue.poll()
            (neighbors(min) intersect queue).forEach { unrelaxedNeighbor ->
                val altDist = dist[min]!! + edgeWeight(min, unrelaxedNeighbor)
                if (altDist < dist[unrelaxedNeighbor]!!) {
                    dist[unrelaxedNeighbor] = altDist
                    prev[unrelaxedNeighbor]!!.clear()
                    queue.remove(unrelaxedNeighbor)
                    queue.add(unrelaxedNeighbor)
                    prev[unrelaxedNeighbor]!!.add(min)
                } else if (altDist == dist[unrelaxedNeighbor]!!) {
                    prev[unrelaxedNeighbor]!!.add(min)
                }
            }
        }
        return Pair(dist, prev)
    }

    fun <T> Iterable<T>.freqCount(): Map<T, Int> {
        return this.groupingBy { it }.eachCount()
    }

    fun extractIntListFromString(line: String, legalBreaks: Set<Char> = setOf(' ')): List<Int> {
        return line.filter { it in legalBreaks || it == '-' || it.isDigit() }.map { if (it in legalBreaks) ' ' else it }
            .joinToString("").split(" ")
            .filter { it.isNotBlank() }.map { it.toInt() }
    }

    fun extractLongListFromString(line: String, legalBreaks: Set<Char> = setOf(' ')): List<Long> {
        return line.filter { it in legalBreaks || it == '-' || it.isDigit() }.map { if (it in legalBreaks) ' ' else it }
            .joinToString("").split(" ")
            .filter { it.isNotBlank() }.map { it.toLong() }
    }

    fun <T, V> iterprint(iter: Map<T, V>) {
        for (item in iter) {
            println(item)
        }
    }

    fun <T> iterprint(iter: Iterable<T>) {
        for (item in iter) {
            println(item)
        }
    }

    fun Collection<Long>.median(): Double {
        val sorted = this.sorted()
        return when {
            sorted.size % 2 == 0 -> {
                val middle1 = sorted[sorted.size / 2 - 1]
                val middle2 = sorted[sorted.size / 2]
                (middle1 + middle2) / 2.0
            }

            else -> sorted[sorted.size / 2].toDouble()
        }
    }

    fun crtSolve(equations: List<Pair<Long, Long>>): Long {
        val N = equations.map { it.second }.reduce { acc, i -> acc * i }
        return equations.sumOf { pair ->
            val a_i = pair.first
            val n_i = pair.second
            val y_i = N / n_i
            val z_i = modInverse(y_i, n_i)
            a_i * y_i * z_i!! % N
        } % N
    }

    fun extendedGcd(a: Long, b: Long): Triple<Long, Long, Long> {
        if (b == 0L) return Triple(a, 1L, 0L)
        val (g, x1, y1) = extendedGcd(b, a % b)
        val x = y1
        val y = x1 - (a / b) * y1
        return Triple(g, x, y)
    }

    fun modInverse(a: Long, m: Long): Long? {
        val (g, x, _) = extendedGcd(a, m)
        if (g != 1L) return null
        return Math.floorMod(x, m)
    }

    // T : Any for Java - Kotlin interop
    fun <T : Any> orderedSubsets(elements: Collection<T>, n: Int): List<List<T>> {
        val allOrderedSubsets = mutableListOf<List<T>>()
        val setElements = elements.toSet()
        val combinations = Sets.combinations(setElements, n)
        for (combination in combinations) {
            val permutations = Collections2.permutations(combination)
            allOrderedSubsets.addAll(permutations.map { it.toList() })
        }
        return allOrderedSubsets
    }

    class SlidingWindow<T : Any>(val maxSize: Long) {
        val window = ArrayDeque<T>()

        fun addAndPotentiallyRemove(element: T): T? {
            window.addLast(element)
            return if (window.size > maxSize) {
                window.removeFirst()
            } else {
                null
            }
        }

    }
}