import java.io.File
import kotlin.math.absoluteValue

fun main() {
    p16a()
    p16b()
}

fun p16b() {
    val input = File("inputs/input16.txt").readLines().first()
    val offset = input.substring(0, 7).toInt()
    var signal = mutableListOf<Int>()
    for (i in offset until input.length * 10000) {
        signal.add(input[i % input.length].toString().toInt())
    }

    repeat(100) {
        val newSignal = mutableListOf<Int>()
        var running = 0
        for (idx in signal.indices.reversed()) {
            running = (running + signal[idx]) % 10
            newSignal.add(running)
        }
        signal = newSignal.reversed().toMutableList()
    }

    val partB = signal.joinToString("").substring(0, 8)
    println("Part B: $partB")
}

/**
 * Note each one is just the sum of the ones past us
 * -----------------------------1234567****
 * Since we are past the halfway point, our phases will be 0 until our index, where
 * it will be 1 until the end of the signal, never reaching 0s or -1s as we are past halfway given
 * our input size * 10000 vs our first 7 digits
 *
 * Therefore we actually do not need to do any complicated math to replace * l2[idx] in our old dot
 * In fact we dont need this function entirely
 *
 * Credits to the main Reddit thread
 */
@Suppress("UNUSED_PARAMETER", "unused")
fun dotWithOffset(l1: List<Int>, idx: Int, offset: Int): Int {
    return l1.subList(idx, l1.indices.last + 1).sum().absoluteValue % 10
}

fun p16a() {
    var signal = mutableListOf<Int>()
    File("inputs/input16.txt").readLines().first().forEach { signal.add(it.toString().toInt()) }
    val phases = mutableListOf<MutableList<Int>>()
    val pattern = listOf(0, 1, 0, -1)
    signal.indices.forEach {
        val newPhase = mutableListOf<Int>()
        val repeat = it + 1
        for (i in 1..signal.size) {
            newPhase.add(pattern[(i / repeat) % 4])
        }
        phases.add(newPhase)
    }

    repeat(100) {
        val newSignal = mutableListOf<Int>()
        for (idx in signal.indices) {
            newSignal.add(dot(signal, phases[idx]))
        }
        signal = newSignal
    }

    val partA = signal.joinToString("").substring(0, 8)
    println("Part A: $partA")
}

fun dot(l1: List<Int>, l2: List<Int>): Int {
    assert(l1.size == l2.size)

    return l1.mapIndexed { idx, num -> num * l2[idx] }.sum().absoluteValue % 10
}