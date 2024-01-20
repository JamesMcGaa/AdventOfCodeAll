import java.io.File

fun main() {
    val board = Board(File("inputs/input14.txt").readLines())
    board.solveA()
    board.solveB()
}

data class Board(
    val input: List<String>,
) {
    lateinit var board: MutableList<MutableList<Char>>

    init {
        resetBoard()
    }

    fun resetBoard() {
        board = input.map { it.map { ch -> ch }.toMutableList() }.toMutableList()
    }

    val signature
        get() = board.flatten().joinToString("")

    fun move() {
        var flipped = true
        while (flipped) {
            flipped = false
            for (i in board.indices) {
                for (j in board[i].indices) {
                    if (board[i][j] == ROCK && inBounds(i - 1, j) && board[i - 1][j] == EMPTY) {
                        board[i - 1][j] = ROCK
                        board[i][j] = EMPTY
                        flipped = true
                    }
                }
            }
        }
    }

    fun solveB() {
        resetBoard()
        val iterations = 1000000000
        val seen = hashMapOf(signature to 0)
        var cycles = 0
        while (true) {
            cycles += 1
            cycle()
            if (seen.contains(signature)) {
                val offset = seen[signature]!!
                val cycleLen = cycles - seen[signature]!!
                val cyclesTbd = (iterations - offset) % cycleLen
                repeat(cyclesTbd) {
                    cycle()
                }
                printWeight()
                return
            }
            seen[signature] = cycles
        }
    }

    fun cycle() {
        val offsets = listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))
        offsets.forEach { offset ->
            var flipped = true
            while (flipped) {
                flipped = false
                for (i in board.indices) {
                    for (j in board[i].indices) {
                        if (board[i][j] == ROCK && inBounds(
                                i + offset.first,
                                j + offset.second
                            ) && board[i + offset.first][j + offset.second] == EMPTY
                        ) {
                            board[i + offset.first][j + offset.second] = ROCK
                            board[i][j] = EMPTY
                            flipped = true
                        }
                    }
                }
            }
        }

    }

    fun printBoard() {
        println(" ")
        for (line in board) {
            println(line.joinToString(""))
        }
        println(" ")
        println(signature)
    }

    fun inBounds(i: Int, j: Int): Boolean {
        return board.indices.contains(i) && board[i].indices.contains(j)
    }

    fun printWeight() {
        var counter = 0
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == ROCK) {
                    counter += board.size - i
                }
            }
        }
        println(counter)
    }


    fun solveA() {
        resetBoard()
        move()
        printWeight()
    }

    companion object {
        const val ROCK = 'O'
        const val FIXED = '#'
        const val EMPTY = '.'
    }
}


