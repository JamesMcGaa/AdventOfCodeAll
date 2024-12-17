import java.io.File
import Utils.Coord

fun main() {
    val input =
        File("inputs/input19.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()
    val computer = IntcodeP19(input.toMutableMap()) // Dont forget to copy
    val grid = mutableMapOf<Coord, Char>()
    val searchSize = 1100 // Arbitrary
    val boxSize = 100
    for (i in 0..searchSize) {
        for (j in 0..searchSize) {
            computer.inputBuffer.addAll(listOf(i.toLong(), j.toLong()))
            computer.executeIntcode()
            grid[Coord(i, j)] = if (computer.lastOutput == 1L) '#' else '.'
            computer.reset()

        }
    }

    val partA = grid.filterKeys { it.x < 50 && it.y < 50 }.values.count { it == '#' }
    val partB = grid.keys.minOf {
        var fits = grid[it.copy(x = it.x + boxSize - 1)] == '#' && grid[it.copy(y = it.y + boxSize - 1)] == '#'
        if (fits) it.x * 10000 + it.y else Int.MAX_VALUE
    }

    println("Part A: $partA")
    println("Part B: $partB")
}

class IntcodeP19(instructions: MutableMap<Long, Long>) : IntcodeP15Base(instructions) {
    var inputBuffer = mutableListOf<Long>()
    var inputCtr = 0

    override fun getInput(): Long {
        return inputBuffer[inputCtr++]
    }

    override fun resetHook() {
        super.resetHook()
        inputCtr = 0
        inputBuffer.clear()
    }
}