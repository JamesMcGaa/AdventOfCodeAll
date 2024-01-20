import kotlin.math.absoluteValue

fun main() {
    val input =
        "R4, R5, L5, L5, L3, R2, R1, R1, L5, R5, R2, L1, L3, L4, R3, L1, L1, R2, R3, R3, R1, L3, L5, R3, R1, L1, R1, R2, L1, L4, L5, R4, R2, L192, R5, L2, R53, R1, L5, R73, R5, L5, R186, L3, L2, R1, R3, L3, L3, R1, L4, L2, R3, L5, R4, R3, R1, L1, R5, R2, R1, R1, R1, R3, R2, L1, R5, R1, L5, R2, L2, L4, R3, L1, R4, L5, R4, R3, L5, L3, R4, R2, L5, L5, R2, R3, R5, R4, R2, R1, L1, L5, L2, L3, L4, L5, L4, L5, L1, R3, R4, R5, R3, L5, L4, L3, L1, L4, R2, R5, R5, R4, L2, L4, R3, R1, L2, R5, L5, R1, R1, L1, L5, L5, L2, L1, R5, R2, L4, L1, R4, R3, L3, R1, R5, L1, L4, R2, L3, R5, R3, R1, L3"
//    printDist("R2, L3")
//    printDist("R2, R2, R2")
//    printDist("R5, L5, R5, R3")
    printDist(input)
}

fun printDist(input: String) {
    val instructions = input.split(", ")

    var currentDir = Direction.UP
    var pos = Coord(0, 0)
    val seen = mutableSetOf(pos)
    var partBFound = false
    for (ins in instructions) {
        val direction = ins[0]
        val dist = ins.substring(1).toInt()
        currentDir = Direction.entries[Math.floorMod(direction.toDirection() + currentDir.ordinal, 4)]
        repeat(dist) {
            pos = when (currentDir) {
                Direction.UP -> pos.copy(y = pos.y + 1)
                Direction.RIGHT -> pos.copy(x = pos.x + 1)
                Direction.DOWN -> pos.copy(y = pos.y - 1)
                Direction.LEFT -> pos.copy(x = pos.x - 1)
            }
            if (seen.contains(pos) && !partBFound) {
                partBFound = true
                println(pos.originManhattanDist)
            }
            seen.add(pos)
        }
    }
    println(pos.originManhattanDist)
}

fun Char.toDirection(): Int {
    return if (this == 'R') 1 else -1
}

// Starts UP, R is CCW or positive
enum class Direction(val numericValue: Int) {
    UP(0), RIGHT(1), DOWN(2), LEFT(3),
}

data class Coord(
    val x: Int, var y: Int
) {
    val originManhattanDist = x.absoluteValue + y.absoluteValue
}