package main.kotlin


fun main() {
    val grid = Utils.readAsGrid("inputs/input20.txt", null) { it }
    val start = Utils.findCoord('S', grid)
    val end = Utils.findCoord('E', grid)

    val distsFromStart = Utils.generalizedBFS(
        grid, start,
        isLegal = { coord, grid -> coord in grid && grid[coord] != '#' },
        neighbors = { coord, _ -> coord.manhattanNeighbors },
    )
    val distsFromEnd = Utils.generalizedBFS(
        grid, end,
        isLegal = { coord, grid -> coord in grid && grid[coord] != '#' },
        neighbors = { coord, _ -> coord.manhattanNeighbors },
    )
    val bestFairTime = distsFromEnd[start]!!

    fun runForCheatDurationTargetSavings(cheatDuration: Int, targetSavings: Int): Long {
        var counter = 0L
        for (cStart in grid.keys) {
            for (cEnd in grid.keys) {
                if (grid[cStart] != '#' && grid[cEnd] != '#' && (cEnd - cStart).manhattanDist <= cheatDuration) { // Valid cheat
                    if ((cEnd - cStart).manhattanDist + distsFromStart[cStart]!! + distsFromEnd[cEnd]!! <= bestFairTime - targetSavings) {
                        counter++
                    }
                }
            }
        }
        return counter
    }

    println("Part A: ${runForCheatDurationTargetSavings(2, 100)}")
    println("Part B: ${runForCheatDurationTargetSavings(20, 100)}")
}
