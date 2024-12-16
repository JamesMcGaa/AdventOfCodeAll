@file:Suppress("unused")

import java.io.File
import java.util.BitSet
import kotlin.collections.set
import kotlin.time.measureTime

fun partBPrecalculated(): Int {
    val input = File("inputs/input18btrue.txt").readLines()
    val X = input.size
    val Y = input[0].length

    val graph = mutableMapOf<Coord, Char>()
    val keyToCoord = mutableMapOf<Char, Coord>()
    val graphDoorFree = mutableMapOf<Coord, Char>() // For finding keys
    val traversableSpots = mutableSetOf<Coord>() // For finding dists between keys
    val origins = mutableListOf<Coord>()
    for (i in 0 until X) {
        for (j in 0 until Y) {
            if (input[i][j] != '#') {
                val point = Coord(i, j)
                var char = input[i][j]
                if (char == '@') {
                    origins.add(point)
                    char = '.'
                }
                graph[point] = char

                graphDoorFree[point] = if (!char.isUpperCase()) char else '.'
                if (char.isLowerCase()) {
                    keyToCoord[char] = point
                }
                if (char != '#') {
                    traversableSpots.add(point)
                }
            }
        }
    }

    println("keyToCoord: $keyToCoord")
    val keyAlphabetSize = graph.values.toSet().filter { it.isLowerCase() }.size
    val keyCoords = origins.map { findKeysetInQuadrant(it, graphDoorFree).toSet() }
    val keyVals = keyCoords.map { it.map { graphDoorFree[it]!! }.toSet() }
    println("keyCoords: $keyCoords")
    println("keyVals: $keyVals")

    fun dfsDistBetweenKeys(start: Coord, end: Coord): Int {
        val seen = mutableSetOf<Coord>()

        fun helper(current: Coord, end: Coord): Int? {
            if (current == end) {
                return 0
            }
            if (current in seen || current !in traversableSpots) {
                return null
            }
            seen.add(current)

            return 1 + (current.neighbors.mapNotNull { helper(it, end) }.minOrNull() ?: 100000)

        }
        return helper(start, end)!!
    }

    fun bfsKeysBetweenKeys(start: Coord, end: Coord): BitSet {
        val seen = mutableSetOf<Coord>()
        var frontier = mutableSetOf<Pair<Coord, BitSet>>(Pair(start, BitSet(keyAlphabetSize)))

        while (true) {
            val newFrontier = mutableSetOf<Pair<Coord, BitSet>>()
            frontier.forEach { (current, keys) ->
                current.neighbors.forEach { neighbor ->
                    if (neighbor !in seen && neighbor in graph && graph[neighbor] != '#') {
                        var newKeys = keys.clone() as BitSet
                        if (graph[neighbor]!!.isUpperCase()) {
                            newKeys.set(graph[neighbor]!! - 'A', true)
                        }
                        seen.add(neighbor)
                        newFrontier.add(Pair(neighbor, newKeys))

                        if (neighbor == end) {
                            return keys
                        }
                    }
                }
            }
            frontier = newFrontier
        }
    }

    val pairwiseKeyDists = mutableMapOf<Pair<Coord, Coord>, Int>()
    val pairwiseKeyDeps = mutableMapOf<Pair<Coord, Coord>, BitSet>()

    keyCoords.forEachIndexed { idx, originList ->
        for (p1 in originList + origins[idx]) {
            for (p2 in originList) {
                if (p1 != p2) {
                    pairwiseKeyDists[Pair(p1, p2)] = dfsDistBetweenKeys(p1, p2)
                    pairwiseKeyDeps[Pair(p1, p2)] = bfsKeysBetweenKeys(p1, p2)
                }
            }
        }
    }

    data class State(
        val coord1: Coord,
        val coord2: Coord,
        val coord3: Coord,
        val coord4: Coord,
        val keys: BitSet,
    ) {
        fun nicePrint(graph: Map<Coord, Char>) {
            val input = File("inputs/input18b.txt").readLines()
            val X = input.size
            val Y = input[0].length
            for (i in 0 until X) {
                var row = ""
                for (j in 0 until Y) {
                    val coord = Coord(i, j)
                    val toPrint = graph[Coord(i, j)] ?: '#'
                    row += if (coord in listOf(coord1, coord2, coord3, coord4)) {
                        "@"
                    } else {
                        toPrint
                    }
                }
                println(row)
            }
            println(keys)
            println("-----------------------------------------------------")
        }
    }

    fun BitSet.toCharSet(): Set<Char> {
        val ret = mutableSetOf<Char>()
        for (i in 0 until keyAlphabetSize) {
            if (this.get(i)) {
                ret.add('a' + i)
            }
        }
        return ret
    }

    val memo = mutableMapOf<State, Int>()
    val seen = mutableSetOf<State>()

    fun helper(current: State): Int {
        // Avoid doing repeat work
        if (current in memo) {
            return memo[current]!!
        }

        // Base case
        if (current.keys.cardinality() == keyAlphabetSize) {
//            current.nicePrint(graph)
            return 0
        }

        // Prevent looping back on oneself, keep track of positions on the stack but not memoized yet
        if (current in seen) {
            return INFINITY
        }
        seen.add(current)

        // Recurse
        val recurse = mutableListOf<Int>()

        for (quadrant in 0..3) {
            val missingKeys1 = keyVals[quadrant] - current.keys.toCharSet()
            missingKeys1.forEach {
                val missingKeyPos = keyToCoord[it]!!
                val currentPos = when (quadrant) {
                    0 -> current.coord1
                    1 -> current.coord2
                    2 -> current.coord3
                    3 -> current.coord4
                    else -> throw Exception("Bad Quadrant")
                }
                val requiredDeps = pairwiseKeyDeps[Pair(currentPos, missingKeyPos)]!!
                val keyCheck = (requiredDeps.clone() as BitSet)
                keyCheck.and(current.keys)
                if (keyCheck.cardinality() == requiredDeps.cardinality()) { // If we have all the keys
                    recurse.add(
                        pairwiseKeyDists[Pair(
                            currentPos,
                            missingKeyPos
                        )]!! + helper(
                            when (quadrant) {
                                0 -> current.copy(
                                    coord1 = missingKeyPos,
                                    keys = keysWithMultipleNewKeys(current.keys, listOf(it))
                                )

                                1 -> current.copy(
                                    coord2 = missingKeyPos,
                                    keys = keysWithMultipleNewKeys(current.keys, listOf(it))
                                )

                                2 -> current.copy(
                                    coord3 = missingKeyPos,
                                    keys = keysWithMultipleNewKeys(current.keys, listOf(it))
                                )

                                3 -> current.copy(
                                    coord4 = missingKeyPos,
                                    keys = keysWithMultipleNewKeys(current.keys, listOf(it))
                                )

                                else -> throw Exception("Bad Quadrant")
                            }
                        )
                    )
                }
            }
        }

        memo[current] = recurse.minOrNull() ?: INFINITY
        seen.remove(current)
        return memo[current]!!
    }


    println("pairwiseKeyDists: $pairwiseKeyDists")
    println("quadrant keyset sizes: ${keyCoords.map { it.size }}")
    println("pairwiseKeyDeps: $pairwiseKeyDeps")

    val start = State(origins[0], origins[1], origins[2], origins[3], BitSet(keyAlphabetSize))
    val ret = helper(start)
    println(ret)
    return ret
}


