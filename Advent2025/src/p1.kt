import java.io.File
import kotlin.math.abs

fun main() {
    println("Part A: ${p1a("inputs/input1.txt")}")
    println("Part B: ${p1b("inputs/input1.txt")}")
}

fun p1a(filename: String): Long {
    val input = File(filename).readLines().map {
        val direction = if (it.first() == 'R') 1L else -1L
        direction * it.substring(1).toLong()
    }

    var counter = 0L
    var pointer = 50L
    for (amount in input) {
        pointer = Math.floorMod(pointer + amount, 100L)
        if (pointer == 0L) {
            counter++
        }
    }

    return counter
}

fun p1b(filename: String): Long {
    val input = File(filename).readLines().map {
        val direction = if (it.first() == 'R') 1L else -1L
        direction * it.substring(1).toLong()
    }

    var counter = 0L
    var pointer = 50L
    for (amount in input) {
        val modAmount = Math.floorMod(abs(amount), 100L)
        if (amount > 0L) {
            if (pointer != 0L && pointer + modAmount >= 100L) {
                counter++
            }
        } else {
            if (pointer != 0L && pointer - modAmount <= 0L) {
                counter++
            }
        }
        counter += abs(amount) / 100L
        pointer = Math.floorMod(pointer + amount, 100L)
    }

    return counter
}