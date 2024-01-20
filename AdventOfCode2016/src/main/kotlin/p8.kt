import java.io.File

val rows = 6
val cols = 50
//val rows = 3
//val cols = 7
fun main() {
    var board = mutableMapOf<Coord, Boolean>()
    for (i in 0..rows - 1) {
        for (j in 0..cols -1) {
            board[Coord(i, j)] = false
        }
    }

    File("inputs/input8.txt").forEachLine {
        val split = it.split(" ")
        val action = split[0]
        when (action) {
            "rect" -> {
                val dimensions = split[1].split("x").map { it.toInt() }
                for (i in 0..dimensions[1] - 1) {
                    for (j in 0..dimensions[0] - 1) {
                        board[Coord(i, j)] = true
                    }
                }
            }

            "rotate" -> {
                val rowOrCol = split[1]
                val rowOrColIdx = split[2].substring(split[2].indexOf("=") + 1).toInt()
                val amount = split[4].toInt()
                val newBoard = board.toMutableMap()
                when (rowOrCol) {
                    "row" -> {
                        for (j in 0..cols - 1) {
                            newBoard[Coord(rowOrColIdx, j)] =
                                board[Coord(rowOrColIdx, Math.floorMod(j - amount, cols))]!!
                        }
                    }
                    "column" -> {
                        for (i in 0..rows - 1) {
                            newBoard[Coord(i, rowOrColIdx)] =
                                board[Coord(Math.floorMod(i - amount, rows), rowOrColIdx)]!!
                        }
                    }
                }
                board = newBoard
            }
        }
    }

    println(board.values.filter {it}.size)
    printScreen(board) // Part B via visual inspection of stdout string / art
}

fun printScreen(board: MutableMap<Coord, Boolean>) {
    for (i in 0..rows - 1) {
        var cur = ""
        for (j in 0..cols -1) {
            cur += if (board[Coord(i, j)]!!) "#" else "."
        }
        println(cur)
    }
}


//rect 3x2
//rotate column x=1 by 1
//rotate row y=0 by 4
//rotate column x=1 by 1