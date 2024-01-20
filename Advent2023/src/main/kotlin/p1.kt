import java.io.File

fun main(args: Array<String>) {
//    var counter = 0L
//    File("inputs/input1.txt").forEachLine {
//        println(it)
//        val digits = it.filter{ch -> ch.isDigit()}
//        val newNum = digits.first().toString() + digits.last().toString()
//        counter += newNum.toLong()
//    }
//    println(counter)

    var counterB = 0L
    val translations = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )
    val numerics = translations.keys + translations.values.toSet()
    File("inputs/input1.txt").forEachLine {
        line ->
        val ranks = numerics.map {
            num -> Pair(num, line.indexOf(num))
        }.filter { it.second >= 0}.sortedBy { it.second }
        var first = ranks.first().first
        if (translations.containsKey(first)) {
            first = translations[first]!!
        }

        val lastRanks = numerics.map {
                num -> Pair(num, line.lastIndexOf(num))
        }.filter { it.second >= 0}.sortedBy { it.second }
        var last = lastRanks.last().first
        if (translations.containsKey(last)) {
            last = translations[last]!!
        }

        val result = first + last
        counterB += result.toLong()
        println(ranks)
        println(result)
    }
    println(counterB)
}