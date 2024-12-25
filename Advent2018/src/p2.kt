import java.io.File

fun <T> Iterable<T>.freqCount(): Map<T, Int> {
    return this.groupingBy { it }.eachCount()
}

fun main() {
    val input = File("inputs/input2.txt").readLines()
    val counts = input.map { it.toList().freqCount() }
    val twos = counts.count { it.containsValue(2) }
    val threes = counts.count { it.containsValue(3) }
    println("Part A: ${twos * threes}")

    val seen = mutableSetOf<String>()
    input.forEach { line ->
        line.forEachIndexed { index, ch ->
            val match = line.replaceRange(index, index + 1, "*")
            if (match in seen) {
                println("Part B: ${match.replace("*", "")}")
                return
            }
            seen.add(match)
        }
    }
}