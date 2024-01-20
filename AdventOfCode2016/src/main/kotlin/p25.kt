import java.io.File

fun main() {
    var start = 0L
    while (true) {
        println(start)
        if (runP25(start)) {
            println(start)
            return
        }
        start += 1L
    }
}
fun runP25(initialA: Long): Boolean {
    val regs = mutableMapOf(
        "a" to initialA,
        "b" to 0L,
        "c" to 0L,
        "d" to 0L,
    )
    val buffer = mutableListOf<Long>()
    val seen = mutableSetOf<Int>()
    val input = File("inputs/input25.txt").readLines().toMutableList()
    var pointer = 0
    while (pointer in input.indices) {
        val line = input[pointer]
        val split = line.split(" ")
        when (split[0]) {
            "cpy" -> {
                val copied = split[1]
                try {
                    regs[split[2]] = if (copied.first().isDigit() || copied.first() == '-') copied.toLong() else regs[copied]!!
                } catch (e: Exception) {}
                pointer += 1
            }

            "inc" -> {
                regs[split[1]] = regs[split[1]]!! + 1
                pointer += 1
            }

            "dec" -> {
                regs[split[1]] = regs[split[1]]!! - 1
                pointer += 1
            }

            "jnz" -> {
                if ((split[1][0].isDigit() && split[1].toInt() == 0) || regs[split[1]] == 0L) { // Literal 0 or register 0
                    pointer += 1
                } else {
                    pointer += if (split[2][0].isDigit() || split[2][0] == '-') split[2].toInt() else regs[split[2]]!!.toInt()
                }
            }

            "tgl" -> {
                val newIdx = pointer + regs[split[1]]!!.toInt()
                if (newIdx in input.indices) {
                    when (input[newIdx].split(" ")[0]) {
                        "inc" -> {
                            input[newIdx] = input[newIdx].replace("inc", "dec")
                        }
                        "dec" -> {
                            input[newIdx] = input[newIdx].replace("dec", "inc")
                        }
                        "tgl" -> {
                            input[newIdx] = input[newIdx].replace("tgl", "inc")
                        }
                        "cpy" -> {
                            input[newIdx] = input[newIdx].replace("cpy", "jnz")
                        }
                        "jnz" -> {
                            input[newIdx] = input[newIdx].replace("jnz", "cpy")
                        }
                    }
                }
                pointer += 1
            }

            "out" -> {
                buffer.add(regs[split[1]]!!)
                if (buffer.size > 1 && buffer[buffer.lastIndex] == buffer[buffer.lastIndex - 1]) return false
                if (seen.contains(regs.hashCode())) {
                    return true
                }
                seen.add(regs.hashCode())
               pointer += 1
            }

        }
    }
    println("DONE")
    return false
}