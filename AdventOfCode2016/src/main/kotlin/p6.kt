import java.io.File

fun main() {
    val input = File("inputs/input6.txt").readLines()
    val cols = mutableMapOf<Int, String>()
    for (i in input[0].indices) {
        cols[i] = ""
        for (row in input.indices) {
            cols[i] = cols[i] + input[row][i]
        }
    }

    var freqStrA = ""
    var freqStrB = ""
    for (i in input[0].indices) {
        val freqA = cols[i]!!.groupingBy { it }.eachCount().maxBy { it.value }.key
        freqStrA += freqA

        val freqB = cols[i]!!.groupingBy { it }.eachCount().minBy { it.value }.key
        freqStrB += freqB
    }
    println(freqStrA)
    println(freqStrB)
}