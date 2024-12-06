import java.io.File

const val XMAS = "XMAS"
const val MAS = "MAS"

fun main() {
    var counterA = 0
    val counts = mutableMapOf<Pair<Int, Int>, Int>()
    val grid = File("inputs/input4.txt").readLines()
    for (x in grid.indices) {
        for (y in grid[x].indices) {
            counterA += countAtPoint(x, y, grid, XMAS, counts = null)
            countAtPoint(x, y, grid, MAS, counts = counts)
        }
    }
    println("Part A: $counterA")
    println("Part B: ${counts.count { entry -> entry.value >= 2 }}")
}

/**
 * Given the input grid inp and a target string, returns the number of times we can start at (x,y)
 * And find the target
 *
 * Optionally for part B, if "counts" is provided we store a map of the midpoints and each time they
 * midpoint for a path. Implicitly in this case we have to disable UP-DOWN and LEFT-RIGHT counts as we
 * are doing the X-MAS case.
 */
fun countAtPoint(x: Int, y: Int, inp: List<String>, target: String, counts: MutableMap<Pair<Int, Int>, Int>?): Int {
    var count = 0

    val dirs = mutableListOf(
        listOf(1, 1),
        listOf(1, -1),
        listOf(-1, 1),
        listOf(-1, -1)
    )

    if (counts == null) {
        dirs.addAll(
            listOf(
                listOf(0, 1),
                listOf(0, -1),
                listOf(1, 0),
                listOf(-1, 0)
            )
        )
    }

    for (dir in dirs) {
        var attempt = ""
        for (len in target.indices) {
            val projX = x + len * dir[0]
            val projY = y + len * dir[1]
            attempt += safeIndex(projX, projY, inp)
        }
        if (attempt == target) {
            count += 1
            if (counts != null) {
                val projX = x + (target.length / 2) * dir[0]
                val projY = y + (target.length / 2) * dir[1]
                counts[Pair(projX, projY)] = counts.getOrDefault(Pair(projX, projY), 0) + 1
            }
        }
    }

    return count
}

fun safeIndex(x: Int, y: Int, inp: List<String>): String {
    if (x in inp.indices && y in inp[x].indices) {
        return inp[x][y].toString()
    }
    return ""
}
