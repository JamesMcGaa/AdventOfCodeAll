import java.io.File
import java.util.BitSet
import kotlin.math.max
import kotlin.time.measureTime

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

data class SplitKeyMazeState(
    val coord1: Coord,
    val coord2: Coord,
    val coord3: Coord,
    val coord4: Coord,
    val keys: BitSet,
) {
    fun neighbors(graph: MutableMap<Coord, Char>): List<SplitKeyMazeState> {
        val ret = mutableListOf<SplitKeyMazeState>()
        coord1.neighbors.forEach { neighbor1 ->
            coord2.neighbors.forEach { neighbor2 ->
                coord3.neighbors.forEach { neighbor3 ->
                    coord4.neighbors.forEach { neighbor4 ->
                        val good = listOf(neighbor1, neighbor2, neighbor3, neighbor4).all { selectedNeighbor ->
                            val graphVal = graph[selectedNeighbor]
                            if (graphVal == null) return@all false
                            return@all graphVal == '.' || graphVal.isLowerCase() || (graphVal.isUpperCase() && keys.get(graphVal.toLowerCase() - 'a'))
                        }
                        if (good) {
                            val newKeys =
                                listOf(neighbor1, neighbor2, neighbor3, neighbor4).mapNotNull { graph[it] }
                                    .filter { it.isLowerCase() }
                            ret.add(
                                SplitKeyMazeState(
                                    neighbor1,
                                    neighbor2,
                                    neighbor3,
                                    neighbor4,
                                    keysWithMultipleNewKeys(keys, newKeys)
                                )
                            )
                        }
                    }
                }
            }
        }
        return ret
    }
}


fun partB(): Int {
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

    var keyThreshold = mutableMapOf<Int, Int>()
    val start = SplitKeyMazeState(origins[0],origins[1],origins[2],origins[3], BitSet(keyAlphabetSize))
    val seen = mutableSetOf<SplitKeyMazeState>(start)
    var frontier = mutableSetOf<SplitKeyMazeState>(start)
    var iterations = 0
    while (frontier.isNotEmpty()) {
        println("Iterations: $iterations, Seen Size: ${seen.size}, Frontier Size: ${frontier.size}, Key Treshold: ${keyThreshold[iterations-1]}")
        val newFrontier = mutableSetOf<SplitKeyMazeState>()
        keyThreshold[iterations] = max(keyThreshold.getOrDefault(iterations - 1, 0), frontier.maxOf {it.keys.length()})
        iterations++
        frontier.forEach { point ->
            point.neighbors(graph).forEach { neighbor ->
                if (neighbor !in seen && neighbor.keys.length() >= keyThreshold.getOrDefault(iterations - 2, 0)) {
                    seen.add(neighbor)
                    newFrontier.add(neighbor)
                    if (neighbor.keys.length() == keyAlphabetSize) {
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
fun keysWithMultipleNewKeys(keys: BitSet, newKeys: List<Char>): BitSet {
    val newBitset = keys.clone() as BitSet
    newKeys.forEach { ch ->
        val idx = (ch - 'a').toInt()
        newBitset.set(idx, true)
    }
    return newBitset
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

fun main() {
    println(measureTime { partA() })
    partB()
}
