import Utils.freqCount
import java.io.File
import kotlin.properties.Delegates

val dateCompator = compareBy<Pair<List<Int>, List<String>>> { it.first[0] }
    .thenBy { it.first[1] }
    .thenBy { it.first[2] }
    .thenBy { it.first[3] }
    .thenBy { it.first[4] }

val daysPerMonth = mutableMapOf(
    1 to 31,
    2 to 28,
    3 to 31,
    4 to 30,
    5 to 31,
    6 to 30,
    7 to 31,
    8 to 31,
    9 to 30,
    10 to 31,
    11 to 30,
    12 to 31,
)

fun main() {
    val inputs = File("inputs/input4.txt").readLines().map {
        val nums = it.split("]")[0].replace("-", " ").replace(":", " ").removePrefix("[").split(" ").map { it.toInt() }
        val command = it.split("]")[1].split(" ").filter { !it.isBlank() }
        Pair(nums, command)
    }
    val chronologicalEvents = inputs.sortedWith(dateCompator)

    var currentGuard = chronologicalEvents.first().second[1]
    var currentTime = chronologicalEvents.first().first
    var sleeping = false
    val guardMap = mutableMapOf<String, MutableList<Pair<Int, Boolean>>>() // #730 -> [(0, sleep), (1,awake)...]

    chronologicalEvents.subList(1, chronologicalEvents.size).forEach {
        while (currentTime != it.first) {
            guardMap.getOrPut(currentGuard) { mutableListOf<Pair<Int, Boolean>>() }
                .apply { add(Pair(currentTime[4], sleeping)) }
            currentTime = advanceSecond(currentTime)
        }
        when (it.second[0]) {
            "Guard" -> {
                currentGuard = it.second[1]
                sleeping = false
            }

            "falls" -> {
                sleeping = true
            }

            "wakes" -> {
                sleeping = false
            }
        }
    }

    var bestSleepAmountA = 0
    lateinit var bestGuardA: String
    var bestMinuteA by Delegates.notNull<Int>()

    var bestSleepAmountB = 0
    lateinit var bestGuardB: String
    var bestMinuteB by Delegates.notNull<Int>()

    guardMap.forEach { guard ->
        // Filter to sleeps, map to minutes, get minute -> count
        val minuteToSleepCountForGuard = guard.value.filter { it.second }.map { it.first }.freqCount()
        val bestMinuteForGuard = minuteToSleepCountForGuard.maxByOrNull { it.value }?.key ?: return@forEach
        val bestMinuteAmountForGuard = minuteToSleepCountForGuard.maxByOrNull { it.value }?.value ?: return@forEach
        val sleepAmountForGuard = guard.value.count { it.second }
        if (sleepAmountForGuard > bestSleepAmountA) {
            bestMinuteA = bestMinuteForGuard
            bestGuardA = guard.key
            bestSleepAmountA = sleepAmountForGuard
        }

        if (bestMinuteAmountForGuard > bestSleepAmountB) {
            bestMinuteB = bestMinuteForGuard
            bestGuardB = guard.key
            bestSleepAmountB = bestMinuteAmountForGuard
        }

    }

    println("Part A: ${bestGuardA.removePrefix("#").toInt() * bestMinuteA}")
    println("Part B: ${bestGuardB.removePrefix("#").toInt() * bestMinuteB}")
}

fun advanceSecond(time: List<Int>): List<Int> {
    val copy = time.toMutableList()
    copy[4] += 1
    if (copy[4] == 60) {
        copy[4] = 0
        copy[3] += 1
    }
    if (copy[3] == 24) {
        copy[3] = 0
        copy[2] += 1
    }
    if (copy[2] > daysPerMonth[copy[1]]!!) {
        copy[2] = 1
        copy[1] += 1
    }
    if (copy[1] == 13) {
        copy[1] = 1
        copy[0] += 1
    }
    return copy
}

// Unused work - the rest of this work was not useful - converting to a timestamp doesnt matter when our primary logic occurs at the minute level
val priorDaysPerMonth = mutableMapOf(
    1 to 0,
    2 to 31,
    3 to 31 + 28,
    4 to 31 + 28 + 31,
    5 to 31 + 28 + 31 + 30,
    6 to 31 + 28 + 31 + 30 + 31,
    7 to 31 + 28 + 31 + 30 + 31 + 30,
    8 to 31 + 28 + 31 + 30 + 31 + 30 + 31,
    9 to 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31,
    10 to 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30,
    11 to 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31,
    12 to 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30,
)

@Suppress("unused")
fun secondsNotation(list: List<Int>): Int {
    return list[4] + 60 * list[3] + 24 * 60 * list[2] + 24 * 60 * priorDaysPerMonth[list[1]]!!
}
