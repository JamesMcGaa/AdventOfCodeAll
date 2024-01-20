import java.io.File
import kotlin.math.min

fun main() {
    val reindeers = mutableListOf<Reindeer>()
    File("input/input14.txt").forEachLine {
        val speed = it.split(" ")[3].toInt()
        val runTime = it.split(" ")[6].toInt()
        val restTime = it.split(" ")[13].toInt()
        reindeers.add(Reindeer(speed, runTime, restTime))
    }
    println(reindeers.map{it.getDist(2503)}.max())

    val points = hashMapOf<Reindeer, Int>()
    for (i in 1..2503) {
        val max = reindeers.map{it.getDist(i)}.max()
        reindeers.forEach {
            if (it.getDist(i) == max) {
                points[it] = points.getOrDefault(it, 0) + 1
            }
        }
    }
    println(points.values.max())
}

data class Reindeer(
    val speed: Int,
    val runTime: Int,
    val restTime: Int
) {
    val cycleTime = runTime + restTime

    fun getDist(sec: Int): Int {
        val completeCycles = sec / cycleTime
        val leftoverTime = sec % cycleTime

        val partialDist = min(runTime, leftoverTime).toInt() * speed
        return partialDist + completeCycles * speed * runTime
    }
}

