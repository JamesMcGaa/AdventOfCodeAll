import java.io.File

fun main() {
    var counter = 0L
    File("src/input25.txt").forEachLine {
        counter += sNAFUstringToDecimalLong(it)
    }
    println(counter)
    println(decimalLongToSnafuString(counter))
}

fun sNAFUstringToDecimalLong(str: String): Long {
    var counter = 0L
    str.forEach { ch ->
        counter *= 5
        when (ch) {
            '2' -> counter += 2L
            '1' -> counter += 1L
            '0' -> counter += 0L
            '-' -> counter -= 1L
            '=' -> counter -= 2L
            else -> throw Exception()
        }
    }
    return counter
}

fun decimalLongToSnafuString(dec: Long): String {
    var counter = 1
    var decEquivalent = 1L
    while (2L * decEquivalent < dec) {
        counter += 1
        decEquivalent *= 5L
    }

    var addStr = ""
    for (i in 1..counter) {
        addStr += "2"
    }

    val adjDec = dec + addStr.toLong(5)
    val strBase5 = adjDec.toString(5)
    var transformed = ""
    strBase5.forEach {
        transformed += when(it) {
            '4' -> '2'
            '3' -> '1'
            '2' -> '0'
            '1' -> '-'
            '0' -> '='
            else -> throw Exception()
        }
    }
    return transformed
}