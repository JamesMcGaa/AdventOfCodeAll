import java.io.File

fun main() {
    val regs = mutableMapOf<String, Long>(
        "a" to 12L, // Part B
        "b" to 0L,
        "c" to 1L,
        "d" to 0L,
    )
    val input = File("inputs/input23.txt").readLines().toMutableList()
    var pointer = 0
    while (pointer in input.indices) {
        if (pointer == 5) {
            regs["a"] = (regs["c"]!! + regs["a"]!!) * regs["d"]!!
            regs["c"] = 0
            regs["d"] = 0
            pointer += 5
            continue
        }
        if (pointer == 17) {
            println(regs)
            println(input.subList(17, input.lastIndex -1))
        }
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
        }
    }

    println(regs["a"])
}