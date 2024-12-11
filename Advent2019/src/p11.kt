import java.io.File

open class IntcodeP11(private var instructions: MutableMap<Long, Long>) {

    private val originalInstructions = instructions.toMutableMap()

    private var executionPointer = 0L

    var lastOutput = 0L

    val inputs = mutableListOf<Long>()

    var isHalted = false

    var relativeBase = 0L

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
                    assert(lastOutput in listOf(0L, 1L))
                    if (isPainting) {
                        if (lastOutput == 0L) {
                            whiteSet.remove(currentPosition)
                        } else {
                            whiteSet.add(currentPosition)
                        }
                        paintedSet.add(currentPosition)
                    }
                    else {
                        if (lastOutput == 0L) {
                            direction = (direction + 3) % 4
                        } else {
                            direction = (direction + 1) % 4
                        }
                        currentPosition += directions[direction]
                    }
                    isPainting = !isPainting
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
    val paintedSet = mutableSetOf<Coord>()
    val whiteSet = mutableSetOf<Coord>()
    var currentPosition = Coord(0, 0)
    var direction = 0 // UP, RIGHT, DOWN, LEFT
    val directions = listOf(Coord(0, 1), Coord(1, 0), Coord(0, -1), Coord(-1, 0))
    var isPainting = true
    open fun getInput(): Long {
        return if (currentPosition in whiteSet) 1L else 0L
    }
}


fun main() {
    val input =
        File("inputs/input11.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()
    val computerA = IntcodeP11(input)
    computerA.executeIntcode()
    println("Part A: ${computerA.paintedSet.size}")

    val computerB = IntcodeP11(input)
    computerB.whiteSet.add(Coord(0,0))
    computerB.executeIntcode()
    val minX = computerB.whiteSet.minOf{it.x}
    val maxX = computerB.whiteSet.maxOf{it.x}
    val minY = computerB.whiteSet.minOf{it.y}
    val maxY = computerB.whiteSet.maxOf{it.y}
    println("--------------Part B-----------")
    for (i in minX..maxX) {
        var row = ""
        for (j in minY .. maxY) {
            if (Coord(i,j) in computerB.whiteSet) {
                row += "#"
            } else {
                row += " "
            }
        }
        println(row)
    }
}