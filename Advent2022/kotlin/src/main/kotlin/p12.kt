import java.io.File
import kotlin.Exception

fun main(args: Array<String>) {
    val seen = HashSet<Pair<Int, Int>>()
    val board = HashMap<Pair<Int, Int>, Int>()
    val lines = File("inputs/input12.txt").readLines()
    var frontier = ArrayList<Pair<Int, Int>>()
    var exit: Pair<Int, Int> = Pair(-1,-1)
    for (i in lines.indices) {
        for (j in 0 until lines[i].length) {
            board[Pair(i, j)] = when (lines[i][j]) {
                'S', 'a' -> {
                    seen.add(Pair(i,j))
                    frontier.add(Pair(i, j))
                    0
                }

                'E' -> {
                    exit = Pair(i, j)
                    25
                }

                else -> lines[i][j].code - 'a'.code
            }
        }
    }

    var counter = 0
    while (true) {
        var newFrontier = ArrayList<Pair<Int, Int>>()
        while (frontier.isNotEmpty()) {
            val pair = frontier.removeLast()
            if (pair == exit!!) {
                println(counter)
                return
            }
            val newPairsToTry = listOf(
                Pair(pair.first + 1, pair.second),
                Pair(pair.first - 1, pair.second),
                Pair(pair.first, pair.second + 1),
                Pair(pair.first, pair.second - 1),
            )
            newPairsToTry.forEach { newPair ->
                if (!seen.contains(newPair) && newPair.first >= 0 && newPair.second >= 0 && newPair.first < lines.size && newPair.second < lines[0].length && board[newPair]!! - board[pair]!! <= 1) {
                    newFrontier.add(newPair)
                    seen.add(newPair)
                }
            }
        }
        counter += 1
        frontier = newFrontier
    }
}


