import java.io.File
import kotlin.time.measureTimedValue


fun main() {
    // Used a hash map at first but testValue is not necessarily unique...
    val calibrationEquations = mutableListOf<Pair<Long, List<Long>>>()
    File("inputs/input7.txt").forEachLine { line ->
        val tokens = line.split(":")
        val testValue = tokens[0].toLong()
        val args = tokens[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        calibrationEquations.add(Pair(testValue, args))
    }

    val partATime = measureTimedValue {
        var counterA = 0L
        calibrationEquations.forEach {
            val testValue = it.first
            val args = it.second
            if (solve(args[0], 1, testValue, args)) {
                counterA += testValue
            }
        }
        return@measureTimedValue counterA
    }
    val partBTime = measureTimedValue {
        var counterB = 0L
        calibrationEquations.forEach {
            val testValue = it.first
            val args = it.second
            if (solve(args[0], 1, testValue, args, isPartBEnabled = true)) {
                counterB += testValue
            }
        }
        return@measureTimedValue counterB
    }
    println("Part A: $partATime")
    println("Part B: $partBTime")
}

fun solve(soFar: Long, idx: Int, testValue: Long, args: List<Long>, isPartBEnabled: Boolean = false): Boolean {
    if (idx == args.size) {
        return soFar == testValue
    }
    if (soFar > testValue) {
        return false
    }
    if (isPartBEnabled) {
        val concat = (soFar.toString() + args[idx].toString()).toLong()
        return solve(soFar * args[idx], idx + 1, testValue, args, true) ||
                solve(soFar + args[idx], idx + 1, testValue, args, true) ||
                solve(concat, idx + 1, testValue, args, true)
    } else {
        return solve(soFar * args[idx], idx + 1, testValue, args) ||
                solve(soFar + args[idx], idx + 1, testValue, args)
    }
}