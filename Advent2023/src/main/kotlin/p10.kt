import java.io.File
import java.util.HashMap

/**
 * Cool notes I discovered after completing and consulting the boards -
 *
 * - One person solved for this using Pick's theorem and the Shoelace Formula
 *
 * - Another used the trick of scanning left to right and counting number of times you hit the loop,
 *          odd parity means 'inside'
 *
 * - Double resolution + flood fill
 *
 * - My solution was a form of double resolution + DFS to see if we can escape
 */
fun main() {
    val BOARD = hashMapOf<Pair<Int, Int>, Char>()
    val lines = File("inputs/input10.txt").readLines()
    val POSSIBLE_PIPES = mutableListOf('|', '-', 'L', 'J', '7', 'F')

    lateinit var START_COORDS: Pair<Int, Int>

    for (y in lines.indices) {
        for (x in lines[0].indices) {
            BOARD[Pair(x, y)] = lines[y][x]
            if (lines[y][x] == 'S') {
                START_COORDS = Pair(x, y)
            }
        }
    }

    for (pipeCast in POSSIBLE_PIPES) {
        BOARD[START_COORDS] = pipeCast
        val stack = mutableListOf(START_COORDS)
        val seen: HashMap<Pair<Int, Int>, Int> = hashMapOf(START_COORDS to -1)
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (seen.containsKey(current) && seen[current] != -1) {
                seen[current] = seen.getOrDefault(current, 0) + 1
                continue
            } else {
                seen[current] = seen.getOrDefault(current, 0) + 1
                when (BOARD[current]) {
                    '|' -> {
                        stack.add(Pair(current.first, current.second - 1))
                        stack.add(Pair(current.first, current.second + 1))
                    }

                    '-' -> {
                        stack.add(Pair(current.first - 1, current.second))
                        stack.add(Pair(current.first + 1, current.second))
                    }

                    'L' -> {
                        stack.add(Pair(current.first, current.second - 1))
                        stack.add(Pair(current.first + 1, current.second))
                    }

                    'J' -> {
                        stack.add(Pair(current.first, current.second - 1))
                        stack.add(Pair(current.first - 1, current.second))
                    }

                    '7' -> {
                        stack.add(Pair(current.first - 1, current.second))
                        stack.add(Pair(current.first, current.second + 1))
                    }

                    'F' -> {
                        stack.add(Pair(current.first + 1, current.second))
                        stack.add(Pair(current.first, current.second + 1))
                    }

                    else -> Unit // Out of bounds, '.' ground pieces
                }
            }
        }
        if (seen.values.toSet().size == 1 && seen.values.toSet()
                .contains(2)
        ) { // All closed loops have 2 outgoing, 2 ingoing edges
            println(seen.keys.size / 2) // Part A answer, we can always move halfway around the loop, rounding down if odd

            // At this point seen is our true wallets, now we must construct a graph
            refinedSolveB(lines, BOARD, seen)
        }
    }
}

data class Node10(
    val adj: MutableList<Node10>,
    val chVal: Char?, // Null if synthetic
    var isBlocking: Boolean = false,
    var isLoop: Boolean = false,
)

/**
 * Correct attempt. In between each regular board / pipe piece we place a 'synthetic' node that connects to its neighbors if not
 * 'blocked' or lying between two connected pipes
 */
