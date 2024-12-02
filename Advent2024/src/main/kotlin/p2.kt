import java.io.File
import kotlin.math.absoluteValue

fun main() {
    var counterA = 0
    var counterB = 0
    File("inputs/input2.txt").forEachLine {
        line ->
        val levels = line.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        if (safe(levels)) {
            counterA += 1
            counterB += 1
            return@forEachLine
        }

        for (i in levels.indices) {
            val newLevels = levels.toMutableList()
            newLevels.removeAt(i)
            if (safe(newLevels)) {
                counterB += 1
                return@forEachLine
            }
        }
    }
    println("Counter A: $counterA")
    println("Counter B: $counterB")
}

fun safe(levels: List<Int>): Boolean {
    val diffs = mutableListOf<Int>()
    for (i in 0..levels.size - 2) {
        diffs.add(levels[i] - levels[i+1])
    }

    val isDecreasing = diffs.none { it >= 0 }
    val isIncreasing = diffs.none { it <= 0 }
    assert(diffs.isNotEmpty())
    val absValCondition = diffs.maxOf { it.absoluteValue } <= 3 && diffs.minOf { it.absoluteValue } >= 1
    return (isIncreasing || isDecreasing) && absValCondition
}