import Utils.Coord
import java.io.File


fun main() {
    val CLAY = '▓'
    val STILL_WATER = ','
    val VERTICAL_FLOW = '*'
    val HORIZONTAL_FLOW = '-'
    val grid = mutableMapOf<Coord, Char>()
    File("inputs/input17.txt").forEachLine {
        val lhs = it.split(" ")[0]
        val type = lhs.first()
        val fixed = lhs.filter { it.isDigit() }.toInt()
        val range = it.split(" ")[1].filter { it.isDigit() || it == '.' }.split("..").map { it.toInt() }
        // We operate on flipped x,y
        if (type == 'x') {
            for (x in range[0]..range[1]) {
                grid[Coord(x, fixed)] = CLAY
            }
        } else {
            for (y in range[0]..range[1]) {
                grid[Coord(fixed, y)] = CLAY
            }
        }
    }

    val minX = grid.keys.minOf { it.x }
    val maxX = grid.keys.maxOf { it.x }
    fun Coord.inBounds(): Boolean {
        return this.x in minX..maxX
    }

    val origin = Coord(0, 500)
    grid[origin] = 'W' // Source

    val activeWaters = mutableSetOf(origin)
    while (activeWaters.isNotEmpty()) {
        val active = activeWaters.first()
        activeWaters.remove(active)
        if (active.x > maxX) {
            continue // Water falls out of grid
        }

        // Add free floating downs
        if (active.down !in grid) {
            activeWaters.add(active.down)
            grid[active.down] = VERTICAL_FLOW
        }
        // We are supported and can flow horizontally
        else if (grid[active.down] in setOf(STILL_WATER, CLAY)) {
            grid[active] = HORIZONTAL_FLOW
            val horizontal = mutableSetOf(active)
            var shouldBecomeStill = true

            var current = active
            // Flow over unmarked and over horizontals (since we could potentially still them now
            while (grid[current.right] in setOf(HORIZONTAL_FLOW, VERTICAL_FLOW, null)) {
                current = current.right
                grid[current] = HORIZONTAL_FLOW
                horizontal.add(current)
                if (current.down !in grid || grid[current.down] == VERTICAL_FLOW) {
                    grid[current] = VERTICAL_FLOW
                    activeWaters.add(current)
                    shouldBecomeStill = false
                    break
                }
            }
            current = active
            while (grid[current.left] in setOf(HORIZONTAL_FLOW, VERTICAL_FLOW, null)) {
                current = current.left
                grid[current] = HORIZONTAL_FLOW
                horizontal.add(current)
                if (current.down !in grid || grid[current.down] == VERTICAL_FLOW) {
                    grid[current] = VERTICAL_FLOW
                    activeWaters.add(current)
                    shouldBecomeStill = false
                    break
                }
            }

            if (shouldBecomeStill) {
                horizontal.forEach {
                    grid[it] = STILL_WATER
                }
                activeWaters.addAll(horizontal.map{it.copy(x = it.x - 1)}.filter {grid[it] == VERTICAL_FLOW})
            }
        }
    }

    Utils.printGrid(grid, "outputs/output17.txt")
    println("Part A: ${grid.count { it.key.inBounds() && it.value in setOf(HORIZONTAL_FLOW, STILL_WATER, VERTICAL_FLOW)  }}")
    println("Part B: ${grid.count { it.key.inBounds() && it.value in setOf(STILL_WATER) }}")

}