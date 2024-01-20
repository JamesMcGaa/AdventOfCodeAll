import java.io.File

fun main() {
    val input = File("inputs/input1.txt").readLines()[0]
    conseqChars(input, false)
    conseqChars(input, true)
}
fun conseqChars(input: String, isPartB: Boolean) {
    val offset = if (isPartB) input.length / 2 else 1
    var counterA = 0
    for (i in input.indices) {
        if (input[i] == input[(i + offset) % input.length]) {
            counterA += input[i].toString().toInt()
        }
    }
    println(counterA)
}