import java.io.File

fun main() {
    val empty = ValleyState(up = false, down = false, left = false, right = false)

    val board = HashMap<Triple<Int, Int, Int>, ValleyState>()
    val seen = HashSet<Triple<Int, Int, Int>>()
    val lines = File("src/input24.txt").readLines()
    val height = lines.size
    val width = lines[0].length
    for (i in lines.indices) {
        for (j in lines[0].indices) {
            board[Triple(i, j, 0)] = when (lines[i][j]) {
                '.' -> empty.copy()
                '<' -> empty.copy(left = true)
                '>' -> empty.copy(right = true)
                '^' -> empty.copy(up = true)
                'v' -> empty.copy(down = true)
                else -> throw Exception("wtf is this input")
            }
        }
    }

    val lcm = 600

    for (time in 1..lcm - 1) {
        for (i in lines.indices) {
            for (j in lines[0].indices) {
                board[Triple(i, j, time)] = empty.copy()
                if (board[Triple(i, Math.floorMod(j - 1, width), time - 1)]!!.right) {
                    board[Triple(i, j, time)]!!.right = true
                }
                if (board[Triple(i, Math.floorMod(j + 1, width), time - 1)]!!.left) {
                    board[Triple(i, j, time)]!!.left = true
                }
                if (board[Triple(Math.floorMod(i + 1, height), j, time - 1)]!!.up) {
                    board[Triple(i, j, time)]!!.up = true
                }
                if (board[Triple(Math.floorMod(i - 1, height), j, time - 1)]!!.down) {
                    board[Triple(i, j, time)]!!.down = true
                }
            }
        }
    }

    // (25, 120, 288)
    // (-1, -1, 571)
    // (25, 120, 261) - 861
    val entrance = Triple(-1, -1, 571)

    var counter = -1
    var frontier = HashSet<Triple<Int, Int, Int>>()
    frontier.add(entrance)
    while (frontier.isNotEmpty()) {
        counter += 1
        println("${counter}, ${frontier.size}")
        val newFrontier = HashSet<Triple<Int, Int, Int>>()
        for (triple in frontier) {
            if (triple in seen) {
                continue
            }
            seen.add(triple)
            if (triple.first < 0 ||
                triple.first > 24 ||
                triple.second < 0 ||
                triple.second > 119 ||
                board[triple]!!.isBlocked()
            ) {
                if ((triple.first == -1 && triple.second == -1) ||
                    (triple.first == 25 && triple.second == 120)  ) {
                    // We good
                } else {
                    continue
                }
            }


            if (triple.first == 25 && triple.second == 120) {
                println(triple)
                return
            }

            newFrontier.addAll(getAdj(triple))
        }
        frontier = newFrontier
    }
    println("rip")
}

fun runBFS(){

}

fun getAdj(triple: Triple<Int, Int, Int>): HashSet<Triple<Int, Int, Int>> {
    val adj = HashSet<Triple<Int, Int, Int>>()
    val adjTime = (triple.third + 1) % 600
    adj.add(Triple(triple.first, triple.second, adjTime))
    if (triple.first == -1 && triple.second == -1) {
        adj.add(Triple(0, 0, adjTime))
    }
    if (triple.first == 0 && triple.second == 0) {
        adj.add(Triple(-1, -1, adjTime))
    }
    if (triple.first == 24 && triple.second == 119) {
        adj.add(Triple(25, 120, adjTime))
    }
    if (triple.first == 25 && triple.second == 120) {
        adj.add(Triple(24, 119, adjTime))
    }
    adj.add(Triple(triple.first + 1, triple.second, adjTime))
    adj.add(Triple(triple.first - 1, triple.second, adjTime))
    adj.add(Triple(triple.first, triple.second + 1, adjTime))
    adj.add(Triple(triple.first, triple.second - 1, adjTime))
    return adj
}

data class ValleyState(var up: Boolean, var down: Boolean, var left: Boolean, var right: Boolean) {
    fun isBlocked(): Boolean {
        return up || down || left || right
    }
}
