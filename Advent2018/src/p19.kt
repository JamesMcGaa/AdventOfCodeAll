import java.io.File

fun main() {
    run19()
    run19(isPartB = true)
}

fun run19(isPartB: Boolean = false) {
    val input = File("inputs/input19.txt").readLines()
    var instrIdx = input.first().filter { it.isDigit() }.toInt()
    val program = input.subList(1, input.size).map { line ->
        val split = line.split(" ")
        val op = split[0]
        val opInstruction = listOf(-1) + split.subList(1, 4).map { it.toInt() }
        Pair(op, opInstruction)
    }
    var registers = mutableListOf(0, 0, 0, 0, 0, 0)

    if (isPartB) {
        registers[0] = 1
    }

    while (registers[instrIdx] in program.indices) {
        /**
         * See inputs/p19scratch.txt - this program essentially jumps to the very end
         * and calculates the sum of divisors of B after initializing it in a double
         * nested for loop
         */
        if (registers[instrIdx] == 1) {
            println(registers)
        }
        registers = applyOperatorJumps(registers, program[registers[instrIdx]])
        registers[instrIdx] = registers[instrIdx] + 1
    }

    println("Part ${if (isPartB) 'B' else 'A'}: ${registers[0]}")
}

fun applyOperatorJumps(
    initialRegisters: List<Int>,
    opInstruction: Pair<String, List<Int>>
): MutableList<Int> {
    val registers = initialRegisters.toMutableList()
    val opcode = opInstruction.first
    val instruction = opInstruction.second
    when (opcode) {
        "addr" -> {
            registers[instruction[3]] = registers[instruction[1]] + registers[instruction[2]]
        }

        "addi" -> {
            registers[instruction[3]] = registers[instruction[1]] + instruction[2]
        }

        "mulr" -> {
            registers[instruction[3]] = registers[instruction[1]] * registers[instruction[2]]
        }

        "muli" -> {
            registers[instruction[3]] = registers[instruction[1]] * instruction[2]
        }

        "banr" -> {
            registers[instruction[3]] = registers[instruction[1]] and registers[instruction[2]]
        }

        "bani" -> {
            registers[instruction[3]] = registers[instruction[1]] and instruction[2]
        }

        "borr" -> {
            registers[instruction[3]] = registers[instruction[1]] or registers[instruction[2]]
        }

        "bori" -> {
            registers[instruction[3]] = registers[instruction[1]] or instruction[2]
        }

        "setr" -> {
            registers[instruction[3]] = registers[instruction[1]]
        }

        "seti" -> {
            registers[instruction[3]] = instruction[1]
        }

        "gtir" -> {
            val passes = instruction[1] > registers[instruction[2]]
            registers[instruction[3]] = if (passes) 1 else 0
        }

        "gtri" -> {
            val passes = registers[instruction[1]] > instruction[2]
            registers[instruction[3]] = if (passes) 1 else 0
        }

        "gtrr" -> {
            val passes = registers[instruction[1]] > registers[instruction[2]]
            registers[instruction[3]] = if (passes) 1 else 0
        }

        "eqir" -> {
            val passes = instruction[1] == registers[instruction[2]]
            registers[instruction[3]] = if (passes) 1 else 0
        }

        "eqri" -> {
            val passes = registers[instruction[1]] == instruction[2]
            registers[instruction[3]] = if (passes) 1 else 0
        }

        "eqrr" -> {
            val passes = registers[instruction[1]] == registers[instruction[2]]
            registers[instruction[3]] = if (passes) 1 else 0
        }

        else -> throw Exception("Illegal opcode")
    }
    return registers
}
