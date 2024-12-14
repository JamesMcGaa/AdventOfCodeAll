import java.io.File

const val WALL = 0L
val ORIGIN = Coord(0, 0)
const val OPEN_SPACE = 1L
const val OXYGEN = 2L

fun main() {
    val input =
        File("inputs/input15.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()
    val computer = IntcodeP15(input)

    val coordToMaze = mutableMapOf(ORIGIN to OPEN_SPACE)
    val coordToComputer = mutableMapOf(ORIGIN to computer)
    val stack = mutableListOf(ORIGIN)
    val seen = mutableSetOf<Coord>()
    while (stack.isNotEmpty()) {
        val current = stack.removeLast()

        if (current in seen) {
            continue
        } else {
            seen.add(current)
        }

        for (idx in 1L..4L) {
            val newComputer = coordToComputer[current]!!.copy().apply {
                this.hardcodedInput = idx
            }
            val attemptToTraverse = newComputer.executeIntcode(returnOnOutput = true)
            val attemptedNeighbor = current.neighbors[idx.toInt() - 1]
            coordToMaze[attemptedNeighbor] = attemptToTraverse
            if (attemptToTraverse in listOf(OPEN_SPACE, OXYGEN)) {
                coordToComputer[attemptedNeighbor] = newComputer.copy()
                stack.add(attemptedNeighbor)
            }
        }
    }

    val OXYGEN_EXIT = coordToMaze.entries.first { it.value == OXYGEN }.key

    val minX = coordToMaze.keys.minOf { it.x }
    val minY = coordToMaze.keys.minOf { it.y }
    val maxX = coordToMaze.keys.maxOf { it.x }
    val maxY = coordToMaze.keys.maxOf { it.y }
    for (i in minX..maxX) {
        var line = ""
        for (j in minY..maxY) {
            if (i == 0 && j == 0) {
                line += "S"
                continue
            }
            line += when (coordToMaze[Coord(i, j)]) {
                0L -> "."
                1L -> "#"
                2L -> "E"
                else -> ""
            }
        }
        println(line)
    }

    val partA = p15aBFS(coordToMaze, ORIGIN, OXYGEN_EXIT, isPartB = false)
    val partB = p15aBFS(coordToMaze, OXYGEN_EXIT, null, isPartB = true)
    println("Part A: $partA")
    println("Part B: $partB")
}

fun p15aBFS(graph: Map<Coord, Long>, start: Coord, exit: Coord?, isPartB: Boolean = false): Int {
    var frontier = mutableSetOf(start)
    var iterations = 0
    val seen = mutableSetOf<Coord>()
    while (frontier.isNotEmpty()) {
        val newFronter = mutableSetOf<Coord>()
        iterations++
        for (element in frontier) {
            element.neighbors.forEach { neighbor ->
                if (neighbor !in seen && neighbor in graph.keys && graph[neighbor] != WALL) {
                    newFronter.add(neighbor)
                    seen.add(neighbor)
                    if (!isPartB && neighbor == exit) {
                        return iterations // How many steps it took before we made the final step to oxygen
                    }
                }

            }

        }
        frontier = newFronter
    }
    return iterations - 1 // Here iterations implicitly counts the number of times to fill starting from 0 (the oxygen room)
}

class IntcodeP15(instructions: MutableMap<Long, Long>) : IntcodeP15Base(instructions) {
    var hardcodedInput = -730L
    override fun getInput(): Long {
        return hardcodedInput
    }

    fun copy(): IntcodeP15 {
        return IntcodeP15(instructions.toMutableMap()).apply {
            relativeBase =
                this@IntcodeP15.relativeBase
            executionPointer = this@IntcodeP15.executionPointer
        }
    }
}


/**
 * Leave output to the clients
 *
 * abstract fun getInput and open fun reset
 */
abstract class IntcodeP15Base(var instructions: MutableMap<Long, Long>) {
    private val originalInstructions = instructions.toMutableMap()

    var executionPointer = 0L

    var lastOutput = 0L

    private var isHalted = false

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
                instructions.getOrDefault(idx, 0L)
            }

            0L -> { // position
                instructions.getOrDefault(instructions[idx], 0L)
            }

            2L -> { // relative
                instructions.getOrDefault(instructions[idx]!! + relativeBase, 0L)
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
                instructions.getOrDefault(idx, 0L)
            }

            0L -> { // position
                instructions.getOrDefault(idx, 0L)
            }

            2L -> { // relative
                instructions.getOrDefault(idx, 0L) + relativeBase
            }

            else -> {
                throw Exception("Improper Mode")
            }
        }
    }

    abstract fun getInput(): Long

    open fun outputHook() = Unit

    @Suppress("unused")
    open fun reset() {
        executionPointer = 0
        instructions = originalInstructions.toMutableMap()
        relativeBase = 0L
        isHalted = false
        lastOutput = 0L
    }


    fun executeIntcode(shouldPrintOutputs: Boolean = false, returnOnOutput: Boolean = false): Long {
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
                    outputHook()
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
}