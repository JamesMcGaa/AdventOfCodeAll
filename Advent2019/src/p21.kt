import java.io.File


fun main() {
    executeP21("inputs/input21a_springscript.txt")
    executeP21("inputs/input21b_springscript.txt")
}

fun executeP21(springscriptFile: String) {
    val input =
        File("inputs/input21.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()
    val springscript =
        File(springscriptFile).readLines().map { it + "\n" }
            .map { it.map { it.code } }.flatten()
    val computer = IntcodeP17(input.toMutableMap()) // Dont forget to copy
    computer.inputBuffer.addAll(springscript.map { it.toLong() })
    computer.executeIntcode()
}