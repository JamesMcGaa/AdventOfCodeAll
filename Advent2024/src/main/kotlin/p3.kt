import java.io.File

fun main() {
    var counterA = 0
    var counterB = 0
    File("inputs/input3.txt").readText().apply {
        counterA += findSum(this)
        counterB += findSum(this, partBEnabled = true)
    }
    println("Counter A: $counterA")
    println("Counter B: $counterB")
}

fun findSum(line: String, partBEnabled: Boolean = false): Int {
    var counter = 0
    var ops = mutableListOf<Operation>()
    val mulRegex = Regex("mul\\((\\d+),(\\d+)\\)")
    val doRegex = Regex("do\\(\\)")
    val dontRegex = Regex("don't\\(\\)")

    doRegex.findAll(line).forEach { matchResult ->
        ops.add(Operation(matchResult.range.first, null, OpType.DO))
    }
    dontRegex.findAll(line).forEach { matchResult ->
        ops.add(Operation(matchResult.range.first, null, OpType.DONT))
    }
    mulRegex.findAll(line).forEach { matchResult ->
        val subStr = line.substring(matchResult.range)
        val startEnd = subStr.split(",").map { str -> str.filter { ch -> ch.isDigit() }.toInt() }
        ops.add(Operation(matchResult.range.first, startEnd, OpType.MUL))
    }

    ops.sortBy { op -> op.start }
    if (!partBEnabled) {
        ops = ops.filter { op -> op.opType == OpType.MUL }.toMutableList()
    }

    var on = 1
    for (op in ops) {
        when (op.opType) {
            OpType.DO -> {on = 1}
            OpType.DONT -> {on = 0}
            OpType.MUL -> {
                counter += on * op.mulContent!![0] * op.mulContent[1]
            }
        }
    }
    return counter
}

data class Operation(
    val start: Int,
    val mulContent: List<Int>?,
    val opType: OpType,
) {
    init {
        if (opType == OpType.MUL) {
            assert(mulContent!![0] in 1..999)
            assert(mulContent[1] in 1..999)
        }
    }
}

enum class OpType {
    DO, DONT, MUL
}