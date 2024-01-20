import java.io.File

fun main() {
    val lines = File("inputs/input15.txt").readLines()
    var equations = mutableListOf<MutableList<Int>>()
    for (i in lines.indices) {
        val line = lines[i]
        val parsed = line.split(" ").map {it.toInt()}.toMutableList()
        parsed[1] += i + 1
        parsed[1] %= parsed[0]
        parsed[1] = parsed[0] - parsed[1]
        parsed[1] %= parsed[0]
        equations.add(parsed)
    }
    var counter = 0
    println(equations)
    outer@while (true) {
        counter += 1
        for (equation in equations) {
            if (counter % equation[0] != equation[1]) continue@outer // Hit disk
        }
        break
    }
    println(counter)
}