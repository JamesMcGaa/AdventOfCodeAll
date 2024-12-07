import java.io.File

fun main() {
    // More useful, y to the set of dependencies of y
    val inbound = mutableMapOf<Int, MutableSet<Int>>()

    // Matches the input, x to the set of values x must proceed
    val outbound = mutableMapOf<Int, MutableSet<Int>>()
    val incorrectUpdates = mutableListOf<List<Int>>()
    val updates = mutableListOf<List<Int>>()

    var isReadingUpdates = false
    File("inputs/input5.txt").forEachLine { line ->
        if (line.isBlank()) {
            isReadingUpdates = true
            return@forEachLine
        }

        if (isReadingUpdates) {
            updates.add(line.split(",").map { it.toInt() })
        } else {
            val pair = line.split("|").map { it.toInt() }
            outbound[pair[0]] = outbound.getOrDefault(pair[0], mutableSetOf()).apply { add(pair[1]) }
            inbound[pair[1]] = inbound.getOrDefault(pair[1], mutableSetOf()).apply { add(pair[0]) }
        }
    }

    var counterA = 0
    updateLoop@ for (update in updates) {
        for (elemA in update) {
            for (elemB in update) {
                if (elemA != elemB && elemA in outbound && elemB in outbound[elemA]!! && update.indexOf(elemA) > update.indexOf(
                        elemB
                    )
                ) {
                    incorrectUpdates.add(update)
                    continue@updateLoop
                }
            }
        }

        counterA += update[update.size / 2]
    }
    println("Part A: $counterA")

    // Custom Sort (thanks Reddit)
    val partBSimpleSort = incorrectUpdates.sumOf { update ->
        midElementSortedSimpleSort(inbound, update)
    }
    println("Part B (sort): $partBSimpleSort")

    val partBTopologicalSort = incorrectUpdates.sumOf { update ->
        midElementSortedTopological(inbound, update)
    }
    println("Part B (topological sort): $partBTopologicalSort")
}

fun midElementSortedSimpleSort(outbound: MutableMap<Int, MutableSet<Int>>, update: List<Int>): Int {
    val sorted = update.sortedWith(
        Comparator comparator@{ a, b ->
            if (a in outbound && b in outbound[a]!!) {
                return@comparator -1
            }
            if (b in outbound && a in outbound[b]!!) {
                return@comparator 1
            }
            return@comparator 0

        })
    return sorted[sorted.size / 2]
}

fun midElementSortedTopological(inbound: MutableMap<Int, MutableSet<Int>>, update: List<Int>): Int {
    val updateNodes = update.toMutableSet()
    val copiedInbound = mutableMapOf<Int, MutableSet<Int>>()
    val noInbound = mutableSetOf<Int>()
    val sorted = mutableListOf<Int>()

    inbound.keys.forEach { node ->
        if (node in updateNodes) {
            copiedInbound[node] = (inbound[node]!! intersect updateNodes).toMutableSet()
            if (copiedInbound[node]!!.isEmpty()) {
                noInbound.add(node)
            }
        }
    }
    noInbound.addAll(updateNodes - copiedInbound.keys)

    while (noInbound.isNotEmpty()) {
        val current = noInbound.first()
        noInbound.remove(current)
        copiedInbound.remove(current)
        sorted.add(current)

        copiedInbound.keys.forEach { remainingKey ->
            copiedInbound[remainingKey]!!.remove(current)
            if (copiedInbound[remainingKey]!!.isEmpty()) {
                noInbound.add(remainingKey)
            }
        }
    }

    assert(copiedInbound.isEmpty())
    assert(noInbound.isEmpty())
    assert(sorted.size == update.size)

    return sorted[sorted.size / 2]
}