import java.io.File

class NavNode() {
    val children = mutableListOf<NavNode>()
    val metadata = mutableListOf<Int>()
    var end = -1

    fun value(): Int {
        return if (children.isEmpty()) {
            metadata.sum()
        } else {
            metadata.sumOf { idx ->
                children.getOrNull(idx - 1)?.value() ?: 0
            }
        }
    }

    fun metadataSum(): Int {
        return metadata.sum() + children.sumOf { it.metadataSum() }
    }
}

fun main() {
    val input = File("inputs/input8.txt").readLines().first().split(" ").map { it.toInt() }

    fun parseIntoNode(startIdx: Int): NavNode {
        val current = NavNode()
        val childCount = input[startIdx]
        val metadataCount = input[startIdx + 1]
        var pointer = startIdx + 2
        repeat(childCount) {
            val child = parseIntoNode(pointer)
            current.children.add(child)
            pointer = child.end
        }
        for (i in pointer until pointer + metadataCount) {
            current.metadata.add(input[i])
        }
        current.end = pointer + metadataCount
        return current
    }

    val root = parseIntoNode(0)

    println("Part A: ${root.metadataSum()}")
    println("Part B: ${root.value()}")
}