import java.io.File

/**
 * I like my solution however besides some clever but unreadable solutions I found this one to stand out online
 *
 * - Initialise all characters weights to 1
 * - Scan input one character at a time and
 *   - if it's a normal character, count its weight towards the total length
 *   - if it's the beginning of a marker, read the marker and multiply character
 *     weights forward in the input, according to the values of the marker.
 * - Print out the value of length
 *
 */
fun main() {
    File("inputs/input9.txt").forEachLine { line ->
        var ret = ""
        var pointer = 0
        while (pointer in line.indices) {
            val ch = line[pointer]
            if (ch == '(') {
                val endPointer = line.indexesOf("\\)").filter {it > pointer}.min()
                val inner = line.substring(pointer + 1, endPointer)
                val innerParsed = inner.split("x").map {it.toInt()}
                val numChars = innerParsed[0]
                val times = innerParsed[1]
                repeat(times) {
                    ret += line.substring(endPointer + 1, endPointer + 1 + numChars)
                }
                pointer = endPointer + 1 + numChars
            } else {
                ret += ch
                pointer += 1
            }
        }
        println(ret.length)
    }

    File("inputs/input9.txt").forEachLine { it ->
        val result = getSubsequence(it)
        println(result.sum)
    }

}

fun getSubsequence(line: String, times: Int = 1): Sequence { // End pointer
    var pointer = 0
    var subseq = Sequence(times, mutableListOf(), null)
    while (pointer <= line.lastIndex) {
        val ch = line[pointer]
        if (ch == '(') {
            val endPointer = line.indexesOf("\\)").filter { it > pointer }.min()
            val inner = line.substring(pointer + 1, endPointer)
            val innerParsed = inner.split("x").map { it.toInt() }
            val numChars = innerParsed[0]
            val nestedTimes = innerParsed[1]
            val repeating = line.substring(endPointer + 1, endPointer + 1 + numChars)
            pointer = endPointer + 1 + numChars
            subseq.subsequences!!.add(getSubsequence(repeating, nestedTimes))
        } else {
            subseq.subsequences!!.add(Sequence(1, null, ch))
            pointer += 1
        }
    }
    return subseq
}

data class Sequence(
    var times: Int,
    var subsequences: MutableList<Sequence>?,
    var literal: Char?
) {
    val sum: Long
        get() = if (literal != null) 1L else times * subsequences!!.sumOf { it.sum }


    override fun toString(): String {
        literal?.let {
            return literal.toString()
        }
        return "{" + times.toString() + ", " + subsequences.toString() + "}"
    }
}

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    return this?.let {
        val regex = Regex(substr)
        regex.findAll(this).map { it.range.start }.toList()
    } ?: emptyList()
}
