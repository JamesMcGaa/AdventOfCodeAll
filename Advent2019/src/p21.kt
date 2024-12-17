import java.io.File


fun main() {
    val input =
        File("inputs/input21.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()
    val springscript = File("inputs/input21_input.txt").readLines().map { it + "\n" }.toMutableList()   .apply { add("WALK \n") }
//    val springscript = listOf(
//        "OR D J\n",
//        "WALK\n",
//    )
        .map { it.map { it.code } }.flatten()
    val computerB = IntcodeP21(input.toMutableMap()) // Dont forget to copy
    computerB.inputBuffer.addAll(springscript.map { it.toLong() })
    computerB.executeIntcode()
}

class IntcodeP21(instructions: MutableMap<Long, Long>) : IntcodeP15Base(instructions) {
    private var buffer = ""
    private var rowCtr = 0
    private var colCtr = 0
    val grid = mutableSetOf<Coord>()
    var inputBuffer = mutableListOf<Long>()
    var inputCtr = 0

    override fun getInput(): Long {
        return inputBuffer[inputCtr++]
    }

    override fun outputHook() {
        when (lastOutput) {
            10L -> {
                println(buffer)
                buffer = ""
                rowCtr++
                colCtr = 0
            }

            else -> {
                // Final output is no longer a Char - its the numeric answer
                try {
                    buffer += Char(lastOutput.toInt())
                    if (Char(lastOutput.toInt()) in listOf('^', '#')) {
                        grid.add(Coord(rowCtr, colCtr))
                    }
                    colCtr++
                } catch (_: Exception) {
                    println("Part B: $lastOutput (final output)")
                }
            }
        }
    }
}