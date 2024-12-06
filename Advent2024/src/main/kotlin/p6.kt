import java.io.File

val INPUT = File("inputs/input6.txt").readLines()
val R: Int = INPUT.size
val C: Int = INPUT[0].length

fun main() {

    println("Part A: ${run(flippedPair = null)}")

    var counterB = 0
    for (i in 0..<R) {
        for (j in 0..<C) {
            if (run(Pair(i,j)) == INFINITE_LOOP) {
                counterB += 1
            }
        }
    }

    println("Part B: $counterB")
}

const val INFINITE_LOOP = -1
const val FLIPPED_GUARD = -2

/**
 * Calculates the number of seen guard spots for a given input
 *
 * Optionally flips the pair given by flippedPair
 *
 * Optionally prints out the grid after traversal
 *
 * Returns the number of seen spots, -1 if an infinite loop is found,
 *  or -2 if flippedPair goes on top of a guard or obstacle
 */
fun run(flippedPair: Pair<Int, Int>?, debugFlag: Boolean = false): Int {
    val obstacles = mutableSetOf<Pair<Int, Int>>()
    val seen = mutableSetOf<Pair<Int, Int>>()
    val guardHistory = mutableSetOf<Guard>()
    val input = File("inputs/input6.txt").readLines()
    lateinit var guard: Guard

    input.forEachIndexed { r, row ->
        row.forEachIndexed { c, ch ->
            if (ch == '#') {
                obstacles.add(Pair(r, c))
            }
            if (ch == '^') {
                guard = Guard(r, c, Direction.UP)
                seen.add(Pair(r,c))
                guardHistory.add(guard)
            }
        }
    }

    if (flippedPair != null) {
        if (flippedPair in obstacles || flippedPair == guard.pos) {
            return FLIPPED_GUARD
        }
        obstacles.add(flippedPair)
    }

    while (guard.r in 0..<R && guard.c in 0..<C) {
        val proj = guard.project().pos
        if (proj in obstacles) {
            guard = guard.rotate()
        } else {
            seen.add(guard.project().pos)
            guard = guard.project()
        }
        if (guard in guardHistory) {
            return INFINITE_LOOP
        } else {
            guardHistory.add(guard)
        }
    }

    if (debugFlag) {
        for (i in 0..<R) {
            for (j in 0..<C) {
                val pair = Pair(i,j)
                when (pair) {
                    in obstacles -> {
                        print("#")
                    }
                    in seen -> {
                        print("X")
                    }
                    else -> {
                        print(".")
                    }
                }
            }
            print("\n")
        }
    }

    // Offset by -1 to remove out of bounds final move
    return seen.size - 1
}

data class Guard(
    val r: Int,
    val c: Int,
    val dir: Direction
) {
    val pos = Pair(r, c)

    fun project(): Guard {
        return when (dir) {
            Direction.UP -> copy(r = r-1)
            Direction.DOWN -> copy(r = r+1)
            Direction.RIGHT -> copy(c = c+1)
            Direction.LEFT -> copy(c = c-1)
        }
    }

    fun rotate(): Guard {
        return when (dir) {
            Direction.UP -> copy(dir = Direction.RIGHT)
            Direction.DOWN -> copy(dir = Direction.LEFT)
            Direction.RIGHT -> copy(dir = Direction.DOWN)
            Direction.LEFT -> copy(dir = Direction.UP)
        }
    }
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}