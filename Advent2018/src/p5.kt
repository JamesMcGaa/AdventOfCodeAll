import java.io.File

fun main() {
    val initialPolymer = File("inputs/input5.txt").readLines().first().toMutableList()

    fun reducedLength(eliminatedLowercase: Char? = null): Int {
        val polymer = initialPolymer
            .filter { eliminatedLowercase == null || (it != eliminatedLowercase && it != eliminatedLowercase.uppercaseChar()) }
            .toMutableList()

        outer@ while (true) {
            val startSize = polymer.size
            var changed = false
            for (i in 0 until startSize - 1) {
                if (polymer[i + 1].isLowerCase() && polymer[i].isUpperCase() && polymer[i + 1].uppercaseChar() == polymer[i]) {
                    polymer.removeAt(i + 1)
                    polymer.removeAt(i)
                    changed = true
                    break
                }

                if (polymer[i + 1].isUpperCase() && polymer[i].isLowerCase() && polymer[i + 1].lowercaseChar() == polymer[i]) {
                    polymer.removeAt(i + 1)
                    polymer.removeAt(i)
                    changed = true
                    break
                }
            }

            if (!changed) {
                break@outer
            }
        }

        return polymer.size
    }

    println("Part A: ${reducedLength()}")
    val partB = ('a'..'z').minOf { reducedLength(it) }
    println("Part B: $partB")
}