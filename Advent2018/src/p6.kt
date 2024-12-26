import Utils.Coord
import Utils.freqCount
import java.io.File

fun main() {
    val chronalCoords = mutableSetOf<Coord>()
    val grid = mutableMapOf<Coord, Char>()
    var counter = 'A'
    File("inputs/input6.txt").forEachLine { line ->
        val split = line.split(", ").map { it.toInt() }
        val coord = Coord(split[0], split[1])
        grid[coord] = counter++
        chronalCoords.add(coord)
    }

    var minX = grid.keys.minOf { it.x }
    var maxX = grid.keys.maxOf { it.x }
    val diameterX = maxX - minX
    var minY = grid.keys.minOf { it.y }
    var maxY = grid.keys.maxOf { it.y }
    val diameterY = maxY - minY
    var partB = 0L

    for (i in minX - diameterX..maxX + diameterX) {
        for (j in minY - diameterY..maxY + diameterY) {
            val current = Coord(i, j)
            if (chronalCoords.sumOf { (it - current).manhattanDist } < 10000) partB++
            val dists = chronalCoords.map {
                Pair(it, (it - current).manhattanDist)
            }.sortedBy { it.second }
            if (dists[0].second == dists[1].second) {
                grid[current] = '.'
            } else {
                grid[current] = grid[dists[0].first]!!
            }
        }
    }

    minX = grid.keys.minOf { it.x }
    maxX = grid.keys.maxOf { it.x }
    minY = grid.keys.minOf { it.y }
    maxY = grid.keys.maxOf { it.y }
    val infiniteSectorsInclPeriods = mutableSetOf<Char>()
    grid.forEach { entry ->
        if (entry.key.x in setOf(minX, maxX) || entry.key.y in setOf(minY, maxY)) {
            infiniteSectorsInclPeriods.add(entry.value)
        }
    }

    val partA = grid.values.filter { it !in infiniteSectorsInclPeriods }.freqCount().values.max()

    println("Part A: $partA")
    println("Part B: $partB")
}