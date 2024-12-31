import java.io.File

fun main() {
    run21(0)
}

/**
 * Inspecting the input we can see that we perform a ton of operations
 * and eventually compare F to A (our input and unchanged)
 *
 * Given this, we can inspect these compared F values and note that we eventually hit a cycle
 * Thus our answers are the first and last values in this cycle
 */
fun run21(aOverride: Int) {
    val input = File("inputs/input21.txt").readLines()
    var instrIdx = input.first().filter { it.isDigit() }.toInt()
    val program = input.subList(1, input.size).map { line ->
        val split = line.split(" ")
        val op = split[0]
        val opInstruction = listOf(-1) + split.subList(1, 4).map { it.toInt() }
        Pair(op, opInstruction)
    }
    var registers = mutableListOf(aOverride, 0, 0, 0, 0, 0)

    val seen = mutableSetOf<List<Int>>()
    var lastSeen: Int? = null
    while (registers[instrIdx] in program.indices) {
        if (registers[instrIdx] == 28) { // Right before the check of F to A
            val F = registers.last()
            if (registers in seen) {
                println("Part B: $lastSeen")
                return
            }
            if (registers !in seen) {
                if (lastSeen == null) {
                    println("Part A: $F")
                }
                seen.add(registers)
                lastSeen = F
            }
        }
        registers = applyOperatorJumps(registers, program[registers[instrIdx]])
        registers[instrIdx] = registers[instrIdx] + 1
    }
}
