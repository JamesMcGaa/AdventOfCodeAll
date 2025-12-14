import java.io.File
import Utils.Coord
import Utils.product

fun main() {
    println("Part A: ${p6a("inputs/input6.txt")}")
    println("Part A: ${p6b("inputs/input6.txt")}")
}

fun p6a(filename: String): Long {
    val input = File(filename).readLines()
    val rows =
        input.subList(0, input.size - 1).map { row ->
            row.split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        }
    val ops = input.last().split(" ").filter { it.isNotBlank() }
    var counter = 0L

    ops.forEachIndexed { col, op ->
        var base = when (op) {
            "*" -> 1L
            "+" -> 0L
            else -> throw Exception()
        }

        for (row in rows) {
            when (op) {
                "*" -> base *= row[col]
                "+" -> base += row[col]
                else -> throw Exception()
            }
        }

        counter += base
    }

    return counter
}

fun p6b(filename: String): Long {
    val input = File(filename).readLines()
    val rows = Utils.readAsGrid(filename, 0..input.size - 2) { it }
    val grid = Utils.rotateCounterclockwise(rows)
    val yMin = grid.keys.minOf { it.y }
    val yMax = grid.keys.maxOf { it.y }
    val xMin = grid.keys.minOf { it.x }
    val xMax = grid.keys.maxOf { it.x }
    val ops = input.last().split(" ").filter { it.isNotBlank() }.reversed()
    var counter = 0L

    val groups = mutableListOf<List<Long>>()
    val numbersInGroup = mutableListOf<Long>()
    for (row in xMin..xMax) {
        var number = ""
        for (col in yMin..yMax) {
            val char = grid[Coord(row, col)]!!
            if (char.isDigit()) {
                number += char
            }
        }

        if (number.isBlank()) {
            groups.add(numbersInGroup.toList())
            numbersInGroup.clear()
        } else {
            numbersInGroup.add(number.toLong())
        }
    }
    groups.add(numbersInGroup.toList())

    ops.forEachIndexed { col, op ->
        counter += when (op) {
            "*" -> groups[col].product()
            "+" -> groups[col].sum()
            else -> throw Exception()
        }
    }

    return counter
}