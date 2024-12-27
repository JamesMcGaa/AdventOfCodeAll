import java.io.File

fun main() {
    val input = File("inputs/input12.txt").readLines()
    var plants = input.first().removePrefix("initial state: ").mapIndexed { idx, ch -> Pair(idx, ch) }
        .filter { it.second == '#' }.map { it.first }.toSet()
    val rules = mutableMapOf<Set<Int>, Char>()
    input.subList(2, input.size).forEach {
        val offsets = mutableSetOf<Int>()
        it.split(" => ")[0].forEachIndexed { idx, ch ->
            if (ch == '#') {
                offsets.add(idx - 2)
            }
        }
        val target = it.split(" => ")[1].first()
        rules[offsets] = target
    }

    val seen = mutableMapOf<Set<Int>, Int>(plants to 0)
    var iteration = 1
    while (true) {  // Arbitrary
        val newPlants = mutableSetOf<Int>()
        val newMin = plants.min() - 2
        val newMax = plants.max() + 2
        for (idx in newMin..newMax) {
            val vicinity =
                (idx - 2..idx + 2).map { Pair(it - idx, it in plants) }.filter { it.second }.map { it.first }.toSet()
            val match = rules[vicinity] ?: '.'

            if (match == '#') {
                newPlants.add(idx)
            }
        }

        val min = newPlants.min()
        val canonical = newPlants.map { it - min }.toSet()
        if (canonical in seen) {
            println("Currently at: $iteration")
            println("Previously seen at: ${seen[canonical]}")
            val totalCycles = 50000000000L
            val perElementDelta = 1L // Print the two previous iterations sorted to see
            val partB =
                newPlants.sumOf { it.toLong() } + (perElementDelta * newPlants.size * (totalCycles - iteration))
            println("Part B: $partB")
            return
        }
        seen[canonical] = iteration

        if (iteration == 20) {
            println("Part A: ${newPlants.sum()}")
        }

        plants = newPlants
        iteration++
    }

}