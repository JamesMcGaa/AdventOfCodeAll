import Utils.ZCoordLong
import Utils.product
import java.io.File

fun main() {
    println("Part A: ${p8a("inputs/input8.txt")}")
    println("Part B: ${p8a("inputs/input8.txt", isPartB = true)}")
}

fun p8a(filename: String, isPartB: Boolean = false): Long {
    val coords = File(filename).readLines().map { line ->
        val split = line.split(",")
        ZCoordLong(split[0].toLong(), split[1].toLong(), split[2].toLong())
    }

    val coordToGroup = mutableMapOf<ZCoordLong, Set<ZCoordLong>>()
    coords.forEach { coord -> coordToGroup[coord] = setOf(coord) }

    val dists = mutableListOf<Triple<ZCoordLong, ZCoordLong, Double>>()
    coords.forEachIndexed { idx, coord ->
        for (i in idx + 1 until coords.size) {
            val secondCoord = coords[i]
            dists.add(Triple(coord, secondCoord, (coord - secondCoord).dist))
        }
    }
    dists.sortBy { it.third }
    val distsToIterateOn = if (isPartB) dists else dists.subList(0, 1000)
    distsToIterateOn.forEach { (first, second, _) ->
        val firstSet = coordToGroup[first]!!
        val secondSet = coordToGroup[second]!!
        if (firstSet != secondSet) {
            val newSet = firstSet union secondSet
            newSet.forEach { coord -> coordToGroup[coord] = newSet }
            if (isPartB && newSet.size == coords.size) {
                return first.x * second.x
            }
        }
    }

    if (isPartB) {
        throw Exception("Failed to merge")
    }

    return coordToGroup.values.toSet().map { it.size }
        .sortedDescending().subList(0, 3).map { it.toLong() }.product()
}


