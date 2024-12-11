import java.io.File

class IntcodeP7(instructions: MutableList<Int>) : IntcodeP5(instructions) {
    private var inputCtr = 0

    override fun getInput(): Int {
        return inputs[inputCtr++]
    }

    override fun reset() {
        super.reset()
        inputCtr = 0
    }
}

const val INIITAL_INPUT = 0
const val UNUSED = -99999

fun main() {
    val input = File("inputs/input7.txt").readLines()[0].split(",").map { it.toInt() }.toMutableList()
    val partA = tryCombinations(
        6,
        prevValue = UNUSED,
        currentPhase = UNUSED,
        remainingPhases = listOf(0, 1, 2, 3, 4),
        input,
        initial = true
    )
    println("Part A: $partA")

    val partB = allPermutations(listOf(5, 6, 7, 8, 9).toSet()).maxOfOrNull { tryLoop(it, input) }
    println("Part B: $partB")
}

fun tryLoop(permutation: List<Int>, input: MutableList<Int>): Int {
    val computers = permutation.map { IntcodeP7(input).apply { inputs.add(it) } }
    computers.first().inputs.add(0)
    var currentComputerIdx = 0
    while (!computers.last().isHalted) {
        val output = computers[currentComputerIdx].executeIntcode(returnOnOutput = true)
        currentComputerIdx = (currentComputerIdx + 1) % 5
        computers[currentComputerIdx].inputs.add(output)
    }
    return computers.last().lastOutput
}

fun tryCombinations(
    left: Int,
    prevValue: Int,
    currentPhase: Int,
    remainingPhases: List<Int>,
    input: MutableList<Int>,
    initial: Boolean = false
): Int {
    val computer = IntcodeP7(input)
    computer.inputs.add(currentPhase)
    computer.inputs.add(prevValue)
    val output = if (initial) INIITAL_INPUT else computer.executeIntcode()
    if (left == 1) {
        return output
    } else {
        val phases = mutableListOf<Int>()
        for (newPhase in remainingPhases) {
            phases.add(
                tryCombinations(
                    left - 1,
                    output,
                    newPhase,
                    remainingPhases = remainingPhases.toMutableList().apply { remove(newPhase) },
                    input
                )
            )
        }
        return phases.max()
    }
}

fun <T> allPermutations(set: Set<T>): Set<List<T>> {
    if (set.isEmpty()) return emptySet()

    @Suppress("FunctionName")
    fun <T> _allPermutations(list: List<T>): Set<List<T>> {
        if (list.isEmpty()) return setOf(emptyList())

        val result: MutableSet<List<T>> = mutableSetOf()
        for (i in list.indices) {
            _allPermutations(list - list[i]).forEach { item ->
                result.add(item + list[i])
            }
        }
        return result
    }

    return _allPermutations(set.toList())
}