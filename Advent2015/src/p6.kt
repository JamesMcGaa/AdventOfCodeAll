import java.io.File

fun main() {
    val board = Board()
    File("input/input6.txt").forEachLine { line ->
        val range = line
                .split(" ")
                .filter { it.contains(',') }
                .map {
                    it.split(',')
                            .map { Integer.parseInt(it) }
                }.flatten()
        val type = when (line[6]) {
            'f' -> Board.Companion.MODES.OFF
            ' ' -> Board.Companion.MODES.TOGGLE
            'n' -> Board.Companion.MODES.ON
            else -> throw Exception()
        }
        board.rangeQuery(range, type)
    }
    println(board.getCount())
    println(board.getCountB())
}

class Board() {
    private val board = HashMap<Pair<Int, Int>, Boolean>()
    private val boardB = HashMap<Pair<Int, Int>, Int>()

    init {
        for (i in 0..999) {
            for (j in 0..999) {
                board[Pair(i, j)] = false
                boardB[Pair(i, j)] = 0
            }
        }
    }

    fun getCount(): Int {
        var count = 0
        for (i in 0..999) {
            for (j in 0..999) {
                if (board[Pair(i, j)]!!) {
                    count += 1
                }
            }
        }
        return count
    }

    fun getCountB(): Int {
        return boardB.values.sum()
    }

    fun rangeQuery(range: List<Int>, type: MODES) {
        for (i in range[0]..range[2]) {
            for (j in range[1]..range[3]) {
                board[Pair(i, j)] = when (type) {
                    MODES.OFF -> false
                    MODES.ON -> true
                    MODES.TOGGLE -> !board[Pair(i, j)]!!
                }
                boardB[Pair(i, j)] = when (type) {
                    MODES.OFF -> (boardB[Pair(i, j)]!! - 1).coerceAtLeast(0)
                    MODES.ON -> boardB[Pair(i, j)]!! + 1
                    MODES.TOGGLE -> boardB[Pair(i, j)]!! + 2
                }
            }
        }
    }

    companion object {
        enum class MODES {
            TOGGLE, ON, OFF
        }
    }
}