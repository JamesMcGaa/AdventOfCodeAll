import java.io.File

fun main() {
    val input =
        File("inputs/input15.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()
    val computer = IntcodeP15(input)
}

class IntcodeP17(instructions: MutableMap<Long, Long>): IntcodeP15Base(instructions) {
    override fun getInput(): Long {
        throw NotImplementedError()
    }
}