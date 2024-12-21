import Utils.CircularLinkedListNode

const val INPUT = "394618527"

fun partA() {
    var cll = CircularLinkedListNode.initFromList(INPUT.map { it.digitToInt() })

    repeat(100) {
        val p1 = cll.next.splice()
        val p2 = p1.second.splice()
        val p3 = p2.second.splice()
        var normalizedId = (cll.value - 2 + INPUT.length) % INPUT.length
        while (normalizedId + 1 in setOf(p1.first.value, p2.first.value, p3.first.value)) {
            normalizedId = (normalizedId - 1 + INPUT.length) % INPUT.length
        }
        val destId = normalizedId + 1
        val destNode = cll.find(destId)
        val p1b = destNode.insert(p1.first)
        val p2b = p1b.insert(p2.first)
        p2b.insert(p3.first)
        cll = cll.next
    }

    val partA = cll.find(1).next.toList().joinToString("").removeSuffix("1")
    println("Part A: $partA")
}

fun partB() {
    val totalNodes = 1000000
    val totalIterations = 10000000
    var cll = CircularLinkedListNode.initFromList(INPUT.map { it.digitToInt() } + List(totalNodes - INPUT.length) { INPUT.length + 1 + it })

    val idToNodeMap = mutableMapOf<Int, CircularLinkedListNode<Int>>(INPUT.first().digitToInt() to cll)
    var current = cll.next
    while (current != cll) {
        idToNodeMap[current.value] = current
        current = current.next
    }

    repeat(totalIterations) {
        val p1 = cll.next.splice()
        val p2 = p1.second.splice()
        val p3 = p2.second.splice()
        var normalizedId = (cll.value - 2 + totalNodes) % totalNodes
        while (normalizedId + 1 in setOf(p1.first.value, p2.first.value, p3.first.value)) {
            normalizedId = (normalizedId - 1 + totalNodes) % totalNodes
        }
        val destId = normalizedId + 1
        val destNode = idToNodeMap[destId]!!
        val p1b = destNode.insert(p1.first)
        val p2b = p1b.insert(p2.first)
        p2b.insert(p3.first)
        cll = cll.next
    }

    val partB = idToNodeMap[1]!!.next.value.toLong() * idToNodeMap[1]!!.next.next.value.toLong()
    println("Part B: $partB")
}

fun main() {
    partA()
    partB()
}