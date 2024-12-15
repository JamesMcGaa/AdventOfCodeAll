import java.io.File
import java.util.BitSet
import kotlin.collections.set
import kotlin.math.min
import kotlin.time.measureTime

val input = File("inputs/input18.txt").readLines()
val X = input.size
val Y = input[0].length

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
                val coord = Coord(i,j)
                val toPrint = graph[Coord(i,j)] ?: '#'
                if (coord in listOf(coord1, coord2, coord3, coord4)) {
                    row += "@"
                } else {
                    row += toPrint
                }
            }
            println(row)
        }
        println(keys)
        println("-----------------------------------------------------")
    }
}

const val INFINITY = Int.MAX_VALUE - 730 // Arbitrary

fun partBPrunedDFS(): Int {
    val input = File("inputs/input18btrue.txt").readLines()
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
//            println(soFar)
//            println(currentBest)
            currentBest = min(currentBest, soFar)
            current.nicePrint(graph)
            return 0
        }

        // Prevent looping back on oneself
        if (current in seen) {
            return INFINITY
        }
        seen.add(current)

        // Prune inefficient branches
        if (soFar >= currentBest) {
            return INFINITY
        }


        // Recurse
        val recurse = mutableListOf<Int>()
//        println(soFar)
        current.neighbors(graph).forEach { neighbor ->
            val recursionResult = 1 + dfs(soFar + 1, graph, neighbor)
            recurse.add(recursionResult)
//            currentBest = min(currentBest, recursionResult)
        }
        memo[current] = recurse.minOrNull() ?: INFINITY
        if (memo[current]!! == INFINITY) {
            seen.remove(current)
        }
//        currentBest = min(currentBest, memo[current]!!)
        return memo[current]!!
    }

    val ret = dfs(0, graph, start)
    println(ret)
    println(currentBest)
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
//    println(measureTime { partA() })
//    println(measureTime { partBBruteForce() })
    println(measureTime { partBPrunedDFS() })
}
