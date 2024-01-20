import java.io.File


enum class LaserDirection {
    UP, DOWN, LEFT, RIGHT
}

data class LaserTile(
    val x: Int,
    val y: Int,
    val type: Char,
    val incomingLasers: MutableSet<LaserDirection>
) {
    fun outgoingLasers(): MutableSet<LaserDirection> {
        return when (type) {
            '.' -> incomingLasers.toMutableSet()

            '/' -> {
                val outgoingSet = mutableSetOf<LaserDirection>()
                for (laser in incomingLasers) {
                    when (laser) {
                        LaserDirection.DOWN -> outgoingSet.add(LaserDirection.LEFT)
                        LaserDirection.UP -> outgoingSet.add(LaserDirection.RIGHT)
                        LaserDirection.RIGHT -> outgoingSet.add(LaserDirection.UP)
                        LaserDirection.LEFT -> outgoingSet.add(LaserDirection.DOWN)
                    }
                }
                outgoingSet
            }

            '\\' -> {
                val outgoingSet = mutableSetOf<LaserDirection>()
                for (laser in incomingLasers) {
                    when (laser) {
                        LaserDirection.DOWN -> outgoingSet.add(LaserDirection.RIGHT)
                        LaserDirection.UP -> outgoingSet.add(LaserDirection.LEFT)
                        LaserDirection.RIGHT -> outgoingSet.add(LaserDirection.DOWN)
                        LaserDirection.LEFT -> outgoingSet.add(LaserDirection.UP)
                    }
                }
                outgoingSet
            }

            '|' -> {
                val outgoingSet = mutableSetOf<LaserDirection>()
                for (laser in incomingLasers) {
                    when (laser) {
                        LaserDirection.DOWN, LaserDirection.UP ->  {
                            outgoingSet.add(laser)
                        }
                        LaserDirection.RIGHT,LaserDirection.LEFT  ->  {
                            outgoingSet.add(LaserDirection.UP)
                            outgoingSet.add(LaserDirection.DOWN)
                        }
                    }
                }
                outgoingSet
            }

            '-' -> {
                val outgoingSet = mutableSetOf<LaserDirection>()
                for (laser in incomingLasers) {
                    when (laser) {
                        LaserDirection.DOWN, LaserDirection.UP ->  {
                            outgoingSet.add(LaserDirection.LEFT)
                            outgoingSet.add(LaserDirection.RIGHT)
                        }
                        LaserDirection.RIGHT,LaserDirection.LEFT  ->  {
                            outgoingSet.add(laser)
                        }
                    }
                }
                outgoingSet
            }

            else -> throw Exception("Unexpected piece")
        }
    }
}

fun tryConfig(startPoint: Pair<Int, Int>, startOrient: LaserDirection, board: List<String>): Int {
    val grid = hashMapOf<Pair<Int, Int>, LaserTile>()
    for (i in board.indices) {
        for (j in board[i].indices) {
            grid[Pair(i, j)] = LaserTile(i, j, board[i][j], mutableSetOf())
        }
    }
    grid[startPoint]!!.incomingLasers.add(startOrient)

    var prevLaserCount = -1
    var currentLaserCount = 1
    while(currentLaserCount != prevLaserCount) {
        prevLaserCount = currentLaserCount

        for (tile in grid.values) {
            for (laser in tile.outgoingLasers()) {
                when (laser) {
                    LaserDirection.DOWN -> grid[Pair(tile.x + 1, tile.y)]?.incomingLasers?.add(laser)
                    LaserDirection.UP -> grid[Pair(tile.x - 1, tile.y)]?.incomingLasers?.add(laser)
                    LaserDirection.RIGHT -> grid[Pair(tile.x, tile.y + 1)]?.incomingLasers?.add(laser)
                    LaserDirection.LEFT -> grid[Pair(tile.x, tile.y - 1)]?.incomingLasers?.add(laser)
                }
            }
        }


        currentLaserCount = grid.values.map { it.incomingLasers.size }.sum()
    }

    return grid.values.filter {it.incomingLasers.isNotEmpty()}.size
}

fun main() {
    val board = File("inputs/input16.txt").readLines()

    println(tryConfig(Pair(0,0), LaserDirection.RIGHT, board))

    val results = mutableListOf<Int>()
    for (i in board.indices) {
        results.add(tryConfig(Pair(i, 0), LaserDirection.RIGHT, board))
        results.add(tryConfig(Pair(i, board[0].lastIndex), LaserDirection.LEFT, board))
    }
    for (j in board[0].indices) {
        results.add(tryConfig(Pair(0, j), LaserDirection.DOWN, board))
        results.add(tryConfig(Pair(board.lastIndex, j), LaserDirection.UP, board))
    }
    println(results.max())
}

/**
 * - Others used complex numbers
 *
 * - Others used DFS with tricks to avoid loops, keep track of direction
 *
 * This said I am happy with my solution, it is a slower brute force but it makes more sense IMO
 *
 * To optimize this I could avoid iterating over the tile unless it has received a new incoming laser - I could
 * keep track of this with a set somehow
 */
