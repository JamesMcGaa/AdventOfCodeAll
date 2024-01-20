import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    val board = hashMapOf<Pair<Int, Int>, String>()
    File("inputs/input14.txt").forEachLine {
        val listOfPairs = it.split("->").map { it.split(',').map { Integer.parseInt(it.trim()) } }
        for (i in 0 until listOfPairs.size - 1) {
            val x1 = listOfPairs[i][0]
            val y1 = listOfPairs[i][1]
            val x2 = listOfPairs[i + 1][0]
            val y2 = listOfPairs[i + 1][1]

            if (x1 == x2) {
                for (y in min(y1, y2)..max(y1, y2)) {
                    board[Pair(x1, y)] = "#"
                }
            } else {
                for (x in min(x1, x2)..max(x1, x2)) {
                    board[Pair(x, y1)] = "#"
                }
            }
        }
    }
    val yMax = board.keys.map { key -> key.second }.max()

    for (x in 500 - 2 * yMax..500 + yMax * 2) {
        board[Pair(x, yMax + 2)] = "#"
    }

    var comeToRestCounter = 0
    while (true) {
        var sand = Pair(500, 0)
        while (
            !board.contains(Pair(sand.first, sand.second + 1)) ||
            !board.contains(Pair(sand.first - 1, sand.second + 1)) ||
            !board.contains(Pair(sand.first + 1, sand.second + 1))
        ) {
            sand = if (!board.contains(Pair(sand.first, sand.second + 1))) {
                Pair(sand.first, sand.second + 1)
            } else if (!board.contains(Pair(sand.first - 1, sand.second + 1))) {
                Pair(sand.first - 1, sand.second + 1)
            } else {
                Pair(sand.first + 1, sand.second + 1)
            }
//            if (sand.second > yMax) {
//                printBoard(board)
//                println(comeToRestCounter)
//                return
//            }
        }
        board[sand] = "O"
        comeToRestCounter += 1
        if (sand == Pair(500, 0)) {
            printBoard(board)
            println(comeToRestCounter)
            return
        }
    }
}

fun printBoard(board: HashMap<Pair<Int, Int>, String>) {
    val xMin = board.keys.map { key -> key.first }.min()
    val xMax = board.keys.map { key -> key.first }.max()
    val yMin = board.keys.map { key -> key.second }.min()
    val yMax = board.keys.map { key -> key.second }.max()
    for (y in yMin..yMax) {
        var row = ""
        for (x in xMin..xMax) {
            row += board.getOrDefault(Pair(x, y), ".")
        }
        println(row)
    }
}

 
