import java.io.File

fun main() {
    val input = File("inputs/input5.txt").readLines()[0].split(",").map { it.toInt() }.toMutableList()
    val computer = IntcodeP5(input)
    computer.inputs.add(1)
    computer.executeIntcode(shouldPrintOutputs = true)
    println("^^^ Part A ^^^")

    println()
    println("-----------------")
    println()

    computer.reset()
    computer.inputs.add(5)
    computer.executeIntcode(shouldPrintOutputs = true)
    println("^^^ Part B ^^^")
}

open class IntcodeP5(private var instructions: MutableList<Int>) {
    private val originalInstructions = instructions.toMutableList()

    private var executionPointer = 0

    var lastOutput = 0

    val inputs = mutableListOf<Int>()

    var isHalted = false

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

    open fun reset() {
        inputs.clear()
        executionPointer = 0
        instructions = originalInstructions.toMutableList()
    }

    open fun getInput(): Int {
        return inputs[0]
    }

    open fun executeIntcode(shouldPrintOutputs: Boolean = false, returnOnOutput: Boolean = false): Int {
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
                    instructions[instructions[executionPointer + 1]] = getInput()
                    executionPointer += 2
                }

                // Output
                4 -> {
                    lastOutput = instructions[instructions[executionPointer + 1]]
                    if (shouldPrintOutputs) println(lastOutput)
                    executionPointer += 2
                    if (returnOnOutput) {
                        return lastOutput
                    }
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
                    isHalted = true
                    return lastOutput
                }

                else -> {
                    println(ins)
                    throw Exception("Illegal opcode")
                }
            }
        }
    }
}

