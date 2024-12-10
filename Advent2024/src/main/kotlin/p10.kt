import java.io.File

val INPUT_10 = File("inputs/input10.txt").readLines()

fun main() {
    val grid = mutableMapOf<Coord, Int>()
    val potentialStarts = mutableListOf<Coord>()
    for (i in INPUT_10.indices) {
        for (j in INPUT_10.indices) {
            grid[Coord(i, j)] = INPUT_10[i][j].toString().toInt()
            if (grid[Coord(i, j)] == 0) {
                potentialStarts.add(Coord(i, j))
            }
        }
    }

    val partA = potentialStarts.sumOf { dfs(it, -1, grid, mutableSetOf()) }
    val partB = potentialStarts.sumOf { dfs(it, -1, grid, mutableSetOf(), isPartB = true) }
    println("Part A: $partA")
    println("Part B: $partB")
}

fun dfs(current: Coord, prevVal: Int, grid: Map<Coord, Int>, seen: MutableSet<Coord>, isPartB: Boolean = false): Int {
    if (current in seen || current.x < 0 || current.y < 0 || current.x >= INPUT_10.size || current.y >= INPUT_10[0].length || grid[current]!! != prevVal + 1) {
        return 0
    } else if (grid[current]!! == 9) {
        if (!isPartB) {
            seen.add(current)
        }
        return 1
    } else {
        if (!isPartB) {
            seen.add(current)
        }
        return dfs(current.copy(x = current.x - 1), prevVal + 1, grid, seen, isPartB) +
                dfs(current.copy(x = current.x + 1), prevVal + 1, grid, seen, isPartB) +
                dfs(current.copy(y = current.y - 1), prevVal + 1, grid, seen, isPartB) +
                dfs(current.copy(y = current.y + 1), prevVal + 1, grid, seen, isPartB)
    }

}
