fun main() {
    val countA = (272091..815432).filter { adjEquals(it) && incOrSame(it) }
    println("Part A: ${countA.size}")
    val countB = (272091..815432).filter { adjEqualsNoGroup(it) && incOrSame(it) }
    println("Part B: ${countB.size}")
}

fun adjEquals(num: Int): Boolean {
    val strNum = num.toString()
    for (i in 0..strNum.length - 2) {
        if (strNum[i] == strNum[i + 1]) return true
    }
    return false
}

fun adjEqualsNoGroup(num: Int): Boolean {
    val strNum = num.toString()
    for (i in 0..strNum.length - 2) {
        if (strNum[i] == strNum[i + 1] && strNum[i] != strNum.getOrNull(i - 1) && strNum[i + 1] != strNum.getOrNull(i + 2)) return true
    }
    return false
}

fun incOrSame(num: Int): Boolean {
    val strNum = num.toString()
    for (i in 0..strNum.length - 2) {
        if (strNum[i].toString().toInt() > strNum[i + 1].toString().toInt()) return false
    }
    return true
}