import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.roundToLong

fun main() {
    println("Part A: ${solve(isPartB = false)}")
    println("Part B: ${solve(isPartB = true)}")
}

fun solve(isPartB: Boolean = false): Long {
    var counter = 0L
    val inputs = File("inputs/input13.txt").readLines()
    val offset = if (isPartB) 10000000000000L else 0L
    for (blockIdx in 0..inputs.size / 4) {
        val idx = blockIdx * 4
        val a1 = inputs[idx].filter { it.isDigit() || it == ',' }.split(',')[0].toLong()
        val a2 = inputs[idx].filter { it.isDigit() || it == ',' }.split(',')[1].toLong()
        val b1 = inputs[idx + 1].filter { it.isDigit() || it == ',' }.split(',')[0].toLong()
        val b2 = inputs[idx + 1].filter { it.isDigit() || it == ',' }.split(',')[1].toLong()
        val c1 = inputs[idx + 2].filter { it.isDigit() || it == ',' }.split(',')[0].toLong() + offset
        val c2 = inputs[idx + 2].filter { it.isDigit() || it == ',' }.split(',')[1].toLong() + offset

        val det = (a1 * b2 - b1 * a2)
        if (det == 0L) {
            continue
        }

        val x = (c1 * b2 - b1 * c2).toDouble() / (a1 * b2 - b1 * a2)
        val y = (a1 * c2 - c1 * a2).toDouble() / (a1 * b2 - b1 * a2)
        val maxPresses = if (isPartB) Long.MAX_VALUE else 100L
        if (x.isWholeNumber() && y.isWholeNumber() && x.roundToLong() > 0L && y.roundToLong() >= 0L && x.roundToLong() <= maxPresses && y.roundToLong() <= maxPresses) {
            counter += 3L * x.roundToLong() + y.roundToLong()
        }
    }
    return counter
}

fun Double.isWholeNumber(): Boolean {
    return (this.roundToLong() - this).absoluteValue < .00001
}