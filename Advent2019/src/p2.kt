import java.io.File
import kotlin.time.measureTimedValue

fun main() {
    val timedA = measureTimedValue {
        executeIntcode(12, 2)
    }
    val timedB = measureTimedValue {
        for (a in 0..99) {
            for (b in 0 .. 99) {
                if (executeIntcode(a,b) ==19690720) {
                    return@measureTimedValue 100*a + b
                }
            }
        }
    }
    println("Part A: $timedA")
    println("Part B: $timedB")
}

fun executeIntcode(a: Int, b: Int): Int {
    val grid = File("inputs/input2.txt").readLines()[0].split(",").map { it.toInt() }.toMutableList()
    var pointer = 0
    grid[1] = a
    grid[2] = b
    while (true) {
        when (grid[pointer]) {
            1 -> {
                grid[grid[pointer + 3]] = grid[grid[pointer + 1]] + grid[grid[pointer + 2]]
            }

            2 -> {
                grid[grid[pointer + 3]] = grid[grid[pointer + 1]] * grid[grid[pointer + 2]]
            }
            99 -> {
                break
            }
            else -> throw Exception("Illegal opcode")
        }
        pointer += 4
    }
    return grid[0]
}


/**
 * Relic from trying to solve this for general a,b
 *
 * It pays to read the problem...
 *
 * Note this would still have issues, as arbitrary a,b could grow exponentially in a,b
 * as well as overwrite operators
 */
@Suppress("unused")
data class ABVal(
    val a: Int,
    val b: Int,
    val raw: Int,
) {
    operator fun plus(other: ABVal): ABVal {
        return ABVal(this.a + other.a, this.b + other.b, this.raw + other.raw)
    }
}
