import java.io.File


/**
 * - One speedrunner used Lagrange interpolation
 *
 * - One used FindSequenceFunction in Wolfram LOL
 */

fun main() {
    var counterA = 0
    var counterB = 0
    File("inputs/input9.txt").forEachLine { line ->
        counterA += forecast(line.split(" ").map { it.toInt() }.toMutableList())
        counterB += forecast(line.split(" ").reversed().map { it.toInt() }.toMutableList()) // Clever reverse input trick
    }
    println(counterA)
    println(counterB)
}

fun forecast(begin: MutableList<Int>): Int {
    val allSequences = mutableListOf<MutableList<Int>>(begin)
    while (!allSequences.last().isFinished()) {
        allSequences.add(diff(allSequences.last()))
    }
    for (i in (0..allSequences.lastIndex).reversed()) {
        if (i == allSequences.lastIndex) {
            allSequences[i].add(0)
        } else {
            allSequences[i].add(allSequences[i].last() + allSequences[i + 1].last())
        }
    }

    return allSequences[0].last()
}

fun diff(list: MutableList<Int>): MutableList<Int> {
    val newList = mutableListOf<Int>()
    for (i in 0..list.lastIndex - 1) {
        newList.add(list[i + 1] - list[i])
    }
    return newList
}

fun List<Int>.isFinished(): Boolean {
    return this.toSet().size == 1 && this.toSet().contains(0)
}