fun main() {
    val cardPublicKey = 18499292L
    val doorPublicKey = 8790390L
    var result = 1L
    var times = 0L
    while (true) {
        times++
        result = result * 7
        result = result % 20201227
        if (result == cardPublicKey) {
            println(transform(doorPublicKey, times))
            break
        }
        if (result == doorPublicKey) {
            println(transform(cardPublicKey, times))
            break
        }
    }
}

fun transform(subjectNumber: Long, loopSize: Long): Long {
    var result = 1L
    repeat(loopSize.toInt()) {
        result = result * subjectNumber
        result = result % 20201227
    }
    return result
}