fun findKeysetInQuadrant(start: Coord, grid: Map<Coord, Char>): Set<Coord> {
    val ret = mutableSetOf<Coord>()
    val stack = mutableListOf(start)
    val seen = mutableSetOf<Coord>()
    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        if (current in seen) {
            continue
        }
        seen.add(current)
        if (grid[current]?.isLowerCase() == true) {
            ret.add(current)
        }
        if (grid[current]?.isLowerCase() == true || grid[current] == '.') {
            stack.addAll(current.neighbors)
        }
    }
    return ret
}


//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
data class SplitKeyMazeState(
    val coord1: Coord,
    val coord2: Coord,
    val coord3: Coord,
    val coord4: Coord,
    val keys: BitSet,
) {
    fun neighbors(graph: Map<Coord, Char>): List<SplitKeyMazeState> {
        val ret = mutableListOf<SplitKeyMazeState>()
        coord1.neighbors.forEach { neighbor1 ->
            val graphVal = graph[neighbor1] ?: return@forEach
            if (graphVal == '.' || graphVal.isLowerCase() || (graphVal.isUpperCase() && keys.get(graphVal.toLowerCase() - 'a'))) {
                val newKeys =
                    listOf(neighbor1).mapNotNull { graph[it] }.filter { it.isLowerCase() }
                ret.add(
                    copy(coord1 = neighbor1, keys = keysWithMultipleNewKeys(keys, newKeys))
                )
            }
        }
        coord2.neighbors.forEach { neighbor2 ->
            val graphVal = graph[neighbor2] ?: return@forEach
            if (graphVal == '.' || graphVal.isLowerCase() || (graphVal.isUpperCase() && keys.get(graphVal.toLowerCase() - 'a'))) {
                val newKeys =
                    listOf(neighbor2).mapNotNull { graph[it] }.filter { it.isLowerCase() }
                ret.add(
                    copy(coord2 = neighbor2, keys = keysWithMultipleNewKeys(keys, newKeys))
                )
            }
        }
        coord3.neighbors.forEach { neighbor3 ->
            val graphVal = graph[neighbor3] ?: return@forEach
            if (graphVal == '.' || graphVal.isLowerCase() || (graphVal.isUpperCase() && keys.get(graphVal.toLowerCase() - 'a'))) {
                val newKeys =
                    listOf(neighbor3).mapNotNull { graph[it] }.filter { it.isLowerCase() }
                ret.add(
                    copy(coord3 = neighbor3, keys = keysWithMultipleNewKeys(keys, newKeys))
                )
            }
        }
        coord4.neighbors.forEach { neighbor4 ->
            val graphVal = graph[neighbor4] ?: return@forEach
            if (graphVal == '.' || graphVal.isLowerCase() || (graphVal.isUpperCase() && keys.get(graphVal.toLowerCase() - 'a'))) {
                val newKeys =
                    listOf(neighbor4).mapNotNull { graph[it] }.filter { it.isLowerCase() }
                ret.add(
                    copy(coord4 = neighbor4, keys = keysWithMultipleNewKeys(keys, newKeys))
                )
            }
        }
        return ret
    }

    fun nicePrint(graph: Map<Coord, Char>) {
        val input = File("inputs/input18b.txt").readLines()
        val X = input.size
        val Y = input[0].length
        for (i in 0 until X) {
            var row = ""
            for (j in 0 until Y) {
                val coord = Coord(i, j)
                val toPrint = graph[Coord(i, j)] ?: '#'
                row += if (coord in listOf(coord1, coord2, coord3, coord4)) {
                    "@"
                } else {
                    toPrint
                }
            }
            println(row)
        }
        println(keys)
        println("-----------------------------------------------------")
    }
}

