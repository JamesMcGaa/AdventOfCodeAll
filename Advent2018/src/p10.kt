import Utils.Coord
import Utils.flip
import Utils.rotateClockwise
import java.io.File

data class Star(
    var pos: Coord,
    val vel: Coord,
)


fun main() {
    var stars = File("inputs/input10.txt").readLines().map {
        val split = it.filter { it.isDigit() || it.isWhitespace() || it == '-' }.split(" ").filter { it.isNotBlank() }
            .map { it.toInt() }
        Star(Coord(split[0], split[1]), Coord(split[2], split[3]))
    }

    repeat (100000) {
        stars.forEach { star ->
            star.pos += star.vel
        }


        val starPos = stars.map { it.pos }.toSet()
        val allAdj = stars.all { star ->
            // Heuristic - all should be adjacent. Started with manhattan neighbors then moved here
            // After no match
            (star.pos.fullNeighbors intersect starPos).isNotEmpty()
        }
        if (allAdj) {

            val grid = stars.associate { star -> Pair(star.pos, "#") }
            println("Part A:")
            Utils.printGrid(flip(rotateClockwise(grid)))
            println("Part B: ${it + 1}")
            return
        }
    }

    throw Exception("Pattern not found!")
}