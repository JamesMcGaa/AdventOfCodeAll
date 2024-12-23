import main.kotlin.Utils
import main.kotlin.Utils.Coord
import java.io.File
import kotlin.collections.flatten

fun main() {
    part15A()
    part15B()
}

fun part15B() {
    val input = File("inputs/input15.txt").readLines()
    val breakIdx = input.indexOf("")

    var grid = mutableMapOf<Coord, Char>()
    input.subList(0, breakIdx).map {
        it.replace("#", "##").replace(".", "..").replace("@", "@.").replace("O", "[]")
    }.forEachIndexed { i, row ->
        row.forEachIndexed { j, ch ->
            grid[Coord(i, j)] = ch
        }
    }
    var current = Utils.findCoord('@', grid)

    Utils.printGrid(grid)

    val inst = input.subList(breakIdx + 1, input.indices.last + 1)
    val parsed = inst.map { it.toCharArray().toList() }.flatten()

    fun prep(dir: Char, spot: Coord, toBePushed: MutableSet<Coord>) {
        // Don't go back and forth in a loop
        if (spot in toBePushed) {
            return
        }

        val current = grid[spot]!!

        // These don't move
        if (current == '.') {
            return
        }
        if (current == '#') {
            return
        }

        toBePushed.add(spot)

        val nextSpot = when (dir) {
            '^' -> spot.copy(x = spot.x - 1)
            'v' -> spot.copy(x = spot.x + 1)
            '<' -> spot.copy(y = spot.y - 1)
            '>' -> spot.copy(y = spot.y + 1)
            else -> throw Exception("Bad dir char")
        }
        prep(dir, nextSpot, toBePushed)
        if (current == '[' && dir != '>') {
            prep(dir, spot.copy(y = spot.y + 1), toBePushed)
        }
        if (current == ']' && dir != '<') {
            prep(dir, spot.copy(y = spot.y - 1), toBePushed)
        }
    }

    fun push(dir: Char, spot: Coord): Coord {
        val toBePushed = mutableSetOf<Coord>()
        prep(dir, spot, toBePushed)

        // Find the destination spots - note that none of the current ones are #,
        // So we are only looking at where they end up in the end
        val goodToPush = toBePushed.map { candidate ->
            when (dir) {
                '^' -> candidate.copy(x = candidate.x - 1)
                'v' -> candidate.copy(x = candidate.x + 1)
                '<' -> candidate.copy(y = candidate.y - 1)
                '>' -> candidate.copy(y = candidate.y + 1)
                else -> throw Exception("Bad dir char")
            }
        }.all { grid[it]!! != '#' }

        if (goodToPush) {
            var newGrid = mutableMapOf<Coord, Char>()
            toBePushed.forEach { spot ->
                val next = when (dir) {
                    '^' -> spot.copy(x = spot.x - 1)
                    'v' -> spot.copy(x = spot.x + 1)
                    '<' -> spot.copy(y = spot.y - 1)
                    '>' -> spot.copy(y = spot.y + 1)
                    else -> throw Exception("Bad dir char")
                }
                newGrid[next] = grid[spot]!!

                // Clear where the 'tail' left
                if (spot !in newGrid) {
                    newGrid[spot] = '.'
                }
            }

            // Fill the rest
            grid.keys.forEach { key ->
                if (key !in newGrid) {
                    newGrid[key] = grid[key]!!
                }
            }

            grid = newGrid

            return Utils.findCoord('@', grid)
        } else {
            return current
        }
    }

    for (dir in parsed) {
        current = push(dir, current)
    }

    val partB = grid.filterValues { ch -> ch == '[' }.keys.sumOf { 100 * it.x + it.y }
    Utils.printGrid(grid)
    println("Part B: $partB")
}

fun part15A() {
    val input = File("inputs/input15.txt").readLines()
    val breakIdx = input.indexOf("")

    val grid = Utils.readAsGrid("inputs/input15.txt", 0 until breakIdx) { ch -> ch }
    val parsed = input.subList(breakIdx + 1, input.indices.last + 1).map { it.toCharArray().toList() }.flatten()
    var current = Utils.findCoord('@', grid)

    fun push(dir: Char, spot: Coord): Coord? {
        if (grid[spot] == '.') { // Noop
            return spot
        }

        if (grid[spot] == '#') { // Break chain
            return null
        }

        val nextSpot = when (dir) {
            '^' -> spot.copy(x = spot.x - 1)
            'v' -> spot.copy(x = spot.x + 1)
            '<' -> spot.copy(y = spot.y - 1)
            '>' -> spot.copy(y = spot.y + 1)
            else -> throw Exception("Bad dir char")
        }
        val result = push(dir, nextSpot)
        if (result != null) { // Empty spaces all the way down already handled
            grid[nextSpot] = grid[spot]!!
            grid[spot] = '.'
            return nextSpot
        }
        return null
    }

    for (dir in parsed) {
        current = push(dir, current) ?: current
    }

    val partA = grid.filterValues { ch -> ch == 'O' }.keys.sumOf { 100 * it.x + it.y }
    Utils.printGrid(grid)
    println("Part A: $partA")
}