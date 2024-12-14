import java.io.File

fun main() {
    val input =
        File("inputs/input17.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()
    val computer = IntcodeP17(input.toMutableMap()) // Dont forget to copy
    computer.executeIntcode()
    val partA =
        computer.grid.sumOf { coord: Coord -> if ((coord.neighbors intersect computer.grid).size == 4) coord.alignmentParameter() else 0 }
    println("Part A: $partA")

    var polarity = 0
    val polarityMap = listOf(Coord(-1, 0), Coord(0, 1), Coord(1, 0), Coord(0, -1))
    var pos = Coord(16, 36)

    val fullPath = mutableListOf<String>()
    val seen = mutableSetOf(pos)
    while (seen.size != computer.grid.size) {
        if (pos + polarityMap[(polarity + 1) % 4] in computer.grid) {
            polarity = (polarity + 1) % 4
            fullPath.add("R")
        } else {
            polarity = (polarity + 3) % 4
            fullPath.add("L")
        }
        var dist = 0
        while (pos + polarityMap[polarity] in computer.grid) {
            pos += polarityMap[polarity]
            seen.add(pos)
            dist += 1
        }
        fullPath.add(dist.toString())
    }
    println("fullPath: $fullPath")

    var currentToken = 'A'
    val fullPathMap = mutableMapOf<String, Char>()
    fullPath.toSet().forEach {
        fullPathMap[it] = currentToken
        currentToken += 1
    }
    println("fullPath (symbolic): ${fullPath.map { fullPathMap[it] }.joinToString("")}")

    val A = fullPath.subList(0, 8)
    val B = fullPath.subList(8, 18)
    val C = fullPath.subList(36, 42)
    println("A: $A")
    println("B: $B")
    println("C: $C")

    val mainMovementRoutine = "A,B,A,B,C,C,B,A,B,C\n".map {it.code}
    val movementFunctions = segmentRepToAsciiList(A) + segmentRepToAsciiList(B) + segmentRepToAsciiList(C)
    val continuousInputFeed = listOf('n'.code, 10)
    val fullInput = mainMovementRoutine + movementFunctions + continuousInputFeed
    println("fullInput: $fullInput")

    val computerB = IntcodeP17(input.toMutableMap()) // Dont forget to copy
    computerB.instructions[0L] = 2L
    computerB.inputBuffer.addAll(fullInput.map { it.toLong() })
    computerB.executeIntcode()
}

fun segmentRepToAsciiList(segmentRep: List<String>): List<Int> {
    var strRep = segmentRep.joinToString(",")
    strRep += "\n"
    assert(strRep.length <= 21)
    return strRep.map { ch -> ch.code }
}

fun Coord.alignmentParameter(): Int {
    return this.x * this.y
}

class IntcodeP17(instructions: MutableMap<Long, Long>) : IntcodeP15Base(instructions) {
    private var buffer = ""
    private var rowCtr = 0
    private var colCtr = 0
    val grid = mutableSetOf<Coord>()
    var inputBuffer = mutableListOf<Long>()
    var inputCtr = 0

    override fun getInput(): Long {
        return inputBuffer[inputCtr++]
    }

    override fun outputHook() {
        when (lastOutput) {
            10L -> {
                println(buffer)
                buffer = ""
                rowCtr++
                colCtr = 0
            }

            else -> {
                // Final output is no longer a Char - its the numeric answer
                try {
                    buffer += Char(lastOutput.toInt())
                    if (Char(lastOutput.toInt()) in listOf('^', '#')) {
                        grid.add(Coord(rowCtr, colCtr))
                    }
                    colCtr++
                } catch (_: Exception) {
                    println("Part B: $lastOutput (final output)")
                }
            }
        }
    }
}