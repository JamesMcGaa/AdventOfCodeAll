import java.io.File
import java.lang.Integer.max

fun main() {
    val registers = mutableMapOf<String, Int>()
    var globalMax = 0
    File("inputs/input8.txt").forEachLine { line ->
        val changedReg = line.split(" ")[0]
        val sign = line.split(" ")[1]
        val amount = line.split(" ")[2].toInt()
        val conditinalReg = line.split(" ")[4]
        val conditional = line.split(" ")[5]
        val conditinalAmount = line.split(" ")[6].toInt()
        val conditions = mutableListOf<Boolean>()
        conditions.add(
            conditional == "==" && registers.getOrDefault(
                conditinalReg,
                0
            ) == conditinalAmount
        )
        conditions.add(
            conditional == "!=" && registers.getOrDefault(
                conditinalReg,
                0
            ) != conditinalAmount
        )
        conditions.add(
            conditional == "<=" && registers.getOrDefault(
                conditinalReg,
                0
            ) <= conditinalAmount
        )
        conditions.add(
            conditional == ">=" && registers.getOrDefault(
                conditinalReg,
                0
            ) >= conditinalAmount
        )
        conditions.add(
            conditional == "<" && registers.getOrDefault(
                conditinalReg,
                0
            ) < conditinalAmount
        )
        conditions.add(
            conditional == ">" && registers.getOrDefault(
                conditinalReg,
                0
            ) > conditinalAmount
        )
        if (conditions.filter {it}.any()) {
            if (sign == "inc") {
                registers[changedReg] = registers.getOrDefault(changedReg, 0) + amount
            } else {
                registers[changedReg] = registers.getOrDefault(changedReg, 0) - amount
            }
        }
        globalMax = max(globalMax, registers.values.max())
    }
    println(registers.values.max())
    println(globalMax)
}