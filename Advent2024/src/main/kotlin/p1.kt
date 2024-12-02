import java.io.File
import kotlin.math.abs

fun main() {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    File("inputs/input1.txt").forEachLine {
        line ->
        val pair = line.split(" ").filter {it.isNotBlank()}.map {num -> num.toInt()}
        left.add(pair[0])
        right.add(pair[1])
    }

    left.sort()
    right.sort()
    var counterA = 0
    var counterB = 0
    left.forEachIndexed{
        idx, id ->
        counterA += abs(id - right[idx])
        counterB += id * right.count { it == id }
    }

    println("Counter A: $counterA")
    println("Counter B: $counterB")
}