const val INFINITY = Int.MAX_VALUE - 73000 // Arbitrary

fun partBPrunedDFS(): Int {
    val input = File("inputs/input18b.txt").readLines()
    val X = input.size
    val Y = input[0].length

    val graph = mutableMapOf<Coord, Char>()
    val origins = mutableListOf<Coord>()
    for (i in 0 until X) {
        for (j in 0 until Y) {
            if (input[i][j] != '#') {
                val point = Coord(i, j)
                var char = input[i][j]
                if (char == '@') {
                    origins.add(point)
                    char = '.'
                }
                graph[point] = char
            }
        }
    }

    val keyAlphabetSize = graph.values.toSet().filter { it.isLowerCase() }.size
    val start = SplitKeyMazeState(origins[0], origins[1], origins[2], origins[3], BitSet(keyAlphabetSize))
    val memo = mutableMapOf<SplitKeyMazeState, Int>()
    val seen = mutableSetOf<SplitKeyMazeState>()
    var currentBest = 10000 // Arbitrary

    fun dfs(
        soFar: Int,
        graph: Map<Coord, Char>,
        current: SplitKeyMazeState,
    ): Int {
        // Avoid doing repeat work
        if (current in memo) {
            return memo[current]!!
        }

        // Base case
        if (current.keys.cardinality() == keyAlphabetSize) {
            current.nicePrint(graph)
            return 0
        }

        // Prevent looping back on oneself, keep track of positions on the stack but not memoized yet
        if (current in seen) {
            return INFINITY
        }
        seen.add(current)

        // Recurse
        val recurse = mutableListOf<Int>()
        current.neighbors(graph).forEach { neighbor ->
            val recursionResult = 1 + dfs(soFar + 1, graph, neighbor)
            recurse.add(recursionResult)
        }
        memo[current] = recurse.minOrNull() ?: INFINITY
        seen.remove(current)
        return memo[current]!!
    }

    val ret = dfs(0, graph, start)
    println(ret)
    return ret
}


