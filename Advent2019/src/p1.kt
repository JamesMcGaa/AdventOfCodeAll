import java.io.File

fun main() {
    var counterA = 0
    var counterB = 0
    File(   "inputs/input1.txt").forEachLine {
        val start = it.toInt()
        counterA += fuel(start, partBEnabled = false)
        counterB += fuel(start, partBEnabled = true)
    }
    println("Counter A: $counterA")
    println("Counter B: $counterB")
}

fun fuel(start: Int, partBEnabled: Boolean): Int {
    val additional = (start / 3) - 2
    if (additional <= 0) {
        return 0
    }

    if (partBEnabled) {
        return additional + fuel(additional, partBEnabled = true)
    }
    return additional
}