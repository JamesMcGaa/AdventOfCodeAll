package main.kotlin

import java.io.File
import kotlin.collections.getOrPut


fun main() {
    val graph = mutableMapOf<String, MutableSet<String>>()
    File("inputs/input23.txt").forEachLine {
        val pair = it.split("-")
        graph.getOrPut(pair[0]) { mutableSetOf<String>() }.apply { add(pair[1]) }
        graph.getOrPut(pair[1]) { mutableSetOf<String>() }.apply { add(pair[0]) }
    }

    println("Graph: $graph")
    var partA = 0L
    graph.keys.forEach { v1 -> // Iterate over neighbors instead of all nodes to avoid n^3
        graph.getOrDefault(v1, setOf()).forEach { v2 ->
            graph.getOrDefault(v2, setOf()).forEach { v3 ->
                if (graph.getOrDefault(v3, setOf()).contains(v1) && 't' in setOf(v1.first(), v2.first(), v3.first())) {
                    partA++
                }
            }
        }
    }
    partA = partA / 6 // Remove duplicates
    println("Part A: $partA")


    val allCliques = mutableMapOf<Int, MutableSet<Set<String>>>()
    @Suppress("LocalVariableName")
    fun bronKerbosch(R: Set<String>, P: MutableSet<String>, X: MutableSet<String>) {
        if (P.isEmpty() && X.isEmpty()) {
            allCliques.getOrPut(R.size) { mutableSetOf() }.apply { add(R) }
        }

        while (!P.isEmpty()) {
            val v = P.first()
            bronKerbosch(
                R union setOf(v),
                (P intersect graph[v]!!).toMutableSet(),
                (X intersect graph[v]!!).toMutableSet()
            )
            P.remove(v)
            X.add(v)
        }
    }

    bronKerbosch(setOf<String>(), graph.keys.toMutableSet(), mutableSetOf<String>())

    val maxCliqueSizes = allCliques.keys.max()
    println("Largest clique size: $maxCliqueSizes")
    println("Maximal cliques: ${allCliques[maxCliqueSizes]}")
    println("Part B: ${allCliques[maxCliqueSizes]!!.first().sorted().joinToString(",")}")
}
