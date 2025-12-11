fun main() {
    println("Part A: ${p4a("inputs/input4.txt")}")
    println("Part B: ${p4b("inputs/input4.txt")}")
}

fun p4a(filename: String): Long {
    val grid = Utils.readAsGrid(filename) { it }
    return grid.count { (coord, char) ->
        char == '@' && coord.fullNeighbors.count { grid[it] == '@' } < 4
    }.toLong()
}

fun p4b(filename: String): Long {
    val grid = Utils.readAsGrid(filename) { it }
    val original = grid.values.count {it == '@'}

    while (true) {
        val accessible = grid.filter { (coord, char) ->
            char == '@' && coord.fullNeighbors.count { grid[it] == '@' } < 4
        }
        if (accessible.size == 0) {
            break
        } else {
            accessible.forEach { (coord, char) ->
                grid[coord] = '.'
            }
        }
    }

    return (original - grid.values.count {it == '@'}).toLong()
}
