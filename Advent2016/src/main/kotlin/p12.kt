import java.io.File

fun main() {
    val regs = mutableMapOf<String, Long>(
        "a" to 0L,
        "b" to 0L,
        "c" to 1L,
        "d" to 0L, // Part B
    )
    val input = File("inputs/input12.txt").readLines()
    var pointer = 0
    var done = false
    while (pointer in input.indices) {
        val line = input[pointer]
        val split = line.split(" ")
        if (pointer == 18 && !done) {
            println(regs)
            done = true
        }
        when (split[0]) {
            "cpy" -> {
                val copied = split[1]
                regs[split[2]] = if (copied.first().isDigit()) copied.toLong() else regs[copied]!!
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
                if ((split[1][0].isDigit() && split[1].toInt() == 0) || regs[split[1]] == 0L) {
                    pointer += 1
                } else {
                    pointer += split[2].toInt()
                }
            }
        }
    }

    println(regs["a"])
}