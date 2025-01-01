import java.io.File
import kotlin.math.absoluteValue

data class Constellation(
    val a: Int,
    val b: Int,
    val c: Int,
    val d: Int,
) {
    val manhattanDist
        get() = a.absoluteValue + b.absoluteValue + c.absoluteValue + d.absoluteValue

    operator fun minus(other: Constellation): Constellation {
        return Constellation(a - other.a, b - other.b, c - other.c, d - other.d)
    }
}

fun main() {
    val constellations = File("inputs/input25.txt").readLines()
        .map { it.split(",").map { it.toInt() } }
        .map { Constellation(it[0], it[1], it[2], it[3]) }

    val graph = mutableMapOf<Constellation, MutableSet<Constellation>>()
    constellations.forEach { constellation ->
        constellations.forEach { other ->
            if ((constellation - other).manhattanDist <= 3) {
                graph.getOrPut(constellation) { mutableSetOf() }.apply { add(other) }
            }
        }
    }

    /**
     * Essentially this problem boils down to the finding-islands problem via BFS
     */
    val grouped = graph.keys
    var counter = 0
    constellations.forEach { constellation ->
        if (constellation in grouped) {
            val dists = Utils.generalizedBFSV2(
                constellation, { constellation -> true },
                { constellation -> graph[constellation]!! }
            )
            grouped.removeAll(dists.keys)
            counter += 1
        }
    }

    println("Part A (only): $counter")
}