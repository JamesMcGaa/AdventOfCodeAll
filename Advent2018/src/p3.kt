import Utils.Coord
import java.io.File

fun main() {
    val grid = mutableMapOf<Coord, Int>()
    val claims = mutableListOf<Set<Coord>>()
    File("inputs/input3.txt").forEachLine {
        val r = it.split(" ")[2].split(",")[1].removeSuffix(":").toInt()
        val c = it.split(" ")[2].split(",")[0].toInt()
        val rOffset = it.split(" ")[3].split("x")[1].toInt()
        val cOffset = it.split(" ")[3].split("x")[0].toInt()
        val claim = mutableSetOf<Coord>()
        for (i in r until r + rOffset) {
            for (j in c until c + cOffset) {
                val target = Coord(i, j)
                grid[target] = grid.getOrDefault(target, 0) + 1
                claim.add(target)
            }
        }
        claims.add(claim)
    }
    println("Part A: ${grid.count { it.value >= 2 }}")

    claims.forEachIndexed { idx, coords ->
        if (coords.all { grid[it] == 1 }) {
            println("Part B: ${idx + 1}")
            return
        }
    }
}