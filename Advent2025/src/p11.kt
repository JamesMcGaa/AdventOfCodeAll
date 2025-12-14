import java.io.File

fun main() {
    println("Part A: ${p11("inputs/input11.txt")}")
    println("Part B: ${p11("inputs/input11.txt", isPartB = true)}")
}

fun p11(filename: String, isPartB: Boolean = false): Long {
    val prev = mutableMapOf<String, MutableSet<String>>()
    val adj = mutableMapOf<String, MutableSet<String>>()
    File(filename).forEachLine { line ->
        val split = line.split(": ")
        val start = split.first()
        val dests = split.last().split(" ")
        adj[start] = dests.toMutableSet()
        dests.forEach { dest ->
            prev[dest] = prev.getOrDefault(dest, mutableSetOf()).apply { add(start) }
        }
    }

    val memo = mutableMapOf<Pair<String, String>, Long>()
    fun pathsTo(from: String, to: String): Long {
        val key = Pair(from, to)
        if (key in memo) {
            return memo[key]!!
        } else if (from == to) {
            return 1
        } else {
            val ret = adj.getOrDefault(from, emptySet()).sumOf { adj -> pathsTo(adj, to) }
            memo[key] = ret
            return ret
        }
    }

    return if (isPartB) {
        pathsTo("svr", "dac") * pathsTo("dac", "fft") * pathsTo("fft", "out") +
                pathsTo("svr", "fft") * pathsTo("fft", "dac") * pathsTo("dac", "out")
    } else {
        pathsTo("you", "out")
    }
}