import Utils.Coord
import kotlin.math.abs

fun main() {
    val INPUT = 368078
    runP3(1)
    runP3(12)
    runP3(23)
    runP3(1024)
    runP3(INPUT)
    runP3B(INPUT)
}

fun runP3(input: Int) {
    if (input <= 1) return println(0)
    var outerRadius = 1
    while ((2 * outerRadius - 1) * (2 * outerRadius - 1) < input) {
        outerRadius += 1
    }
    val innerRadius = outerRadius - 1
    val outerDiameter = 2 * outerRadius - 1
    val innerDiameter = 2 * innerRadius - 1
    val innerSquares = innerDiameter * innerDiameter

    val rowProgress = Math.floorMod(input - innerSquares, outerDiameter - 1)
    val vShaped = abs(innerRadius - rowProgress)
    println(vShaped + innerRadius)
}

fun runP3B(input: Int) {
    val grid = mutableMapOf<Coord, Int>()
    var current = Coord(0,0)
    grid[current] = 1
    var currentDirection = Direction.DOWN
    while (true) {
        val neighbors = listOf(
            current.copy(x = current.x + 1),
            current.copy(x = current.x - 1),
            current.copy(y = current.y + 1),
            current.copy(y = current.y - 1),
        ).filter {grid.contains(it)}

        if (neighbors.size <= 1) {
            currentDirection = Direction.entries[(currentDirection.innerVal + 1) % 4]
        }

        current = when (currentDirection) {
            Direction.DOWN -> current.copy(x = current.x + 1)
            Direction.RIGHT -> current.copy(y = current.y + 1)
            Direction.UP -> current.copy(x = current.x - 1)
            Direction.LEFT -> current.copy(y = current.y - 1)
        }

        val adjacent = mutableListOf<Coord>()
        for (xo in -1..1) {
            for (yo in -1..1) {
                adjacent.add(current.copy(x = current.x + xo, y = current.y + yo))
            }
        }
        grid[current] = adjacent.filter {grid.contains(it)}.sumOf { grid[it]!! }
//        println(current)
//        println(grid[current]!!)
        if (grid[current]!! > input) {
            println(grid[current]!!)
            return
        }

    }
}

enum class Direction(val innerVal: Int) {
    DOWN(0), RIGHT(1), UP(2), LEFT(3),
}