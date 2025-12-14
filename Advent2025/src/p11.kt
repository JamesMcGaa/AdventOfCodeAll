import Utils.Coord
import java.io.File

fun main() {
    println("Part A: ${p11a("inputs/input11.txt")}")
}

fun p11a(filename: String) {
    val prev = mutableMapOf<String, MutableSet<String>>()
    val adj = mutableMapOf<String, MutableSet<String>>()
    File(filename).forEachLine { line ->
        val split = line.split(": ")
        val start = split.first()
        val dests = split.last().split(" ")
        adj[start] = dests.toMutableSet()
        dests.forEach {
            dest ->
            prev[dest] = prev.getOrDefault(dest, mutableSetOf()).apply { add(start) }
        }
    }

    val nodeToPaths = mutableMapOf("you" to 1L)
    prev["you"] = mutableSetOf()
    val stack = mutableListOf("you")
    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        val amount = nodeToPaths.get(current)!!
        adj[current]!!.forEach {  neighbor ->
            nodeToPaths[neighbor] = nodeToPaths.getOrDefault(neighbor, 0) + amount
            prev[neighbor]!!.remove(current)
            if (prev[neighbor]!!.isEmpty()) {
                stack.add(neighbor)
            }
        }
    }
    println(nodeToPaths)
    println(nodeToPaths["out"])
}