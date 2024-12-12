import java.io.File

fun main() {
    val input =
        File("inputs/input13.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()
    val computer = IntcodeP13(input)
    computer.executeIntcode()
    val bricks = computer.grid.filterValues { it == 2L }.size
    println("Part A: $bricks")

    computer.reset()
    computer.instructions[0L] = 2L // Add quarters
    val finalScore = computer.executeIntcode()
    println("Part B: $finalScore")
}

open class IntcodeP13(var instructions: MutableMap<Long, Long>) {

    private val originalInstructions = instructions.toMutableMap()

    private var executionPointer = 0L

    private var lastOutput = 0L

    val inputs = mutableListOf<Long>()

    private var isHalted = false

    private var relativeBase = 0L

    /**
     * Mode is 1 for immediate, 0 for position
     */
    private fun smartAccess(idx: Long, mode: Long): Long {
        if (idx < 0) {
            throw Exception("Negative memory index")
        }
        return when (mode) {
            1L -> { // immediate mode
                instructions.getOrDefault(idx, 0)
            }

            0L -> { // position
                instructions.getOrDefault(instructions[idx], 0)
            }

            2L -> { // relative
                instructions.getOrDefault(instructions[idx]!! + relativeBase, 0)
            }

            else -> {
                throw Exception("Improper Mode")
            }
        }
    }

    private fun smartAccessLiteral(idx: Long, mode: Long): Long {
        if (idx < 0) {
            throw Exception("Negative memory index")
        }
        return when (mode) {
            1L -> { // immediate mode
                instructions.getOrDefault(idx, 0)
            }

            0L -> { // position
                instructions.getOrDefault(idx, 0)
            }

            2L -> { // relative
                instructions.getOrDefault(idx, 0) + relativeBase
            }

            else -> {
                throw Exception("Improper Mode")
            }
        }
    }


    open fun reset() {
        inputs.clear()
        executionPointer = 0
        instructions = originalInstructions.toMutableMap()
        relativeBase = 0
        isHalted = false
        lastOutput = 0L
        grid.clear()
        outputBuffer.clear()
    }


    open fun executeIntcode(shouldPrintOutputs: Boolean = false, returnOnOutput: Boolean = false): Long {
        while (true) {
            val ins = instructions[executionPointer]!!
            assert(ins > 0L)
            val opcode = ins % 100L
            val mode1 = (ins / 100L) % 10L
            val mode2 = (ins / 1000L) % 10L
            val mode3 = (ins / 10000L) % 10L
            when (opcode) {
                // Plus
                1L -> {
                    instructions[smartAccessLiteral(executionPointer + 3, mode3)] =
                        smartAccess(executionPointer + 1, mode1) + smartAccess(executionPointer + 2, mode2)
                    executionPointer += 4
                }

                // Multiply
                2L -> {
                    instructions[smartAccessLiteral(executionPointer + 3, mode3)] =
                        smartAccess(executionPointer + 1, mode1) * smartAccess(executionPointer + 2, mode2)
                    executionPointer += 4
                }

                // Input
                3L -> {
                    instructions[smartAccessLiteral(executionPointer + 1, mode1)] = getInput()
                    executionPointer += 2
                }

                // Output
                4L -> {
                    lastOutput = smartAccess(executionPointer + 1, mode1)
                    if (shouldPrintOutputs) println("Output: $lastOutput")
                    executionPointer += 2
                    if (returnOnOutput) {
                        return lastOutput
                    }

                    //------P11
                    outputBuffer.add(lastOutput)
                    if (outputBuffer.size == 3) {
                        val coord = Coord(outputBuffer[0].toInt(), outputBuffer[1].toInt())
                        if (coord == Coord(-1,0)) {
                            println("Score: ${outputBuffer[2]}")
                        }
                        grid[coord] = outputBuffer[2]
                        outputBuffer.clear()
                    }
                    //end ------P11
                }

                // jumpIfTrue
                5L -> {
                    if (smartAccess(executionPointer + 1, mode1) != 0L) {
                        executionPointer = smartAccess(executionPointer + 2, mode2)
                    } else {
                        executionPointer += 3
                    }
                }

                // jumpIfFalse
                6L -> {
                    if (smartAccess(executionPointer + 1, mode1) == 0L) {
                        executionPointer = smartAccess(executionPointer + 2, mode2)
                    } else {
                        executionPointer += 3
                    }
                }

                // less than
                7L -> {
                    instructions[smartAccessLiteral(executionPointer + 3, mode3)] =
                        if (smartAccess(executionPointer + 1, mode1) < smartAccess(executionPointer + 2, mode2)) {
                            1
                        } else {
                            0
                        }
                    executionPointer += 4
                }

                // equals
                8L -> {
                    instructions[smartAccessLiteral(executionPointer + 3, mode3)] =
                        if (smartAccess(executionPointer + 1, mode1) == smartAccess(executionPointer + 2, mode2)) {
                            1
                        } else {
                            0
                        }
                    executionPointer += 4
                }

                9L -> {
                    relativeBase += smartAccess(executionPointer + 1, mode1)
                    executionPointer += 2
                }

                // End
                99L -> {
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


    // New to P11
    val grid = mutableMapOf<Coord, Long>()
    private val outputBuffer = mutableListOf<Long>()

    open fun getInput(): Long {
        return if (grid.filter { entry -> entry.value == 4L }.keys.toList()
                .first().x > grid.filter { entry -> entry.value == 3L }.keys.toList().first().x
        ) {
            1
        } else if (grid.filter { entry -> entry.value == 4L }.keys.toList()
                .first().x < grid.filter { entry -> entry.value == 3L }.keys.toList().first().x
        ) {
            -1
        } else {
            0
        }
    }
}