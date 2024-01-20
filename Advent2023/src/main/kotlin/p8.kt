import java.io.File

data class Node(
    val name: String,
    val left: String,
    val right: String
)

data class Ghost(
    var current: String,
    val seen: HashMap<Pair<String, Long>, Pair<Long, Boolean>> // Node, Move -> Index, wasTerminal
)

fun main() {
    val input = File("inputs/input8.txt").readLines()
    val MOVES = input[0]
    val GRAPH = hashMapOf<String, Node>()
    input.subList(2, input.lastIndex + 1).forEach {
        val start = it.substring(0, 2 + 1)
        val left = it.substring(7, 9 + 1)
        val right = it.substring(12, 14 + 1)
        GRAPH[start] = Node(start, left, right)
    }
    println("Graph: ${GRAPH}")

    var MATH =
        mutableListOf<Triple<Long, Long, List<Long>>>() // Initial cycle offset, cycle len, moduli that are terminal
    var counterB = 0L
    var currentGhosts =
        GRAPH.keys.filter { it.endsWith('A') }.map { Ghost(it, hashMapOf(Pair(it, 0L) to Pair(0L, false))) }
            .toMutableSet()
    while (currentGhosts.isNotEmpty()) {
        val moveIdx = (counterB % MOVES.length).toLong()
        val move = MOVES[moveIdx.toInt()]
        val newGhosts = mutableSetOf<Ghost>()
        currentGhosts.forEach { ghost ->
            val newCurrent = if (move == 'L') GRAPH[ghost.current]!!.left else GRAPH[ghost.current]!!.right
            val newSeenEntry = Pair(newCurrent, moveIdx)
            if (ghost.seen.contains(newSeenEntry) && newCurrent.endsWith('Z')) { // Loop detected
                val firstSeenIdx = ghost.seen[newSeenEntry]!!.first
                val moduli = ghost.seen.values.filter { it.second == true && it.first >= firstSeenIdx }
                    .map { it.first } // Parts ending with Z after the loop started (just the moduli)(
                MATH.add(Triple(counterB, counterB - firstSeenIdx, moduli))
            } else { // Update the ghost and keep looking for a loop
                ghost.seen[newSeenEntry] = Pair(counterB, newCurrent.endsWith('Z'))
                ghost.current = newCurrent
                newGhosts.add(ghost)
            }
        }
        counterB += 1
        currentGhosts = newGhosts
    }
    println("Raw Math: ${MATH}")
    val newMath =
        MATH.map { Triple(it.first - it.second, it.second, it.third.map { marker -> marker - (it.first - it.second) }) }
    println("Processed Math: ${newMath}")

    println(lcm(newMath.map { it.second }))

    /*
     * Note that from here we could do things iteratively, however by noting that the results of the math indicate that
     * they all move in cycles of 0 .. C_i starting from the very beginning
     *
     * The answer is just the LCM
     */


    // Naive attempt
//    var i = newMath.map { it.first }.max().toLong() // Begin where all cycles have started
//    outer@while (true) {
//        i += 1L
//        for (cycle in newMath) {
//            if (!cycle.third.contains(i % cycle.second)) {
//                continue@outer
//            }
//        }
//        println(i)
//        return
//    }
}

fun gcd(a: Long, b: Long): Long {
    var A = a
    var B = b
    while (B > 0) {
        val temp = B
        B = A % B
        A = temp
    }
    return A
}

private fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

private fun lcm(input: List<Long>): Long {
    var result = input[0]
    for (i in 1 until input.size) result = lcm(result, input[i])
    return result
}

