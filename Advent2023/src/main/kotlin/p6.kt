import java.io.File

/**
 * - One speedrunner just plugged this into the quadratic formula via Wolfram LOL
 */

fun main() {
    run(false)
    run(true)
}

fun run(isPartB: Boolean) {
    val lines = File("inputs/input6.txt").readLines()
    val timeStr = lines[0].split(":")[1]
    val distStr = lines[1].split(":")[1]
    val times = if (isPartB) mutableListOf(timeStr.replace(" ", "").toLong())
                else timeStr.split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    val distances = if (isPartB) mutableListOf(distStr.replace(" ", "").toLong())
                    else distStr.split(" ").filter { it.isNotBlank() }.map { it.toLong() }

    val validTimes = mutableListOf<Int>()
    times.forEachIndexed { index, time ->
        val distance = distances[index]
        validTimes.add(0)
        for (i in 0..time) {
            if (i * (time - i) > distance) {
                validTimes[validTimes.lastIndex] += 1
            }
        }
    }

    println(validTimes.reduce(Int::times))
}
