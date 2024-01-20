import java.io.File

fun main() {
    val mappingA = mutableMapOf(
        Coord(-1, 1) to '1',
        Coord(0, 1) to '2',
        Coord(1, 1) to '3',
        Coord(-1, 0) to '4',
        Coord(0, 0) to '5',
        Coord(1, 0) to '6',
        Coord(-1, -1) to '7',
        Coord(0, -1) to '8',
        Coord(1, -1) to '9',
    )

    val mappingB = mutableMapOf(
        Coord(-1, 1) to '2',
        Coord(0, 1) to '3',
        Coord(1, 1) to '4',
        Coord(-1, 0) to '6',
        Coord(0, 0) to '7',
        Coord(1, 0) to '8',
        Coord(-1, -1) to 'A',
        Coord(0, -1) to 'B',
        Coord(1, -1) to 'C',
        Coord(0, 2) to '1',
        Coord(0, -2) to 'D',
        Coord(-2, 0) to '5',
        Coord(2, 0) to '9',
    )

    runForMapping(mappingA)
    runForMapping(mappingB)
}
fun runForMapping(mapping: MutableMap<Coord, Char>) {
    val commands = File("inputs/input2.txt").readLines()
    var pos = Coord(0, 0)
    var code = ""
    commands.forEach { line ->
        line.forEach { ch ->
            val newPos = when (ch) {
                'U' -> pos.copy(y = pos.y + 1)
                'D' -> pos.copy(y = pos.y - 1)
                'L' -> pos.copy(x = pos.x - 1)
                'R' -> pos.copy(x = pos.x + 1)
                else -> throw Exception("Invalid character")
            }
            if (mapping.contains(newPos)) {
                pos = newPos
            }
        }
        code += mapping[pos]!!
    }
    println(code)
}

