import Utils.Coord
import Utils.Direction


fun main() {
    val grid = Utils.readAsGrid("inputs/input19.txt", null) { it }
    val start = Coord(0, 115)
    var direction = Direction.DOWN
    var output = ""

    var current = start
    var stepCounter = 0 // we arent counting the first step but we count the step at the end that goes out of bounds anyways
    while (current in grid) {
        // Follow along a straight line, until we hit a stopping point, go off grid, or go into empty space inside
        while (current in grid && grid[current] != '+' && grid[current] != ' ') {
            // Advance
            when (direction) {
                Direction.UP -> {
                    current = current.copy(x = current.x - 1)
                }

                Direction.RIGHT -> {
                    current = current.copy(y = current.y + 1)
                }

                Direction.DOWN -> {
                    current = current.copy(x = current.x + 1)
                }

                Direction.LEFT -> {
                    current = current.copy(y = current.y - 1)
                }
            }
            stepCounter++
            // Maybe add a letter
            if (grid[current]?.isLetter() == true) {
                output += grid[current]
            }
        }

        // Exited
        if (grid[current] != '+') {
            println(output)
            println(stepCounter)
            return
        }

        // Now turn
        when (direction) {
            Direction.UP, Direction.DOWN -> {
                direction = if (grid[current.copy(y = current.y + 1)] == '-' || grid[current.copy(y = current.y + 1)]?.isLetter() == true) {
                    Direction.RIGHT
                } else if (grid[current.copy(y = current.y - 1)] == '-' || grid[current.copy(y = current.y - 1)]?.isLetter() == true) {
                    Direction.LEFT
                } else {
                    println(current)
                    throw Exception("No turn")
                }
            }

            Direction.LEFT, Direction.RIGHT -> {
                direction = if (grid[current.copy(x = current.x - 1)] == '|' || grid[current.copy(x = current.x - 1)]?.isLetter() == true) {
                    Direction.UP
                } else if (grid[current.copy(x = current.x + 1)] == '|' || grid[current.copy(x = current.x + 1)]?.isLetter() == true) {
                    Direction.DOWN
                } else {
                    throw Exception("No turn")
                }
            }
        }

        // Move 1 more time to exit the inner while loop
        when (direction) {
            Direction.UP -> {
                current = current.copy(x = current.x - 1)
            }

            Direction.RIGHT -> {
                current = current.copy(y = current.y + 1)
            }

            Direction.DOWN -> {
                current = current.copy(x = current.x + 1)
            }

            Direction.LEFT -> {
                current = current.copy(y = current.y - 1)
            }
        }
        stepCounter++
    }
}