import java.io.File
import java.lang.Exception
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main(args: Array<String>) {
    var registerNum = 1
    var signalStrengthCounter = 0
    var cycleCount = 0
    val lines = File("inputs/input10.txt").readLines().toMutableList()
    val opQ = mutableListOf<Op>()
    var outputString = ""

    while (cycleCount < 240) {
        cycleCount += 1
        if (cycleCount in listOf(20, 60, 100, 140, 180, 220))  {
            signalStrengthCounter += cycleCount * registerNum
        }

        if (((cycleCount % 40) - 1 - registerNum).absoluteValue <= 1) {
            outputString += "#"
        }
        else {
            outputString += "."
        }

        var op: Op
        if (opQ.isEmpty()) {
            op = Op(lines.removeFirst())
            if(!op.isNoop) {
                opQ.add(op)
            }
        }
        else {
            op = opQ.removeFirst()
            registerNum += op.numericValue
        }
    }

    println(signalStrengthCounter)
    for (i in 0 until 6) {
        println(outputString.substring(40*i,40*i + 40))
    }

}

class Op(line: String) {
     val numericValue: Int
     val isNoop: Boolean

    init {
        val split = line.split(" ")
        if (split[0] == "noop") {
            isNoop = true
            numericValue = -100000000
        }
        else {
            isNoop = false
            numericValue = Integer.parseInt(split[1])
        }
    }
}
