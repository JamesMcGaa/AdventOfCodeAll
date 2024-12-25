import java.io.File

fun main() {
    val input = File("inputs/input1.txt").readLines()
    val partA = input.sumOf { it.removePrefix("+").toInt() }
    println("Part A: $partA")

    var current = 0
    val seen = mutableSetOf(current)
    var i = 0
    while (true) {
        current += input[i].removePrefix("+").toInt()
        if (current in seen) {
            println("Part B: $current")
            return
        }
        seen.add(current)
        i = (i + 1) % input.size
    }
}