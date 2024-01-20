import java.io.File

fun main() {
    var board = HashMap<Pair<Int, Int>, String>()
    val lines = File("src/input23.txt").readLines()

    for (i in lines.indices) {
        for (j in lines[i].indices) {
            val char = lines[i][j]
            if (char == '#') {
                board[Pair(i, j)] = "#"
            }
        }
    }

    val simulator = DirectionSimulator()
    var counter = 0
    while (true) {
        counter += 1
        println(counter)
        val oldBoard = HashMap(board)
        board = simulate(simulator, board)
        if (oldBoard == board) {
            return
        }
    }
//    printBoard(board)

    val height = board.keys.maxBy { it.first }!!.first - board.keys.minBy { it.first }!!.first + 1
    val width = board.keys.maxBy { it.second }!!.second - board.keys.minBy { it.second }!!.second + 1
    println(height * width - board.size)
}

fun noMove(
    pair: MutableMap.MutableEntry<Pair<Int, Int>, String>,
    board: HashMap<Pair<Int, Int>, String>
): Boolean {
    val adj = hashSetOf(
        Pair(pair.key.first - 1, pair.key.second - 1),
        Pair(pair.key.first - 1, pair.key.second),
        Pair(pair.key.first - 1, pair.key.second + 1),
        Pair(pair.key.first + 1, pair.key.second - 1),
        Pair(pair.key.first + 1, pair.key.second),
        Pair(pair.key.first + 1, pair.key.second + 1),
        Pair(pair.key.first, pair.key.second - 1),
        Pair(pair.key.first, pair.key.second + 1)
    )
    if ((board.keys intersect adj).isEmpty()) {
        return true
    }
    return false
}

fun mutateNorth(
    pair: MutableMap.MutableEntry<Pair<Int, Int>, String>,
    board: HashMap<Pair<Int, Int>, String>
): Boolean {
    val norths = hashSetOf(
        Pair(pair.key.first - 1, pair.key.second - 1),
        Pair(pair.key.first - 1, pair.key.second),
        Pair(pair.key.first - 1, pair.key.second + 1)
    )
    if ((board.keys intersect norths).isEmpty()) {
        board[pair.key] = "n"
        return true
    }
    return false
}

fun mutateSouth(
    pair: MutableMap.MutableEntry<Pair<Int, Int>, String>,
    board: HashMap<Pair<Int, Int>, String>
): Boolean {
    val souths = hashSetOf(
        Pair(pair.key.first + 1, pair.key.second - 1),
        Pair(pair.key.first + 1, pair.key.second),
        Pair(pair.key.first + 1, pair.key.second + 1)
    )
    if ((board.keys intersect souths).isEmpty()) {
        board[pair.key] = "s"
        return true
    }
    return false
}


fun mutateWest(pair: MutableMap.MutableEntry<Pair<Int, Int>, String>, board: HashMap<Pair<Int, Int>, String>): Boolean {
    val wests = hashSetOf(
        Pair(pair.key.first - 1, pair.key.second - 1),
        Pair(pair.key.first, pair.key.second - 1),
        Pair(pair.key.first + 1, pair.key.second - 1)
    )
    if ((board.keys intersect wests).isEmpty()) {
        board[pair.key] = "w"
        return true
    }
    return false
}


fun mutateEast(pair: MutableMap.MutableEntry<Pair<Int, Int>, String>, board: HashMap<Pair<Int, Int>, String>): Boolean {
    val easts = hashSetOf(
        Pair(pair.key.first - 1, pair.key.second + 1),
        Pair(pair.key.first, pair.key.second + 1),
        Pair(pair.key.first + 1, pair.key.second + 1)
    )
    if ((board.keys intersect easts).isEmpty()) {
        board[pair.key] = "e"
        return true
    }
    return false
}

class DirectionSimulator() {
    private val order = mutableListOf("n", "s", "w", "e")

    fun simulate(pair: MutableMap.MutableEntry<Pair<Int, Int>, String>, board: HashMap<Pair<Int, Int>, String>) {
        if (noMove(pair, board) ||
            execute(order[0], pair, board) ||
            execute(order[1], pair, board) ||
            execute(order[2], pair, board) ||
            execute(order[3], pair, board)
                ) {
            return
        }
        else {
            return
        }
    }

    fun cycle() {
        order.add(order.removeFirst())
    }

    private fun execute(
        direction: String,
        pair: MutableMap.MutableEntry<Pair<Int, Int>, String>,
        board: HashMap<Pair<Int, Int>, String>
    ): Boolean {
        return when (direction) {
            "n" -> mutateNorth(pair, board)
            "s" -> mutateSouth(pair, board)
            "w" -> mutateWest(pair, board)
            "e" -> mutateEast(pair, board)
            else -> false
        }
    }
}


fun simulate(simulator: DirectionSimulator, board: HashMap<Pair<Int, Int>, String>): HashMap<Pair<Int, Int>, String> {
    for (pair in board) {
        simulator.simulate(pair, board)
    }
    simulator.cycle()
//    printBoard(board)

    val postMove = HashMap<Pair<Int, Int>, String>()
    for (pair in board) {
        when (pair.value) {
            "n" -> {
                val up = Pair(pair.key.first - 1, pair.key.second)
                if (board[Pair(up.first - 1, up.second)] != "s" &&
                    board[Pair(up.first, up.second - 1)] != "e" &&
                    board[Pair(up.first, up.second + 1)] != "w"
                ) {
                    postMove[up] = "#"
                } else {
                    postMove[pair.key] = "#"
                }
            }
            "s" -> {
                val down = Pair(pair.key.first + 1, pair.key.second)
                if (board[Pair(down.first + 1, down.second)] != "n" &&
                    board[Pair(down.first, down.second - 1)] != "e" &&
                    board[Pair(down.first, down.second + 1)] != "w"
                ) {
                    postMove[down] = "#"
                } else {
                    postMove[pair.key] = "#"
                }
            }
            "w" -> {
                val west = Pair(pair.key.first, pair.key.second - 1)
                if (board[Pair(west.first + 1, west.second)] != "n" &&
                    board[Pair(west.first, west.second - 1)] != "e" &&
                    board[Pair(west.first - 1, west.second)] != "s"
                ) {
                    postMove[west] = "#"
                } else {
                    postMove[pair.key] = "#"
                }
            }
            "e" -> {
                val east = Pair(pair.key.first, pair.key.second + 1)
                if (board[Pair(east.first + 1, east.second)] != "n" &&
                    board[Pair(east.first, east.second + 1)] != "w" &&
                    board[Pair(east.first - 1, east.second)] != "s"
                ) {
                    postMove[east] = "#"
                } else {
                    postMove[pair.key] = "#"
                }
            }
            "#" -> postMove[pair.key] = pair.value
            else -> {
            }
        }
    }

    return postMove
}

fun printBoard(board: HashMap<Pair<Int, Int>, String>) {
    for (i in board.keys.minBy { it.first }!!.first .. board.keys.maxBy { it.first }!!.first) {
        var buffer = ""
        for (j in board.keys.minBy { it.second }!!.second .. board.keys.maxBy { it.second }!!.second) {
            buffer += board.getOrDefault(Pair(i,j),".")
        }
        println(buffer)
    }
    println(" ")
}
