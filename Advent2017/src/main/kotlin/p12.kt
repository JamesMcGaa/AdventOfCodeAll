import java.io.File

fun main() {
    val graph = mutableMapOf<Int, MutableSet<Int>>()
    File("inputs/input12.txt").forEachLine {
        line ->
        val start = line.split("<->")[0].trim().toInt()
        val ends = line.split("<->")[1].split(",").map {it.trim().toInt()}
        for (end in ends) {
            if (!graph.contains(start)) {
                graph[start] = mutableSetOf()
            }
            if (!graph.contains(end)) {
                graph[end] = mutableSetOf()
            }
            graph[start]!!.add(end)
            graph[end]!!.add(start)
        }
    }

    val seenOverall = mutableSetOf<Int>()
    var islandCounter = 0
    for (node in graph.keys) {
        if (!seenOverall.contains(node)) {
            islandCounter += 1

            val seen = mutableSetOf<Int>()
            val stack = mutableListOf(node)
            while (stack.isNotEmpty()) {
                val current = stack.removeLast()
                if (seen.contains(current)) continue

                seen.add(current)
                for (neighbor in graph.get(current)!!){
                    stack.add(neighbor)
                }
            }

            if (node == 0) {
                println(seen.size)
            }

            seenOverall.addAll(seen)
        }
    }
    println(islandCounter)
}