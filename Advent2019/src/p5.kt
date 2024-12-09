import java.io.File

fun main() {
    val input = File("inputs/input5.txt").readLines()[0].split(",").map { it.toInt() }.toMutableList()
    val computer = IntcodeP5(input)
    computer.executeIntcode(1)
    println("^^^ Part A ^^^")

    println()
    println("-----------------")
    println()

    computer.reset()
    computer.executeIntcode(5)
    println("^^^ Part B ^^^")
}

class IntcodeP5(private var instructions: MutableList<Int>) {
    private val originalInstructions = instructions.toMutableList()

    private var executionPointer = 0

    /**
     * Mode is 1 for immediate, 0 for position
     */
    private fun smartAccess(idx: Int, mode: Int): Int {
        return if (mode == 1) {
            instructions[idx]
        } else {
            instructions[instructions[idx]]
        }
    }

    fun reset() {
        executionPointer = 0
        instructions = originalInstructions.toMutableList()
    }

    fun executeIntcode(inputValue: Int) {
        while (true) {
            val ins = instructions[executionPointer]
            val opcode = ins % 100
            val mode1 = (ins / 100) % 10
            val mode2 = (ins / 1000) % 10

            when (opcode) {
                // Plus
                1 -> {
                    instructions[instructions[executionPointer + 3]] =
                        smartAccess(executionPointer + 1, mode1) + smartAccess(executionPointer + 2, mode2)
                    executionPointer += 4
                }

                // Multiply
                2 -> {
                    instructions[instructions[executionPointer + 3]] =
                        smartAccess(executionPointer + 1, mode1) * smartAccess(executionPointer + 2, mode2)
                    executionPointer += 4
                }

                // Input
                3 -> {
                    instructions[instructions[executionPointer + 1]] = inputValue
                    executionPointer += 2
                }

                // Output
                4 -> {
                    println(instructions[instructions[executionPointer + 1]])
                    executionPointer += 2
                }

                // jumpIfTrue
                5 -> {
                    if (smartAccess(executionPointer + 1, mode1) != 0) {
                        executionPointer = smartAccess(executionPointer + 2, mode2)
                    } else {
                        executionPointer += 3
                    }
                }

                // jumpIfFalse
                6 -> {
                    if (smartAccess(executionPointer + 1, mode1) == 0) {
                        executionPointer = smartAccess(executionPointer + 2, mode2)
                    } else {
                        executionPointer += 3
                    }
                }

                // less than
                7 -> {
                    instructions[instructions[executionPointer + 3]] =
                        if (smartAccess(executionPointer + 1, mode1) < smartAccess(executionPointer + 2, mode2)) {
                            1
                        } else {
                            0
                        }
                    executionPointer += 4
                }

                // equals
                8 -> {
                    instructions[instructions[executionPointer + 3]] =
                        if (smartAccess(executionPointer + 1, mode1) == smartAccess(executionPointer + 2, mode2)) {
                            1
                        } else {
                            0
                        }
                    executionPointer += 4
                }

                // End
                99 -> {
                    break
                }

                else -> {
                    println(ins)
                    throw Exception("Illegal opcode")
                }
            }
        }
    }
}

