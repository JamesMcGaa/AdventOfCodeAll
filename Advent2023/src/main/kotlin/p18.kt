import java.io.File
import kotlin.math.abs


data class Coord(var x: Long, var y: Long)

fun main() {
    var isPartB = false

    repeat(2) {
        var current = Coord(0, 0)
        val board = hashMapOf<Coord, Int>(Coord(0, 0) to 1)
        val vertices = mutableListOf<Coord>()
        var amountSum = 0L
        File("inputs/input18.txt").forEachLine {
            var orientation = it.split(" ")[0]
            var amount = it.split(" ")[1].toLong()
            if (isPartB) {
                val color = it.split(" ")[2]
                amount = color.substring(2, 7).toLong(radix = 16)
                amountSum += amount
                orientation = when (color[7].digitToInt()) {
                    0 -> "R"
                    1 -> "D"
                    2 -> "L"
                    3 -> "U"
                    else -> throw Exception("Bad hex orientation")
                }
            }

            when (orientation) {
                "R" -> {
                    if (!isPartB) {
                        for (i in current.y + 1..current.y + amount) {
                            board[current.copy(y = i)] = 1
                        }
                    }
                    current = current.copy(y = current.y + amount)
                }

                "L" -> {
                    if (!isPartB) {
                        for (i in current.y - amount..current.y - 1) {
                            board[current.copy(y = i)] = 1
                        }
                    }
                    current = current.copy(y = current.y - amount)
                }

                "U" -> {
                    if (!isPartB) {
                        for (i in current.x - amount..current.x - 1) {
                            board[current.copy(x = i)] = 1
                        }
                    }
                    current = current.copy(x = current.x - amount)
                }

                "D" -> {
                    if (!isPartB) {
                        for (i in current.x + 1..current.x + amount) {
                            board[current.copy(x = i)] = 1
                        }
                    }
                    current = current.copy(x = current.x + amount)
                }

                else -> throw Exception("Bad orientation")
            }

            vertices.add(current)
        }


        if (!isPartB) {
            val xMin = board.keys.map { it.x }.min()
            val xMax = board.keys.map { it.x }.max()
            val yMin = board.keys.map { it.y }.min()
            val yMax = board.keys.map { it.y }.max()

            current = Coord(xMin - 1, yMin - 1)
            val stack = mutableListOf(current)
            val flooded = mutableSetOf<Coord>()
            while (stack.isNotEmpty()) {
                current = stack.removeLast()
                if (current in flooded) {
                    continue
                }
                if (current.x < xMin - 1 || current.x > xMax + 1 || current.y < yMin - 1 || current.y > yMax + 1) { // Truly out of bounds
                    continue
                }
                if (board[current] != null) { // Hit a trench
                    continue
                }
                flooded.add(current)
                stack.add(current.copy(x = current.x + 1))
                stack.add(current.copy(x = current.x - 1))
                stack.add(current.copy(y = current.y + 1))
                stack.add(current.copy(y = current.y - 1))
            }

            println((xMax - xMin + 3) * (yMax - yMin + 3) - flooded.size)
        } else {
            // Shoelace formula
            var area = 0L
            for (i in vertices.indices) {
                val first = vertices[i % vertices.size]
                val second = vertices[(i + 1) % vertices.size]
                area += (second.x + first.x) * (second.y - first.y)
            }

            println((abs(area) / 2L) + amountSum / 2 + 1)
            // Why the 1? We end up with 4 extra CW rotations that each add 1/4 area than CCW (which take away 1/4 area)
        }

        isPartB = true
    }
}

/**
 * Lots of people skipped the flood fill entirely for part a
 *
 * - Saw Greens theorem, picks theorem
 */