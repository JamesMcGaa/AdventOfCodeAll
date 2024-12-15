import java.io.File
import kotlin.collections.flatten

fun main() {
    val input = File("inputs/input15.txt").readLines()
    val breakIdx = input.indexOf("")
    println(breakIdx)

    lateinit var current: Coord
    val grid = mutableMapOf<Coord, Char>()
    input.subList(0, breakIdx).forEachIndexed { i, row ->
        row.forEachIndexed { j, ch ->
            grid[Coord(i, j)] = ch
            if (ch == '@') {
                current = Coord(i, j)
            }
        }
    }
    val inst = input.subList(breakIdx + 1, input.indices.last+1)
    val parsed = inst.map { it.toCharArray().toList() }.flatten()

    fun push(dir: Char, spot: Coord): Coord? {
        if(grid[spot] == '.') {
            return spot
        }
        if(grid[spot] == '#') {
            return null
        }

        val nextSpot = when (dir) {
            '^' -> spot.copy(x = spot.x-1)
            'v' -> spot.copy(x = spot.x+1)
            '<' -> spot.copy(y = spot.y-1)
            '>' -> spot.copy(y = spot.y+1)
            else -> throw Exception("Bad dir char")
        }
        val result = push(dir, nextSpot)
        if (result != null) {
            grid[nextSpot] = grid[spot]!!
            grid[spot] = '.'
            return nextSpot
        }
        return null
    }
    println(parsed)
    for (dir in parsed) {
        current = push(dir, current) ?: current
    }

    for (i in 0 until breakIdx) {
        var row = ""
        for (j in 0 until breakIdx) {
            row += grid[Coord(i,j)]
        }
        println(row)
    }


    val partA = grid.filterValues {ch -> ch == 'O'}.keys.sumOf {100*it.x + it.y}
    println(partA)
}