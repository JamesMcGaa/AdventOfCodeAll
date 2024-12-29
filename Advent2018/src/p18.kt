import Utils.Coord

fun main() {
    var grid = Utils.readAsGrid("inputs/input18.txt", null) { it }

    val OPEN = '.'
    val TREES = '|'
    val LUMBERYARD = '#'
    val seen = mutableMapOf(grid to 0)
    repeat(1000000000) {
        val newGrid = mutableMapOf<Coord, Char>()
        grid.forEach { coord, ch ->
            newGrid[coord] = when (ch) {
                OPEN -> {
                    val adjTrees = coord.fullNeighbors.map { grid[it] }.count { it == TREES }
                    if (adjTrees >= 3) TREES else OPEN
                }
                TREES -> {
                    val adjLumberyards = coord.fullNeighbors.map { grid[it] }.count { it == LUMBERYARD   }
                    if (adjLumberyards >= 3) LUMBERYARD else TREES
                }
                LUMBERYARD -> {
                    val adj = coord.fullNeighbors.map { grid[it] }
                    if (TREES in adj && LUMBERYARD in adj) LUMBERYARD else OPEN
                }
                else -> throw Exception("Invalid grid entry")
            }
        }
        grid = newGrid

        if (it == 9) {
            val resources = grid.count { entry -> entry.value == TREES } * grid.count { entry -> entry.value == LUMBERYARD }
            println("Part A: $resources")
        }

        if (grid in seen) {
            val cycleLen = it - seen[grid]!!
            val remaining = (1000000000 - 1 - it) % cycleLen
            val grid = seen.filterValues { it == seen[grid]!! + remaining}.toList().first().first
            val resources = grid.count { entry -> entry.value == TREES } * grid.count { entry -> entry.value == LUMBERYARD }
            println("Part B: $resources")
            return
        }
        seen[grid] = it
    }
}