fun refinedSolveB(lines: List<String>, BOARD: HashMap<Pair<Int, Int>, Char>, loop: HashMap<Pair<Int, Int>, Int>) {
    var counterB = 0
    val GRAPH = hashMapOf<Pair<Int, Int>, Node10>()

    // Add all real nodes
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            GRAPH[Pair(2 * x, 2 * y)] = Node10(
                mutableListOf<Node10>(),
                BOARD[Pair(x, y)]!!
            )
        }
    }

    // Fill in all synthetic nodes
    for (y in 0..2 * lines.lastIndex) {
        for (x in 0..2 * lines[0].lastIndex) {
            val potentialCoord = Pair(x, y)
            if (!GRAPH.contains(potentialCoord)) {
                GRAPH[potentialCoord] = Node10(
                    mutableListOf<Node10>(),
                    null
                )
            }
        }
    }

    // Add each valid neighbor
    GRAPH.forEach { coords, node ->
        val adjOffsets = mutableListOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
        adjOffsets.forEach { offset ->
            val neighborCoords = Pair(offset.first + coords.first, offset.second + coords.second)
            if (GRAPH.containsKey(neighborCoords) && inBounds(neighborCoords, lines)) {
                node.adj.add(GRAPH[neighborCoords]!!)
            }
        }
    }

    // Mark loop as blocked
    loop.keys.forEach {
        val blocker = GRAPH[Pair(2 * it.first, 2 * it.second)]!!
        blocker.isBlocking = true
        blocker.isLoop = true
    }

    // Fill in closed loop blocking
    for (y in 0..2 * lines.lastIndex) {
        for (x in 0..2 * lines[0].lastIndex) {
            val current = GRAPH[Pair(x, y)]!!
            val potentialBottom = GRAPH[Pair(x, y + 2)]
            val potentialRight = GRAPH[Pair(x + 2, y)]
            if (
                potentialRight != null && current.isLoop && potentialRight.isLoop
                && mutableListOf('-', 'L', 'F').contains(current.chVal)
                && mutableListOf('-', '7', 'J').contains(potentialRight.chVal)
            ) {
                GRAPH[Pair(x + 1, y)]!!.isBlocking = true
            }

            if (
                potentialBottom != null && current.isLoop && potentialBottom.isLoop
                && mutableListOf('|', 'F', '7').contains(current.chVal)
                && mutableListOf('|', 'L', 'J').contains(potentialBottom.chVal)
            ) {
                GRAPH[Pair(x, y + 1)]!!.isBlocking = true
            }

        }
    }


    val nonEscapable = hashSetOf<Pair<Int, Int>>()
    GRAPH.forEach { coords, node ->
        if (node.chVal != null && !node.isLoop && !canEscapeRefined(coords, lines, GRAPH)) {
            nonEscapable.add(coords)
            counterB += 1
        }
    }


    // Visualization
    for (y in 0..2 * lines.lastIndex) {
        for (x in 0..2 * lines[0].lastIndex) {
            val coord = Pair(x, y)
            val node = GRAPH[coord]!!
                if (node.isBlocking) {
                print('█')
            } else if (node.chVal != null) {
                print('*')
            } else{
                print('.')
            }
        }
        print('\n')
    }

    println(counterB)
}

fun inBounds(coords: Pair<Int, Int>, lines: List<String>): Boolean {
    return coords.first >= 0 && coords.first <= 2 * lines[0].lastIndex
            && coords.second >= 0 && coords.second <= 2 * lines.lastIndex
}

fun canEscapeRefined(
    initial: Pair<Int, Int>,
    lines: List<String>,
    GRAPH: HashMap<Pair<Int, Int>, Node10>

): Boolean {
    val seen = hashSetOf<Pair<Int, Int>>()
    val stack = mutableListOf(initial)
    while (stack.isNotEmpty()) {
        val current = stack.removeLast()

        if (seen.contains(current)) {
            continue
        }

        seen.add(current)

        if (!inBounds(current, lines)) {
            if (initial == Pair(6,28)) {
                println(current)
            }
            return true
        }

        if (GRAPH[current]!!.isBlocking) {
            continue
        }

        val newNeighbors = mutableListOf(
            Pair(current.first - 1, current.second),
            Pair(current.first + 1, current.second),
            Pair(current.first, current.second - 1),
            Pair(current.first, current.second + 1)
        )
        stack.addAll(newNeighbors)
    }
    return false
}


// OLD CODE ---------------------------------

/**
 * My first attempt at B. This did not account for going between pipes
 */
fun naiveSolveB(lines: List<String>, BOARD: HashMap<Pair<Int, Int>, Char>, loop: HashMap<Pair<Int, Int>, Int>) {
    var counterB = 0
    val trapped = hashSetOf<Pair<Int, Int>>()


    for (key in BOARD.keys) {
        if (!loop.containsKey(key)) {
            if (!canEscape(key, loop, lines[0].lastIndex, lines.lastIndex)) {
                trapped.add(key)
                counterB += 1
            }
        }
    }

    // For visualizing surrounded nodes
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            val key = Pair(x, y)
            if (trapped.contains(key)) {
                print('*')
            } else if (loop.contains(key)) {
                print('█')
            } else {
                print(BOARD[key])
            }
        }
        print('\n')
    }
    println(counterB)
}

fun canEscape(
    current: Pair<Int, Int>,
    walls: HashMap<Pair<Int, Int>, Int>,
    maxX: Int,
    maxY: Int
): Boolean {
    val seen = hashSetOf<Pair<Int, Int>>()
    val stack = mutableListOf(current)
    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        if (seen.contains(current)) {
            continue
        }

        seen.add(current)
        if (current.first < 0 || current.second < 0 || current.first > maxX || current.second > maxY) {
            return true
        }

        val newNeighbors = mutableListOf(
            Pair(current.first - 1, current.second),
            Pair(current.first + 1, current.second),
            Pair(current.first, current.second - 1),
            Pair(current.first, current.second + 1)
        )
        stack.addAll(newNeighbors.filter { !walls.containsKey(it) })
    }
    return false
}