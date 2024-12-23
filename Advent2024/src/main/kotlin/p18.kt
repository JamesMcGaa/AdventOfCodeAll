import main.kotlin.Utils.Coord
import java.io.File

fun main() {
    val bytes = File("inputs/input18.txt").readLines().map {
        Coord(it.split(",")[0].toInt(),it.split(",")[1].toInt())
    }

    fun bfs(amount: Int): Int {
        val partABytes = bytes.subList(0,amount)
        var frontier = mutableListOf(Coord(0,0))
        var seen = mutableSetOf(Coord(0,0))
        var iterations = 0
        while (frontier.isNotEmpty()) {
            iterations++
            val newFrontier = mutableListOf<Coord>()
            frontier.forEach { coord ->
                val neighbors = coord.manhattanNeighbors
                for (neigh in neighbors) {
                    if (neigh.x in 0..70 && neigh.y in 0..70 && neigh !in seen && neigh !in partABytes) {
                        newFrontier.add(neigh)
                        seen.add(neigh)
                    }
                    if (neigh == Coord(70,70)) {
                        return iterations
                    }
                }
            }
            frontier = newFrontier
        }
        return -1
    }

    var ctr = 0
    while (bfs(ctr) != -1) {
        ctr++
    }
    println("Part A ${bfs(1024)}")
    println("Part B ${bytes[ctr-1]}")

}