import java.io.File

val nodes = hashMapOf<String, Node>()

data class Node(
    val name: String,
    val weight: Int,
    val children: MutableSet<String>?
) {
    fun weight(): Int {
        return weight + (children?.sumOf { nodes[it]!!.weight() } ?: 0)
    }

    fun isUnbalanced(): Boolean {
        val weights = children?.map {
            nodes[it]!!.weight()
        }
        if (weights?.toSet()?.size != 1 && weights?.toSet()?.size != null) {
           return true
        }
        return false
    }

    fun highestUnbalanced(): Boolean {
        // All children are balanced or children are just null
        return this.isUnbalanced() && children?.all { !nodes[it]!!.isUnbalanced() } != false
    }
}

fun main() {
    File("inputs/input7.txt").forEachLine { line ->
        val current = line.split("->")[0].split(" ")[0]
        val weight = line.split("->")[0].filter { it.isDigit() }.toInt()
        val dependencies =
            if (line.contains("->"))
                line.split("->")[1].split(",").map { it.trim() }
            else null
        nodes[current] = Node(current, weight, dependencies?.toMutableSet())
    }

    for (node in nodes.values) {
        if (nodes.values.filter { it.children?.contains(node.name) == true }.size == 0) {
            println(node.name)
        }
    }

    for (node in nodes.values) {
        if (node.highestUnbalanced()) {
            println(node)
            println(node.children?.all { !nodes[it]!!.isUnbalanced() })
            println(node.children!!.map { nodes[it]!!.weight()})
        }
    }
}
