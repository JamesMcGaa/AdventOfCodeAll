import java.io.File


fun main() {
    DuetComputer().execute()

    val program0 = DuetComputer(0, isPartB = true)
    val program1 = DuetComputer(1, isPartB = true)
    program0.other = program1
    program1.other = program0

    // Does it matter which order we execute in?
    program0.execute()
    program1.execute()
    println("Part B: ${program1.sendCount}")
}

class DuetComputer(p: Long = 0L, val isPartB: Boolean = false) {
    val commands: List<List<String>> = File("inputs/input18.txt").readLines().map {
        it.split(" ")
    }
    var executionPointer = 0L
    val registers = mutableMapOf<String, Long>("p" to p)
    var lastSound = 0L
    val inputBuffer = mutableListOf<Long>()
    var isHalted = false
    var sendCount = 0
    lateinit var other: DuetComputer

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
                "snd" -> {
                    if (isPartB) {
                        sendCount++
                        other.inputBuffer.add(getVal(ins[1]))
                        if (other.isHalted) {
                            other.isHalted = false
                            other.execute()
                        }
                        executionPointer++
                    } else {
                        lastSound = getVal(ins[1])
                        executionPointer++
                    }
                }

                "set" -> {
                    registers[ins[1]] = getVal(ins[2])
                    executionPointer++
                }

                "add" -> {
                    registers[ins[1]] = getVal(ins[1]) + getVal(ins[2])
                    executionPointer++
                }

                "mul" -> {
                    registers[ins[1]] = getVal(ins[1]) * getVal(ins[2])
                    executionPointer++
                }

                "mod" -> {
                    registers[ins[1]] = (getVal(ins[1]) + getVal(ins[2])) % getVal(ins[2])
                    executionPointer++
                }

                "rcv" -> {
                    if (isPartB) {
                        if (inputBuffer.isEmpty()) {
                            isHalted = true
                            return
                        }
                        registers[ins[1]] = inputBuffer.removeFirst()
                        executionPointer++
                    } else {
                        if (getVal(ins[1]) != 0L) {
                            println("Part A: $lastSound")
                            return
                        }
                        executionPointer++
                    }
                }

                "jgz" -> {
                    if (getVal(ins[1]) > 0L) {
                        executionPointer += getVal(ins[2])
                    } else {
                        executionPointer++
                    }
                }
            }
        }
    }
}