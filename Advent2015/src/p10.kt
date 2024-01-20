import java.io.File
import kotlin.system.exitProcess

fun main() {
    var input = "3113322113"
    repeat(50) {
        println(it)
        input = widen(input)
    }
    println(input.length)

}

fun widen(input: String): String {
    val result = mutableListOf<FrequencyMarker>()
    input.forEach { ch ->
        val prev = result.lastOrNull()
        if (prev != null && prev.ch == ch) {
            prev.freq += 1
        } else {
            result.add(FrequencyMarker(ch, 1))
        }
    }

    var resultString = ""
    result.forEach { freqMarker ->
        resultString += freqMarker.freq.toString()
        resultString += freqMarker.ch
    }
    return resultString
}

data class FrequencyMarker(
    var ch: Char,
    var freq: Int,
)

// Part B: 4666278
// https://mathworld.wolfram.com/CosmologicalTheorem.html