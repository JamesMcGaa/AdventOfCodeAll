import java.util.*

fun main() {
    var input = "vzbxkghb"
    val chArr = input.toCharArray().toMutableList()
    increment(chArr, input.lastIndex)
    input = chArr.joinToString("")
    println(nextStr(input))
    println(nextStr(nextStr(input)))
}

fun nextStr(input: String): String {
    val chArr = input.toCharArray().toMutableList()
    increment(chArr, input.lastIndex)
    var input = chArr.joinToString("")
    while(!isLegal(input)) {
        val chArr = input.toCharArray().toMutableList()
        increment(chArr, input.lastIndex)
        input = chArr.joinToString("")
    }
    return input
}

fun increment(input: MutableList<Char>, idx: Int) {
    if (input[idx] == 'z') {
        input[idx] = 'a'
        increment(input, idx - 1)
    }
    else {
        input[idx] = (input[idx].toInt() + 1).toChar()
        input.toString()
    }
}

fun isLegal(input: String): Boolean {
    return tripleStraightCheck(input) && bannedLettersCheck(input) && doubleDoublesCheck(input)
}

fun tripleStraightCheck(input: String): Boolean {
    val asciiCodes = input.map { it.toInt() }
    val diffs = mutableListOf<Int>()
    for (i in 0..asciiCodes.size - 2) {
        diffs.add(asciiCodes[i+1].toInt() - asciiCodes[i].toInt())
    }

    return Collections.indexOfSubList(diffs, listOf(1,1)) > -1
}

fun bannedLettersCheck(input: String): Boolean {
    return !input.contains('i') && !input.contains('o') && !input.contains('l')
}

fun doubleDoublesCheck(input: String): Boolean {
    var counter = 0
    for (ch in 'a' .. 'z') {
        if (input.contains(ch.toString() + ch.toString())) {
            counter += 1
        }
    }
    return counter >= 2
}

