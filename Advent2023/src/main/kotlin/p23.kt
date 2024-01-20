import java.io.File
import kotlin.math.max

data class GraphNode(
    val graphCoord: GraphCoord,
    var adj: MutableMap<GraphNode, Int> // Neighbor: Distance
) {
    init {
        val initialAdj = adj
    }
    override fun hashCode(): Int {
        return graphCoord.hashCode()
    }
}


data class DFSAttempt(
    val current: GraphNode,
    val seen: MutableSet<GraphNode>,
    val cumulativeDist: Int
)


fun main() {
    val bridgeFinder = BridgeFinder()

}

class BridgeFinder {
    lateinit var start: GraphNode
    lateinit var end: GraphNode
    val coordToNode = mutableMapOf<GraphCoord, GraphNode>()


    init {
        val input = File("inputs/input23.txt").readLines()
        for (x in input.indices) {
            for (y in input[x].indices) {
                val coord = GraphCoord(x, y)
                val node = GraphNode(coord, mutableMapOf())
                if (input[x][y] == '#') continue // Skip All Walls
                coordToNode[coord] = node
                if (x == 0 && input[x][y] == '.') {
                    start = node
                }
                if (x == input.lastIndex && input[x][y] == '.') {
                    end = node
                }
            }
        }

        // Set neighbors
        val offsets =
            mutableListOf(
                GraphCoord(0, 1), GraphCoord(0, -1), GraphCoord(1, 0), GraphCoord(-1, 0)
            )
        for (v in coordToNode.keys) {
            for (offset in offsets) {
                val neighbor = GraphCoord(v.x + offset.x, v.y + offset.y)
                if (neighbor in coordToNode) {
                    coordToNode[v]!!.adj.set(coordToNode[neighbor]!!, 1)
                }
            }
        }

        println(coordToNode.size)
        var keepCondensing = true
        while (keepCondensing) {
            keepCondensing = false
            allNodes@ for (node in coordToNode.values) {
                if (node.adj.size == 2) {
                    keepCondensing = true

                    val list = node.adj.keys.toList()
                    val a = list[0]
                    val c = list[1]
                    // Remove the intermediate node from the ends
                    a.adj.remove(node)
                    c.adj.remove(node)

                    // Stitch the ends away
                    val dist = node.adj[a]!! + node.adj[c]!!
                    a.adj.set(c, dist)
                    c.adj.set(a, dist)

                    // Remove the middle from the confines of this world
                    node.adj.clear()
                    coordToNode.remove(node.graphCoord)

                    break@allNodes
                }
            }
        }
        println(coordToNode.size)
        println(coordToNode.values.map { it.adj.size })


        var maxSoFar = 0
        val stack = mutableListOf(DFSAttempt(start, mutableSetOf(start), 0))
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (current.current == end) {
                maxSoFar = max(maxSoFar, current.cumulativeDist)
                continue
            }
            for (neighbor in current.current.adj) {
                val node = neighbor.key
                val dist = neighbor.value
                if (!current.seen.contains(node)) {
                    stack.add(
                        current.copy(
                            current = node,
                            seen = current.seen.toMutableSet().apply { add(node) },
                            cumulativeDist = current.cumulativeDist + dist
                        )
                    )
                }
            }
        }
        println(maxSoFar)
    }
}

/**
 * For this problem I needed to consult the reddit similar to p12
 *
 * Most people suggested using the compressing the chains (i.e.) DFSing only the intersections as I did here
 *
 * Others noted that Longest Path is NP-Hard and just brute forced in a more performant language
 *
 * Some people used heuristics
 *
 * In my original strategy I did try something similar - I tried to get things down to dominators and bridges and calculate
 * pairwise distance between them. This was overly complicated however, and I still ran into long runtimes
 */