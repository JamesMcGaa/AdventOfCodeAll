import java.io.File

fun main() {
    val input =
        File("inputs/input25.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()
    val computer = IntcodeP25(input.toMutableMap()) // Dont forget to copy
    File("inputs/input25_input.txt").readLines().forEach {
        computer.inputBuffer.addAll(stringToInput(it))
    }
    computer.executeIntcode()
}

fun stringToInput(inpStr: String): List<Long> {
    return inpStr.map { it.code.toLong() }.toMutableList().apply { add(10L) }
}

class IntcodeP25(instructions: MutableMap<Long, Long>) : IntcodeP15Base(instructions) {
    private var buffer = ""
    private var rowCtr = 0
    private var colCtr = 0
    var inputBuffer = mutableListOf<Long>()
    var inputCtr = 0

    var isInInputMode = false
    var dropCtr = 0
    val items = listOf("mug", "easter egg", "asterisk", "klein bottle", "cake", "jam", "tambourine", "polygon")

    override fun getInput(): Long {
        if (isInInputMode) {
            val padded = dropCtr.toString(2).padStart(8, '0')
            if (dropCtr != 0) {
                val previous = (dropCtr-1).toString(2).padStart(8, '0')
                previous.forEachIndexed { idx, ch ->
                    if (ch == '1') {
                        inputBuffer.addAll(stringToInput("take ${items[idx]}"))
                    }
                }
            }
            padded.forEachIndexed { idx, ch ->
                if (ch == '1') {
                    inputBuffer.addAll(stringToInput("drop ${items[idx]}"))
                }
            }
            inputBuffer.addAll(stringToInput("east"))
            isInInputMode = false
            dropCtr++
        } else if (inputCtr !in inputBuffer.indices) {
            print("Enter a value: ")
            val line = readln()
            inputBuffer.addAll(stringToInput(line))
        }
        return inputBuffer[inputCtr++]
    }

    override fun outputHook() {
        when (lastOutput) {
            10L -> {
                println(buffer)
                if (buffer.contains("pressure-sensitive")) {
                    isInInputMode = true
                }
                buffer = ""
                rowCtr++
                colCtr = 0
            }

            else -> {
                buffer += Char(lastOutput.toInt())
                colCtr++
            }
        }
    }
}