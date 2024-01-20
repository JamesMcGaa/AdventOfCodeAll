import java.io.File

data class WirePath(
    val current: String,
    val seen: MutableSet<String> = mutableSetOf(),
    var parent: String = ""
)

fun main() {
    val graph = Graph()

    val freqs = mutableMapOf<String, Int>()
    var commonEdge = listOf<String>()
    repeat(3) {
        repeat(1000) {
            val start = graph.allNodes.random()
            val end = graph.allNodes.random()
            if (start != end) {
                val bfsResult = graph.bfs(start, end)
                bfsResult?.forEach { rep ->
                    freqs[rep] = freqs.getOrDefault(rep, 0) + 1
                }
            }
        }

        println(freqs.maxBy { it.value })
        commonEdge = freqs.maxBy { it.value }.key.removePrefix("[").removeSuffix("]").split(",").map { it.trim() }
        graph.adj[commonEdge[0]]!!.remove(commonEdge[1])
        graph.adj[commonEdge[1]]!!.remove(commonEdge[0])
    }

    val red = commonEdge[0]
    val blue = commonEdge[1]
    val redSize =graph.getComponentSize(red)
    val blueSize =graph.getComponentSize(blue)
    println(redSize)
    println(blueSize)
    println(redSize * blueSize)

}

class Graph() {
    val adj = mutableMapOf<String, MutableSet<String>>()
    val allNodes = mutableSetOf<String>()

    init {
        File("inputs/input25.txt").forEachLine {
            val source = it.split(":")[0].trim()
            val dests = it.split(":")[1].split(" ").map { it.trim() }.filter { it.isNotEmpty() }

            allNodes.add(source)
            if (!adj.contains(source)) {
                adj[source] = mutableSetOf()
            }
            adj[source]!!.addAll(dests)

            for (dest in dests) {
                allNodes.add(dest)
                if (!adj.contains(dest)) {
                    adj[dest] = mutableSetOf()
                }
                adj[dest]!!.add(source)
            }
        }
    }

    fun getComponentSize(start: String): Int {
        val seen = mutableSetOf<String>(start)
        val stack = mutableListOf(start)
        while(stack.isNotEmpty()) {
            val current = stack.removeLast()
            val unseenNeighbors = adj[current]!!.filter { !seen.contains(it) }
            seen.addAll(unseenNeighbors)
            stack.addAll(unseenNeighbors)
        }
        return seen.size
    }

    fun bfs(start: String, end: String): MutableSet<String>? {
        val parents = mutableMapOf<String, String>()
        val seen = mutableSetOf(start)
        var frontier = mutableListOf(start)
        while (frontier.isNotEmpty()) {
            val newFrontier = mutableListOf<String>()
            frontier.forEach { current ->
                if (current == end) {
                    val path = mutableSetOf<String>()
                    var tail = end
                    while (tail != start) {
                        val nextTail = parents[tail]!!
                        val rep = mutableListOf(tail, nextTail).sorted().toString()
                        path.add(rep)
                        tail = nextTail
                    }
                    return path
                }

                for (neighbor in adj[current]!!) {
                    if (!seen.contains(neighbor)) {
                        seen.add(neighbor)
                        parents[neighbor] = current
                        newFrontier.add(neighbor)
                    }
                }
            }
            frontier = newFrontier
        }
        return null
    }
}

/**
 * Happy to have gotten this one on my own
 *
 *  - Some others used my same strategy, or the 6 most popular nodes spin on it
 *
 *  - Others used networkx in Python
 */