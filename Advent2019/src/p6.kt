import java.io.File
import java.util.*

fun main() {
    var counterA = 0
    val graph = mutableMapOf<String, MutableList<String>>()
    val undirectedGraph = mutableMapOf<String, MutableList<String>>()
    val queue: Queue<Pair<String, Int>> = LinkedList()
    File("inputs/input6.txt").forEachLine {
        val begin = it.split(")")[0]
        val end = it.split(")")[1]
        graph[begin] = graph.getOrDefault(begin, mutableListOf()).apply {
            add(end)
        }
        undirectedGraph[begin] = undirectedGraph.getOrDefault(begin, mutableListOf()).apply {
            add(end)
        }
        undirectedGraph[end] = undirectedGraph.getOrDefault(end, mutableListOf()).apply {
            add(begin)
        }
    }

    queue.add(Pair("COM", 0))
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        val depth = current.second
        counterA += current.second
        graph.getOrDefault(current.first, mutableListOf()).forEach {
            queue.add(Pair(it, depth + 1))
        }
    }
    println(counterA)

    val seen = mutableSetOf<String>()
    queue.add(Pair("YOU", 0))
    while (queue.isNotEmpty()) {
        val current = queue.remove()

        if (current.first in seen) continue
        seen.add(current.first)

        val depth = current.second
        if (current.first == "SAN") {
            println(depth - 2)
            break
        }
        counterA += current.second
        undirectedGraph.getOrDefault(current.first, mutableListOf()).forEach {
            queue.add(Pair(it, depth + 1))
        }
    }
}