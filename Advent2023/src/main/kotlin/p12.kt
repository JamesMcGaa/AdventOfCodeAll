import java.io.File

data class Line(
    var line: String,
    var contiguous: List<Int>,
)

fun main() {
    val solver = Solver()
    solver.solveANew()
    solver.solveAOld()
    solver.solveB()
}

class Solver() {
    val MEMO = hashMapOf<Triple<Int, Int, Int>, Long>()
    val LINES = mutableListOf<Line>()

    init {
        File("inputs/input12.txt").forEachLine {
            LINES.add(
                Line(
                    it.split(" ")[0],
                    it.split(" ")[1].split(",").map { it.toInt() },
                )
            )
        }
    }

    // Enumerate all of them by mapping the question marks to the binary representation
    fun solveAOld() {
        var counterA = 0
        for (line in LINES) {
            val numQuestions = line.line.count { it == '?' }
            for (i in 0..(1 shl numQuestions).toInt() - 1) { // For all possible ? arrangements
                var currQuestionIdx = 0
                var formattedString = ""
                val binString = i.toString(radix = 2).padStart(numQuestions, '0')
                for (ch in line.line) {
                    if (ch == '?') {
                        formattedString += if (binString[currQuestionIdx] == '1') '#' else '.'
                        currQuestionIdx += 1
                    } else {
                        formattedString += ch
                    }
                }
                val contigs = formattedString.split(".").filter { it.isNotBlank() }.map { it.length }
                if (contigs.equals(line.contiguous)) {
                    counterA += 1
                }
            }
        }
        println(counterA)
    }

    fun multiplyInput() {
        for (line in LINES) {
            val oldLine = line.line
            for (i in 0..3) {
                line.line += '?'
                line.line += oldLine
            }
            val oldContiguous = line.contiguous.toMutableList()
            line.contiguous = mutableListOf<Int>().apply {
                repeat(5) {
                    this.addAll(oldContiguous)
                }
            }
        }
    }

    fun solveANew() {
        var counterA = 0L
        for (line in LINES) {
            counterA += solveLine(line)
        }
        println(counterA)
    }

    fun solveB() {
        multiplyInput()
        var counterB = 0L
        for (line in LINES) {
            counterB += solveLine(line)
        }
        println(counterB)
    }

    fun solveLine(line: Line): Long {
        MEMO.clear()
        return memoizedSolve(Triple(0, 0, 0), line)
    }

    fun memoizedSolve(current: Triple<Int, Int, Int>, line: Line): Long {
        if (MEMO.contains(current)) {
            return MEMO[current]!!
        } else {
            val solved = rawSolve(current, line)
            MEMO[current] = solved
            return solved
        }
    }

    fun rawSolve(current: Triple<Int, Int, Int>, line: Line): Long {
        val charIdx = current.first
        val groupIdx = current.second
        val progressInGroup = current.third

        if (charIdx == line.line.length && (
                    (groupIdx == line.contiguous.size && progressInGroup == 0) ||
                    (groupIdx == line.contiguous.size - 1 && progressInGroup == line.contiguous.last())
            )

        ) {
            return 1
        } else if (charIdx == line.line.length || groupIdx > line.contiguous.size) {
            return 0
        }

        val groupLen = line.contiguous.getOrNull(groupIdx) ?: 730

        when (line.line[charIdx]) {
            '.' -> {
                if (progressInGroup == 0) { // Wait for a new group to be started
                    return memoizedSolve(Triple(charIdx + 1, groupIdx, 0), line)
                } else if (progressInGroup < groupLen) { // Didnt finish the group
                    return 0
                } else { // Cut the group and advance, since progress == groupLen
                    return memoizedSolve(Triple(charIdx + 1, groupIdx + 1, 0), line)
                }
            }

            '?' -> {
                if (progressInGroup == groupLen) { // Cut the group and advance .
                    return memoizedSolve(Triple(charIdx + 1, groupIdx + 1, 0), line)
                }
                else if (progressInGroup != 0 && progressInGroup < groupLen) { // Continue the group #
                    return memoizedSolve(Triple(charIdx + 1, groupIdx, progressInGroup + 1), line)
                }
                else {
                    return memoizedSolve(Triple(charIdx + 1, groupIdx, progressInGroup + 1), line) + // #
                            memoizedSolve(Triple(charIdx + 1, groupIdx, progressInGroup), line) // .
                }
            }

            '#' -> {
                if (progressInGroup < groupLen) { // start, continue the group
                    return memoizedSolve(Triple(charIdx + 1, groupIdx, progressInGroup + 1), line)
                } else { // Went past group, since progress == groupLen
                    return 0
                }
            }

            else -> throw Exception("WTF")
        }
    }
}