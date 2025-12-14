import Utils.CoordLong
import Utils.Coord
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Part A: ${p9a("inputs/input9.txt")}")
    println("Part B: ${p9b("inputs/input9.txt")}")
}

fun p9a(filename: String): Long {
    val coords = File(filename).readLines().map {
        val split = it.split(",").map { it.toLong() }
        CoordLong(split.first(), split.last())
    }

    return coords.maxOf { first ->
        coords.maxOf { second ->
            (abs(first.x - second.x) + 1) * (abs(first.y - second.y) + 1)
        }
    }
}

fun p9b(filename: String): Int {
    val coords = File(filename).readLines().map {
        val split = it.split(",").map { it.toInt() }
        Coord(split.first(), split.last())
    }

    val grid = mutableMapOf<Coord, Char>()
    coords.forEach { grid[it] = '#' }

    var vertCount = 0
    var horzCount = 0
    for (i in coords.indices) {
        val j = (i + 1) % coords.size
        val first = coords[i]
        val second = coords[j]

        if (first.x == second.x) {
            horzCount++ 
            for (y in min(first.y, second.y) + 1 until max(first.y, second.y)) {
                grid[Coord(first.x, y)] = 'X'
            }
        } else {
            vertCount++
            for (x in min(first.x, second.x) + 1 until max(first.x, second.x)) {
                grid[Coord(x, first.y)] = 'X'
            }
        }
    }
    println("$vertCount, $horzCount")

    return 0
}
