import java.io.File

/**
 * DP(x, y, entranceOrientation, streak)
 *
 * x, y in input bounds
 *
 * entranceOrientation - 4 values in up, down, left, right
 *
 * Streak in 1, 2, 3
 *
 * I can either have a START orientation, or encode the
 * start as streak of 0 with entrance right WLOG
 *
 * Max size of 141*141*4*3 + 1
 */

var MAX_STREAK = 3
var MIN_TURN_MOVEMENT = 0

data class State(
    val x: Int,
    val y: Int,
    val orientation: Orientation,
    val streak: Int,
) {
    fun legal(board: List<String>): Boolean {
        return board.indices.contains(x) && board[x].indices.contains(y) && streak <= MAX_STREAK // Part B change
    }
}

data class FullState(
    val state: State,
    val heatLossSoFar: Int
) {
    fun isLegalOrientationForNeighbor(orient: Orientation): Boolean {
        if (state.streak > 0 && state.streak < MIN_TURN_MOVEMENT) { // Part B change
            return orient == state.orientation
        }
        return orient != state.orientation.opposite() || state.streak == 0
    }

    fun getNeighbors(board: List<String>): List<FullState> {
        val neighbors = mutableListOf<FullState>()
        for (orient in Orientation.entries) {
            if (isLegalOrientationForNeighbor(orient)) {
                when (orient) {
                    Orientation.LEFT -> {
                        val newStreak = if (orient != state.orientation) 1 else state.streak + 1
                        val newState = state.copy(y = state.y - 1, orientation = orient, streak = newStreak)
                        if (newState.legal(board)) {
                            neighbors.add(
                                FullState(
                                    newState,
                                    heatLossSoFar + board[newState.x][newState.y].digitToInt()
                                )
                            )
                        }
                    }

                    Orientation.UP -> {
                        val newStreak = if (orient != state.orientation) 1 else state.streak + 1
                        val newState = state.copy(x = state.x - 1, orientation = orient, streak = newStreak)
                        if (newState.legal(board)) {
                            neighbors.add(
                                FullState(
                                    newState,
                                    heatLossSoFar + board[newState.x][newState.y].digitToInt()
                                )
                            )
                        }
                    }
                    Orientation.RIGHT -> {
                        val newStreak = if (orient != state.orientation) 1 else state.streak + 1
                        val newState = state.copy(y = state.y + 1, orientation = orient, streak = newStreak)
                        if (newState.legal(board)) {
                            neighbors.add(
                                FullState(
                                    newState,
                                    heatLossSoFar + board[newState.x][newState.y].digitToInt()
                                )
                            )
                        }
                    }
                    Orientation.DOWN -> {
                        val newStreak = if (orient != state.orientation) 1 else state.streak + 1
                        val newState = state.copy(x = state.x + 1, orientation = orient, streak = newStreak)
                        if (newState.legal(board)) {
                            neighbors.add(
                                FullState(
                                    newState,
                                    heatLossSoFar + board[newState.x][newState.y].digitToInt()
                                )
                            )
                        }
                    }
                }
            }
        }
        return neighbors
    }
}

enum class Orientation {
    UP, DOWN, LEFT, RIGHT;
}

fun Orientation.opposite(): Orientation {
    return when (this) {
        Orientation.DOWN -> Orientation.UP
        Orientation.UP -> Orientation.DOWN
        Orientation.LEFT -> Orientation.RIGHT
        Orientation.RIGHT -> Orientation.LEFT
    }
}

fun main() {
    val board = File("inputs/input17.txt").readLines()

    var isPartB = false
    repeat(2) {
        if (isPartB) {
            MAX_STREAK = 10
            MIN_TURN_MOVEMENT = 4
        }
        isPartB = true

        val startState = State(0, 0, Orientation.RIGHT, 0)
        val startHeat = 0
        val optimalMap = hashMapOf<State, Int>()
        val stack = mutableListOf(
            FullState(startState, startHeat)
        )

        var bestSoFar = 178929 // (141 * 141) * 9, basic upper bound on heat loss
        while (stack.isNotEmpty()) {

            val current = stack.removeLast()

            if (optimalMap.getOrDefault(current.state, bestSoFar) <= current.heatLossSoFar || bestSoFar <= current.heatLossSoFar) {
                continue
            }

            if (current.state.x == board.lastIndex && current.state.y == board[0].lastIndex && current.heatLossSoFar < bestSoFar) {
                bestSoFar = current.heatLossSoFar
            }

            optimalMap[current.state] = current.heatLossSoFar

            for (neighbor in current.getNeighbors(board)) {
                stack.add(neighbor)
            }
        }

        println(optimalMap.filterKeys { state -> state.x == board.lastIndex && state.y == board[state.x].lastIndex }.values.min())
    }
}

/**
 * - One person online used complex numbers to represent direction
 *
 * - Another based it on heat, see how many nodes we can hit with heat = 1, then heat += 1 until we find the exit, etc
 *
 * - Other users considered all moves between min -> max for that direction, then only handled left and right turns, not forwards
 */