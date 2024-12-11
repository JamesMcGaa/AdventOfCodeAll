import java.io.File


fun main() {
    println("Part A: ${part11A()}")
    println("Part B: ${part11B()}")
}

/**
 * The key to note is that the order of the stones does not matter, the list is irrelevant
 */
fun part11B(): Long {
    var stones = File("inputs/input11.txt").readLines()[0].split(" ").map { it.toLong() }.groupingBy { it }.eachCount()
        .mapValues { entry -> entry.value.toLong() }.toMutableMap()
    repeat(75) {
        val newStones = mutableMapOf<Long, Long>()
        stones.forEach { entry ->
            val stone = entry.key.toString()
            if (stone == "0") {
                newStones[1L] = newStones.getOrDefault(1L, 0L) + entry.value
            } else if (stone.length % 2 == 0) {
                val right = stone.substring(stone.length / 2).toLong()
                val left = stone.substring(0, stone.length / 2).toLong()
                newStones[right] = newStones.getOrDefault(right, 0L) + entry.value
                newStones[left] = newStones.getOrDefault(left, 0L) + entry.value
            } else {
                val biggerStone = entry.key * 2024L
                newStones[biggerStone] = newStones.getOrDefault(biggerStone, 0L) + entry.value
            }
        }
        stones = newStones
    }
    return stones.values.sum()
}

fun part11A(): Int {
    val stones = File("inputs/input11.txt").readLines()[0].split(" ").map { it.toLong() }.toMutableList()
    repeat(25) {
        var i = stones.indices.last
        while (i >= 0) {
            val stone = stones[i].toString()
            if (stone == "0") {
                stones[i] = 1L
            } else if (stone.length % 2 == 0) {
                stones.removeAt(i)
                stones.add(i, stone.substring(stone.length / 2).toLong())
                stones.add(i, stone.substring(0, stone.length / 2).toLong())
            } else {
                stones[i] *= 2024L
            }
            i -= 1
        }
    }
    return stones.size
}