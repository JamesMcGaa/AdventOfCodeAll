import java.io.File

const val SIZE = 256

fun main() {
    partA()
    println(partB())
}

fun partB(): String {
    return knothashB(File("inputs/input10.txt").readLines()[0])
}

fun knothashB(input: String): String {
    var chain = mutableListOf<Int>()
    for (i in 0..SIZE - 1) {
        chain.add(i)
    }
    var current = 0
    var skipSize = 0
    // toInt on char gets ascii code instead of parsing it like a String
    val lengths = input.toCharArray().map { it.toInt() }.toMutableList()
    lengths.addAll(listOf(17, 31, 73, 47, 23))

    for (i in 1..64) {
        lengths.forEach { length ->
            chain = reverseRange(chain, length, current)
            current = (current + length + skipSize) % SIZE
            skipSize++
        }
    }

    val sparse = mutableListOf<Int>()
    for (i in 0..15) {
        val slice = chain.subList(i * 16, i * 16 + 16)
        sparse.add(
            slice.reduce { acc, num -> acc xor num }
        )
    }
    val dense = sparse.map {
        val hex = Integer.toHexString(it)
        if (hex.length == 1) { // Subtle bug - need to add padding manually
            "0" + hex
        } else {
            hex
        }
    }.joinToString("")
    return dense
}

fun partA() {
    var chain = mutableListOf<Int>()
    for (i in 0..SIZE - 1) {
        chain.add(i)
    }
    var current = 0
    var skipSize = 0
    val lengths = File("inputs/input10.txt").readLines()[0].split(",").map { it.toInt() }

    lengths.forEach { length ->
        chain = reverseRange(chain, length, current)
        current = (current + length + skipSize) % SIZE
        skipSize++
    }

    println(chain[0] * chain[1])
}

fun <T> reverseRange(chain: MutableList<T>, length: Int, current: Int): MutableList<T> {
    val out = chain.toMutableList()
    for (i in 0..length - 1) {
        out[(current + i) % SIZE] = chain[(current + length - 1 - i) % SIZE]
    }
    return out
}