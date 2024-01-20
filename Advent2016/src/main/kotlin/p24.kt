import java.io.File
import kotlin.math.min

fun main() {
    runP24(false)
    runP24(true)
}

fun runP24(isPartB: Boolean) {
    var board = mutableSetOf<Coord>()
    val nums = mutableMapOf<Char, Coord>()
    val input = File("inputs/input24.txt").readLines()
    for (x in input.indices) {
        for (y in input[x].indices) {
            val char = input[x][y]
            if (char != '#') {
                val coord = Coord(x, y)
                board.add(coord)
                if (char != '.') {
                    nums[char] = coord
                }
            }
        }
    }

    val dists = mutableMapOf<Pair<Coord, Coord>, Int>()
    for (start in nums.values) {
        for (end in nums.values) {
            if (start != end) {
                dists[Pair(start, end)] = dist(board, start, end)
            }
        }
    }

    var counterA = 1000000
    for (order in allPermutations(nums.values.toSet())) {
        if (order.first() == nums['0']) {
            if (isPartB) {
                order.add(order.first())
            }
            var counter = 0
            for (i in 0 .. order.lastIndex - 1) {
                counter += dists[Pair(order[i], order[i+1])]!!
            }
            counterA = min(counterA, counter)
        }
    }

    println(counterA)
}

fun dist(board: Set<Coord>, start: Coord, end: Coord ): Int {
    val seen = mutableSetOf<Coord>()
    var frontier = mutableSetOf<Coord>(start)
    var counter = 0
    while (frontier.isNotEmpty()) {
        val newFontier = mutableSetOf<Coord>(start)
        frontier.forEach {
            seen.add(it)
            if (it == end) {
                return counter
            }
            newFontier.addAll(
                listOf(
                    it.copy(x = it.x+1),
                    it.copy(x = it.x-1),
                    it.copy(y = it.y+1),
                    it.copy(y = it.y-1),
                ).filter { board.contains(it) && !seen.contains(it) }
            )
        }
        frontier = newFontier
        counter += 1
    }
    throw Exception("path not found")
}

// https://stackoverflow.com/questions/59715608/kotlin-generate-permutations-in-order-of-list-without-duplicate-elements
fun <T> allPermutations(set: Set<T>): Set<MutableList<T>> {
    if (set.isEmpty()) return emptySet()

    fun <T> _allPermutations(list: List<T>): Set<List<T>> {
        if (list.isEmpty()) return setOf(emptyList())

        val result: MutableSet<List<T>> = mutableSetOf()
        for (i in list.indices) {
            _allPermutations(list - list[i]).forEach {
                    item -> result.add(item + list[i])
            }
        }
        return result
    }

    return _allPermutations(set.toList()).map {it.toMutableList()}.toSet()
}