// Faster with bit tricks
fun keysWithMultipleNewKeys(keys: BitSet, newKeys: List<Char>): BitSet {
    val newBitset = keys.clone() as BitSet
    newKeys.forEach { ch ->
        val idx = (ch - 'a').toInt()
        newBitset.set(idx, true)
    }
    return newBitset
}

/**
 * Intractable
 */
fun partBBruteForce(): Int {
    val input = File("inputs/input18b.txt").readLines()
    val X = input.size
    val Y = input[0].length

    val graph = mutableMapOf<Coord, Char>()
    val origins = mutableListOf<Coord>()
    for (i in 0 until X) {
        for (j in 0 until Y) {
            if (input[i][j] != '#') {
                val point = Coord(i, j)
                var char = input[i][j]
                if (char == '@') {
                    origins.add(point)
                    char = '.'
                }
                graph[point] = char
            }
        }
    }
    val keyAlphabetSize = graph.values.toSet().filter { it.isLowerCase() }.size
    val start = SplitKeyMazeState(origins[0], origins[1], origins[2], origins[3], BitSet(keyAlphabetSize))
    val seen = mutableSetOf<SplitKeyMazeState>(start)
    var frontier = mutableSetOf<SplitKeyMazeState>(start)
    var iterations = 0
    while (frontier.isNotEmpty()) {
//        println("Iterations: $iterations, Seen Size: ${seen.size}, Frontier Size: ${frontier.size}")
        val newFrontier = mutableSetOf<SplitKeyMazeState>()
        iterations++
        frontier.forEach { point ->
            point.neighbors(graph).forEach { neighbor ->
                if (neighbor !in seen) {
                    seen.add(neighbor)
                    newFrontier.add(neighbor)
                    if (neighbor.keys.cardinality() == keyAlphabetSize) {
                        println("Part B: $iterations")
                        return iterations
                    }
                }
            }
        }
        frontier = newFrontier
    }
    return -1
}

// Faster with bit tricks
fun keysWithKey(keys: String, newKey: Char): String {
    if (newKey in keys) {
        return keys
    }
    return (keys + newKey).toCharArray().sorted().joinToString("")
}

fun partA(): Int {
    val input = File("inputs/input18.txt").readLines()
    val X = input.size
    val Y = input[0].length

    val graph = mutableMapOf<Coord, Char>()
    lateinit var ORIGIN: Coord
    for (i in 0 until X) {
        for (j in 0 until Y) {
            if (input[i][j] != '#') {
                val point = Coord(i, j)
                var char = input[i][j]
                if (char == '@') {
                    ORIGIN = point
                    char = '.'
                }
                graph[point] = char
            }
        }
    }
    val keyAlphabetSize = graph.values.toSet().filter { it.isLowerCase() }.size

    val seen = mutableSetOf<KeyMazeState>(KeyMazeState(ORIGIN, ""))
    var frontier = mutableSetOf<KeyMazeState>(KeyMazeState(ORIGIN, ""))
    var iterations = 0
    while (frontier.isNotEmpty()) {
        val newFrontier = mutableSetOf<KeyMazeState>()
        iterations++
        frontier.forEach { point ->
            point.neighbors(graph).forEach { neighbor ->
                if (neighbor !in seen) {
                    seen.add(neighbor)
                    newFrontier.add(neighbor)
                    if (neighbor.keys.length == keyAlphabetSize) {
                        println("Seen size: ${seen.size}")
                        println("Part A: $iterations")
                        return iterations
                    }
                }
            }
        }
        frontier = newFrontier
    }
    return -1
}

data class KeyMazeState(
    val coord: Coord,
    val keys: String,
) {
    fun neighbors(graph: MutableMap<Coord, Char>): List<KeyMazeState> {
        val ret = mutableListOf<KeyMazeState>()
        coord.neighbors.forEach { neighbor ->
            val graphVal = graph[neighbor]
            when {
                graphVal == null -> Unit
                graphVal == '.' -> {
                    ret.add(KeyMazeState(neighbor, keys))
                }

                graphVal.isUpperCase() -> {
                    if (graphVal.toLowerCase() in keys) {
                        ret.add(KeyMazeState(neighbor, keys))
                    }
                }

                graphVal.isLowerCase() -> {
                    ret.add(KeyMazeState(neighbor, keysWithKey(keys, graphVal)))
                }

                else -> throw Exception()
            }
        }
        return ret
    }
}

fun main() {
    println(measureTime { partBPrecalculated() })
}
