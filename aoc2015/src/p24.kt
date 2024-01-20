import kotlin.math.min

val splitMap = mutableMapOf<Int, MutableSet<List<Long>>>()
val allSets = mutableSetOf<List<Long>>()
val numbers = listOf(
    1,
    3,
    5,
    11,
    13,
    17,
    19,
    23,
    29,
    31,
    41,
    43,
    47,
    53,
    59,
    61,
    67,
    71,
    73,
    79,
    83,
    89,
    97,
    101,
    103,
    107,
    109,
    113
).map { it.toLong() } // 1548 sum
val numSet = numbers.toSet()
val thirdSplits = mutableListOf<List<Long>>()

/**
 * This one took a good amount of work - I put some failed attempts in the inputs as a txt file.
 *
 * - Other people just simply found the smallest 5 set and avoided some of the other logic
 *
 * - I wonder if there is a better more efficient way to generalize here.
 */
fun main() {
    for (i in 1..24) {
        splitMap[i] = mutableSetOf()
    }
    findValids(Attempt(0, 24, 0), 387)
    for (i in 1..24) {
        println("${i}, ${splitMap[i]!!.size}")
    }

    val bestBases = splitMap[5]!!.sortedBy { it.reduce { sum, ele -> sum * ele } }

    for (a in bestBases) {
        println("------ Base Attempt -----")

        // Find another 6 set - since theres 19 left, there must be at least 1 of size 6
        val remaining = (numSet - a.toSet()).toList()
        thirdSplits.clear()
        findSecondQuarters(Attempt(0, 6, 0), remaining )

        for (b in thirdSplits) {
            val finalRemaining = (remaining.toSet() - b.toSet()).toList()
            if (findEvenSplit(Attempt(0, 8, 0), finalRemaining)) {
                println(a)
                println(b)
                println(a.reduce { sum, ele -> sum * ele })
                return
            }
        }
    }
}

fun findEvenSplit(attempt: Attempt, remainingNumbers: List<Long>): Boolean {
    if (attempt.sumSoFar == 387) { // Success
        return true
    }
    if (attempt.idx == remainingNumbers.size || attempt.leftSpots == 0 || attempt.sumSoFar > 387) { // Failure
        return false
    }
    return findEvenSplit(
        attempt.copy(
            idx = attempt.idx + 1,
            leftSpots = attempt.leftSpots - 1,
            sumSoFar = (attempt.sumSoFar + remainingNumbers[attempt.idx]).toInt(),
            taken = attempt.taken.toMutableList().apply { add(remainingNumbers[attempt.idx]) }
        ), remainingNumbers
    ) ||
            findEvenSplit(attempt.copy(idx = attempt.idx + 1), remainingNumbers)
}


fun findSecondQuarters(attempt: Attempt, remainingNumbers: List<Long>) {
    if (attempt.sumSoFar == 387) { // Success
        thirdSplits.add(attempt.taken)
        return
    }
    if (attempt.idx == remainingNumbers.size || attempt.leftSpots == 0 || attempt.sumSoFar > 516) { // Failure
        return
    }
    findSecondQuarters(
        attempt.copy(
            idx = attempt.idx + 1,
            leftSpots = attempt.leftSpots - 1,
            sumSoFar = (attempt.sumSoFar + remainingNumbers[attempt.idx]).toInt(),
            taken = attempt.taken.toMutableList().apply { add(remainingNumbers[attempt.idx]) }
        ), remainingNumbers
    )
    findSecondQuarters(attempt.copy(idx = attempt.idx + 1), remainingNumbers)
}


fun findValids(attempt: Attempt, target: Int) {
    if (attempt.sumSoFar == target) { // Success
        splitMap[attempt.taken.size]!!.add(attempt.taken)
        allSets.add(attempt.taken)
        return
    }
    if (attempt.idx == numbers.size || attempt.leftSpots == 0 || attempt.sumSoFar > target) { // Failure
        return
    }
    findValids(
        attempt.copy(
            idx = attempt.idx + 1,
            leftSpots = attempt.leftSpots - 1,
            sumSoFar = (attempt.sumSoFar + numbers[attempt.idx]).toInt(),
            taken = attempt.taken.toMutableList().apply { add(numbers[attempt.idx]) }
        ), target
    )
    findValids(attempt.copy(idx = attempt.idx + 1), target)
}


data class Attempt(
    val idx: Int,
    val leftSpots: Int,
    val sumSoFar: Int,
    val taken: MutableList<Long> = mutableListOf()
)

