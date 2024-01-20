import java.io.File
import kotlin.math.abs

fun main() {
    val strings = File("input/input5.txt").readLines()
    println(strings.count { abaPatern(it) && twoNonAdjPairs(it) })
}

fun twoNonAdjPairs(input: String): Boolean {
    for (ch_a in 'a'..'z') {
        for (ch_b in 'a'..'z') {
            var potential = ""
            potential += ch_a
            potential += ch_b
            if (abs(input.indexOf(potential) - input.lastIndexOf(potential)) > 1) {
                return true
            }
        }
    }
    return false
}

fun abaPatern(input: String): Boolean {
    for (ch_a in 'a'..'z') {
        for (ch_b in 'a'..'z') {
            var potential = ""
            potential += ch_a
            potential += ch_b
            potential += ch_a
            if (input.contains(potential)) {
                return true
            }
        }
    }
    return false
}