import java.io.File

fun main() {
    Register(0L)
    Register(1L)
    collatz(9663L)
    collatz(77671)
}


/**
 * By inspection, the input builds up deterministically and simply a starting value and runs collatz on it, with
 *  B being a step counter
 */
fun collatz(start: Long) {
    var A = start
    var B = 0L

    while (A != 1L) {
        B += 1L
        if (A % 2L == 0L) {
            A = A / 2L
        } else {
            A = 3*A + 1
        }

    }
    println(B)
}


data class Register(
    val startA: Long,
    var pointer: Int = 0,
    val registers: MutableMap<String,  Long> = mutableMapOf("a" to startA, "b" to 0),
) {
    var instructions: List<List<String>>

    init {
        instructions = File("input/input23.txt").readLines().map {
            it.split(" ").map {it.trim()}
        }
        while (pointer in instructions.indices && registers["b"]!! < 500) {
            if (pointer == 41 && registers["b"]!! == 0L) {
                println("${pointer}, ${registers["a"]}, ${registers["b"]}")
            }
            val op = instructions[pointer]
            if (setOf("hlf", "tpl", "inc").contains(op[0])) {
                when (op[0]) {
                    "hlf" ->  registers[op[1]] = registers[op[1]]!! / 2
                    "tpl" ->  registers[op[1]] = registers[op[1]]!! *  3
                    "inc" ->  registers[op[1]] = registers[op[1]]!! + 1
                }
                pointer += 1
            }
            else {
                when (op[0]) {
                    "jmp" ->  {
                        val sign = if (op[1][0] == '-') -1 else 1
                        pointer += sign * op[1].substring(1).toInt()
                    }
                    "jie" ->  {
                        if (registers[op[1].removeSuffix(",")]!! % 2 == 0L) {
                            val sign = if (op[2][0] == '-') -1 else 1
                            pointer += sign * op[2].substring(1).toInt()
                        } else {
                            pointer += 1
                        }
                    }
                    "jio" ->  {
                        if (registers[op[1].removeSuffix(",")]!! == 1L) {
                            val sign = if (op[2][0] == '-') -1 else 1
                            pointer += sign * op[2].substring(1).toInt()
                        } else {
                            pointer += 1
                        }
                    }
                }
            }
        }
        println(registers["b"])
    }
}