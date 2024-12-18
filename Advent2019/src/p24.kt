import Utils.Coord

data class PlutoCoord(
    val depth: Int,
    val coord: Coord,
) {
    fun getPlutoNeighbors(): Set<PlutoCoord> {
        val ret = mutableSetOf<PlutoCoord>()

        coord.manhattanNeighbors.forEach { neighborCoord ->
            when {
                neighborCoord == Coord(1, 2) -> {
                    ret.add(PlutoCoord(depth + 1, Coord(0,0)))
                    ret.add(PlutoCoord(depth + 1, Coord(0,1)))
                    ret.add(PlutoCoord(depth + 1, Coord(0,2)))
                    ret.add(PlutoCoord(depth + 1, Coord(0,3)))
                    ret.add(PlutoCoord(depth + 1, Coord(0,4)))
                }
                neighborCoord == Coord(2, 3) -> {
                    ret.add(PlutoCoord(depth + 1, Coord(0,4)))
                    ret.add(PlutoCoord(depth + 1, Coord(1,4)))
                    ret.add(PlutoCoord(depth + 1, Coord(2,4)))
                    ret.add(PlutoCoord(depth + 1, Coord(3,4)))
                    ret.add(PlutoCoord(depth + 1, Coord(4,4)))
                }
                neighborCoord == Coord(3, 2) -> {
                    ret.add(PlutoCoord(depth + 1, Coord(4,0)))
                    ret.add(PlutoCoord(depth + 1, Coord(4,1)))
                    ret.add(PlutoCoord(depth + 1, Coord(4,2)))
                    ret.add(PlutoCoord(depth + 1, Coord(4,3)))
                    ret.add(PlutoCoord(depth + 1, Coord(4,4)))
                }
                neighborCoord == Coord(2, 1) -> {
                    ret.add(PlutoCoord(depth + 1, Coord(0,0)))
                    ret.add(PlutoCoord(depth + 1, Coord(1,0)))
                    ret.add(PlutoCoord(depth + 1, Coord(2,0)))
                    ret.add(PlutoCoord(depth + 1, Coord(3,0)))
                    ret.add(PlutoCoord(depth + 1, Coord(4,0)))
                }

                neighborCoord == Coord(2,2) -> Unit

                neighborCoord.x == -1 -> {
                    ret.add(PlutoCoord(depth - 1, Coord(1, 2)))
                }

                neighborCoord.x == 5 -> {
                    ret.add(PlutoCoord(depth - 1, Coord(3, 2)))
                }

                neighborCoord.y == -1 -> {
                    ret.add(PlutoCoord(depth - 1, Coord(2, 1)))
                }

                neighborCoord.y == 5 -> {
                    ret.add(PlutoCoord(depth - 1, Coord(2, 3)))
                }

                else -> {
                    ret.add(PlutoCoord(depth, neighborCoord))
                }
            }
        }
        return ret
    }
}

fun main() {
    var grid = Utils.readAsGrid("inputs/input24.txt", null, { it })
    var plutoGrid = grid.filterValues { it == '#' }.keys.map { PlutoCoord(0, it) }.groupingBy { it }.eachCount()
    Utils.printGrid(grid)

//    val biodiversitySet = mutableSetOf<Int>()
//    while (biodiversityRating(grid) !in biodiversitySet) {
//        biodiversitySet.add(biodiversityRating(grid))
//        grid = iterateBugs(grid)
//    }
//    println(biodiversityRating(grid))

    println(plutoGrid.size)
    repeat(10) {
        plutoGrid = iteratePlutoBugs(plutoGrid)
    }
    println(plutoGrid.size)
    println(plutoGrid.keys.sortedBy { it.depth })
    for (depth in -5..5) {
        println(plutoGrid.filterKeys { it.depth == depth }.keys.map { it.coord }.size)
    }
}

fun iteratePlutoBugs(plutoGrid: Map<PlutoCoord, Int>): Map<PlutoCoord, Int> {
    val ret = mutableMapOf<PlutoCoord, Int>()
    plutoGrid.keys.forEach { bugCoord ->
        bugCoord.getPlutoNeighbors().forEach { adj ->
            ret[adj] = ret.getOrDefault(adj, 0) + 1
        }
    }
    return ret.filter { entry ->
        if (entry.key in plutoGrid) {
            return@filter entry.value == 1
        } else {
            return@filter entry.value in listOf(1, 2)
        }
    }
}

fun iterateBugs(grid: MutableMap<Coord, Char>): MutableMap<Coord, Char> {
    var newGrid = grid.toMutableMap()
    newGrid.forEach { entry ->
        when (grid[entry.key]) {
            '#' -> {
                if (entry.key.manhattanNeighbors.count { grid[it] == '#' } == 1) {
                    newGrid[entry.key] = '#'
                } else {
                    newGrid[entry.key] = '.'
                }
            }

            '.' -> {
                if (entry.key.manhattanNeighbors.count { grid[it] == '#' } in listOf(1, 2)) {
                    newGrid[entry.key] = '#'
                } else {
                    newGrid[entry.key] = '.'
                }
            }

            else -> {
                throw Exception("Bad grid")
            }
        }
    }

    return newGrid
}

fun biodiversityRating(grid: MutableMap<Coord, Char>): Int {
    return grid.entries.sumOf {
        if (it.value == '.') {
            0
        } else {
            1 shl 5 * it.key.x + it.key.y
        }
    }
}
