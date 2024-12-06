import java.io.File

fun main() {
    val outbound = mutableMapOf<Int, MutableSet<Int>>()
    val updates = mutableListOf<List<Int>>()

    var isReadingUpdates = false
    File("inputs/input5.txt").forEachLine { line ->
        if (line.isBlank()) {
            isReadingUpdates = true
            return@forEachLine
        }

        if (isReadingUpdates) {
            updates.add(line.split(",").map { it.toInt() })
        } else {
            val pair = line.split("|").map { it.toInt() }
            outbound[pair[0]] = outbound.getOrDefault(pair[0], mutableSetOf()).apply { add(pair[1]) }
        }
    }

    println(outbound)
    println(updates)

    var counterA = 0
    updateLoop@ for (update in updates) {
        for (elemA in update) {
            for (elemB in update) {
                if (elemA != elemB && elemA in outbound && elemB in outbound[elemA]!! && update.indexOf(elemA) > update.indexOf(
                        elemB
                    )
                ) {
                    continue@updateLoop
                }
            }
        }

        counterA += update[update.size / 2]

    }

    println("Counter A: $counterA")
}