val instr = mutableListOf(2, 4, 1, 1, 7, 5, 4, 7, 1, 4, 0, 3, 5, 5, 3, 0)
val observedOutput = instr // mutableListOf(1, 3, 7, 4, 6, 4, 2, 3, 5)

class Candidate(
    var bits: MutableList<Int> = mutableListOf<Int>(),
    var rightCounter: Int = 63,
) {

    init {
        if (bits.isEmpty()) {
            repeat(64) {
                bits.add(-1)
            }
        }
    }

    fun setAC(a: Int, c: Int, cShift: Int): Candidate? {
        val newCandidate = Candidate(bits.toMutableList(), rightCounter)
        a.toString(2).padStart(3, '0').forEachIndexed { index, ch ->
            val digit = ch.toString().toInt()
            // Make sure we don't get a conflict
            if (newCandidate.bits[rightCounter - (2 - index)] in listOf(-1, digit)) {
                newCandidate.bits[rightCounter - (2 - index)] = digit
            } else {
                return null
            }
        }

        c.toString(2).padStart(3, '0').forEachIndexed { index, ch ->
            val digit = ch.toString().toInt()
            // Make sure we don't get a conflict, including with the just set 'a' digits
            if (newCandidate.bits[rightCounter - cShift - (2 - index)] in listOf(-1, digit)) {
                newCandidate.bits[rightCounter - cShift - (2 - index)] = digit
            } else {
                return null
            }
        }

        return newCandidate.apply {
            rightCounter -= 3
        }
    }

    fun prettyPrint(): String {
        return bits.joinToString("") { if (it == -1) "." else it.toString() }.trimStart('.')
    }

    fun reduced(): String {
        return bits.joinToString("") { if (it == -1) "0" else it.toString() }.trimStart('0')
    }
}

fun main() {
    println("Part A: ${Chronospatial(30553366).execute(stopAtFirstOutput = false)}") // Part A

    Candidate().prettyPrint()
    var frontier = mutableListOf(Candidate())
    println(frontier.map {it.prettyPrint()})
    observedOutput.forEach { currentInstr ->
        val newFrontier = mutableListOf<Candidate>()
        frontier.forEach { candidate ->
            for (a in 0..7) {
                for (c in 0..7) {
                    var b = a
                    b = b xor 1
                    val bShift = b
                    b = b xor c
                    b = b xor 4
                    if (b == currentInstr) {
                        candidate.setAC(a, c, bShift)?.let {
                            newFrontier.add(it)
                        }
                    }
                }
            }

        }
        frontier = newFrontier
        println(frontier.map {it.prettyPrint()})
    }
    val possibleInputs = frontier.map { it.reduced().toLong(2) }
    println(possibleInputs)
    println("Part B: ${possibleInputs.min()}")
}

data class Chronospatial(
    var a: Long,
    var instrPtr: Int = 0,
    var b: Long = 0L,
    var c: Long = 0L
) {

    fun combo(operand: Int): Int {
        return when (operand) {
            0, 1, 2, 3 -> operand
            4 -> a.toInt()
            5 -> b.toInt()
            6 -> c.toInt()
            else -> throw Exception("Bad opcode")
        }
    }

    fun execute(stopAtFirstOutput: Boolean): String {
        var output = mutableListOf<Int>()

        while (instrPtr in instr.indices) {
            val opcode = instr[instrPtr]
            val operand = instr[instrPtr + 1]
            var jumped = false
            when (opcode) {
                0 -> {
                    a = a shr combo(operand)
                }

                1 -> {
                    b = b xor operand.toLong()
                }

                2 -> {
                    b = (combo(operand) % 8).toLong()
                }

                3 -> {
                    if (a != 0L) {
                        instrPtr = operand
                        jumped = true
                    }
                }

                4 -> {
                    b = b xor c
                }

                5 -> {
                    val new = combo(operand) % 8
                    if (stopAtFirstOutput) {
                        return new.toString()
                    }
                    output.add(new)
                }

                6 -> {
                    b = a shr combo(operand)
                }

                7 -> {
                    c = a shr combo(operand)
                }

                else -> throw Exception("Bad opcode")
            }
            if (!jumped) {
                instrPtr += 2
            }
        }

        return output.joinToString(",")
    }
}