import java.io.File
import kotlin.math.pow

fun main() {
    println("Part A: ${p3("inputs/input3.txt")}")
    println("Part B: ${p3("inputs/input3.txt", isPartB = true)}")
}

fun p3(filename: String, isPartB: Boolean = false): Long {
    return File(filename).readLines()
        .map { line ->
            line.toList().map { digit -> digit.digitToInt() }
        }
        .sumOf { battery ->
            if (isPartB) nJoltage(battery, 12) else joltage(battery)
        }
}

fun joltage(battery: List<Int>): Long {
    // Find the first (non endpoint) value
    val startIdx = battery.indexOf(battery.subList(0, battery.size - 1).max())
    val remainder = battery.subList(startIdx + 1, battery.size)
    val endIdx = remainder.indexOf(remainder.max()) + startIdx + 1
    return (10 * battery[startIdx] + battery[endIdx]).toLong()
}

fun nJoltage(battery: List<Int>, n: Int): Long {
    if (n == 1) {
        return battery.max().toLong()
    } else {
        val boundsForStart = battery.subList(0, battery.size - n + 1) // Leave room at the end
        val startIdx = battery.indexOf(boundsForStart.max())

        val remainder = battery.subList(startIdx + 1, battery.size)
        return battery[startIdx] * 10.0.pow(n - 1).toLong() + nJoltage(remainder, n - 1)
    }
}