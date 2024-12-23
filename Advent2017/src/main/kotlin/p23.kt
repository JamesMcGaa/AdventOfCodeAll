import java.io.File


fun main() {
    val coprocessor = Coprocessor()
    coprocessor.execute()
    println("Part A: ${coprocessor.mulCount}")

    //        // This will run for a LONG time
    //        val debug = Coprocessor(debugMode = true)
    //        debug.execute()
    //        println("Part B: ${debug.registers["h"]}")


    /**
     * See input23_handwork.txt
     *
     * The program essentially counts the number of composite numbers
     * between the b and c initial values
     */
    var hCounter = 0L
    for (b in 108100 .. 125100 step 17) {
        inner@for (d in 2.. b) {
            if (b % d == 0 && b != d) {
                hCounter++
                break@inner
            }
        }
    }
    println(hCounter)
}

class Coprocessor(debugMode: Boolean = false) {
    val commands: List<List<String>> = File("inputs/input23.txt").readLines().map {
        it.split(" ")
    }
    var executionPointer = 0L
    val registers = mutableMapOf<String, Long>()
    var mulCount = 0L

    init {
        if (debugMode) {
            registers["a"] = 1L
        }
    }

    fun getVal(token: String): Long {
        return if (token.first().isLetter()) {
            registers.getOrDefault(token, 0)
        } else {
            token.toLong()
        }
    }

    fun execute() {
        while (executionPointer.toInt() in commands.indices) {
            val ins = commands[executionPointer.toInt()]
            val command = ins[0]
            when (command) {
                "set" -> {
                    registers[ins[1]] = getVal(ins[2])
                    executionPointer++
                }

                "sub" -> {
                    registers[ins[1]] = getVal(ins[1]) - getVal(ins[2])
                    executionPointer++
                }

                "mul" -> {
                    mulCount++
                    registers[ins[1]] = getVal(ins[1]) * getVal(ins[2])
                    executionPointer++
                }


                "jnz" -> {
                    if (getVal(ins[1]) != 0L) {
                        executionPointer += getVal(ins[2])
                    } else {
                        executionPointer++
                    }
                }
            }
        }
    }
}