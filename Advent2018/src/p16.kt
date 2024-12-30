import java.io.File

fun main() {
    val input = File("inputs/input15.txt").readLines()
    val samples = input.subList(0, 3111).chunked(4).map { sample ->
        sample.map { line -> Utils.extractIntListFromString(line) }
    }
    val program = input.subList(3114, input.size).map { line -> Utils.extractIntListFromString(line) }
    val allOps = mutableSetOf(
        "addr",
        "addi",
        "mulr",
        "muli",
        "banr",
        "bani",
        "borr",
        "bori",
        "setr",
        "seti",
        "gtir",
        "gtri",
        "gtrr",
        "eqir",
        "eqri",
        "eqrr",
    )

    val counterA = samples.count { sample ->
        allOps.count { op -> applyOperator(sample[0], sample[1], op) == sample[2] } >= 3
    }
    println("Part A: $counterA")

    val opToPossible = mutableMapOf<Int, MutableSet<String>>()
    for (opcode in 0..15) {
        opToPossible[opcode] = allOps.toMutableSet()
    }

    samples.forEach { sample ->
        opToPossible[sample[1][0]] = (opToPossible[sample[1][0]]!! intersect allOps.filter { op ->
            applyOperator(
                sample[0],
                sample[1],
                op
            ) == sample[2]
        }).toMutableSet()
    }

    val finalized = mutableMapOf<Int, String>()
    while (opToPossible.isNotEmpty()) {
        val fixedOps = opToPossible.filterValues { strings -> strings.size == 1 }
        fixedOps.forEach { fixed ->
            finalized[fixed.key] = fixed.value.first()
            opToPossible.remove(fixed.key)
            opToPossible.forEach { entry -> entry.value.remove(fixed.value.first()) }
        }
    }

    var registers = listOf(0, 0, 0, 0)
    program.forEach { instruction ->
        registers = applyOperator(registers, instruction, opcode = null, finalized)
    }
    println("Part B: ${registers[0]}")
}

fun applyOperator(
    initialRegisters: List<Int>,
    instruction: List<Int>,
    opcode: String?,
    opcodeMap: MutableMap<Int, String>? = null
): List<Int> {
    val registers = initialRegisters.toMutableList()
    val opcodeAssumption = opcode ?: opcodeMap!![instruction[0]]!!
    when (opcodeAssumption) {
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