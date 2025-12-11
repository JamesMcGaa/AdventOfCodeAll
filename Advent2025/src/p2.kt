import java.io.File
import kotlin.math.log10
import kotlin.math.pow

fun main() {
    println("Part A: ${p2("inputs/input2.txt")}")
    println("Part B: ${p2("inputs/input2.txt", isPartB = true)}")
}

fun p2(filename: String, isPartB: Boolean = false): Long {
    val input = File(filename).readLines().first().split(",").map {
        hyphen -> hyphen .split("-").map { str -> str.toLong() }
    }

    var counter = 0L
    input.forEach {
        for (num in it.first()..it.last()) {
            if (isPartB && isInvalidStr(num)) {
                counter += num
            } else if (!isPartB && isInvalid(num)) {
                counter += num
            }
        }
    }

    return counter
}

fun isInvalid(num: Long): Boolean {
    val digits = log10(num.toDouble()).toInt() + 1
    if (digits % 2 == 1) {
        return false
    }
    val splitAmount = 10.0.pow((digits / 2).toDouble()).toInt()
    return num / splitAmount == num % splitAmount
}

fun isInvalidStr(num: Long): Boolean {
    val digits = log10(num.toDouble()).toInt() + 1
    for (i in 2..digits) {
        if (digits % i == 0) {
            val chunks = num.toString().chunked(digits / i)
            if (chunks.toSet().size == 1) {
                return true
            }
        }
    }
    return false
}