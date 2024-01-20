import java.io.File

val colorToMax = hashMapOf("red" to 12, "green" to 13, "blue" to 14)
fun main() {
    var counter = 0
    var counterB = 0
    var ID = 0
    File("inputs/input2.txt").forEachLine {
        line ->
        var valid = true
        val mins = hashMapOf<String, Int>()
        ID += 1
        val content = line.split(":")[1]
        val rounds = content.split(";")
        rounds.forEach {
            round ->
            val colors = round.split(",")
            colors.map {it.trim()}.forEach {
                colorPair ->
                val num = colorPair.split(" ")[0].toInt()
                val color = colorPair.split(" ")[1]
                if (num > colorToMax[color]!!) {
                    valid = false
                }
                if (!mins.contains(color) || mins[color]!! < num) {
                    mins[color] = num
                }
            }
        }
        if (valid) {
            counter += ID
        }
        counterB += mins.values.reduce(Int::times)
    }

    println(counter)
    println(counterB)
}
