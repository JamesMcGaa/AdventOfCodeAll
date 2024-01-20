import java.io.File

fun main(args: Array<String>) {
    var containsCounter = 0
    var overlapCounter = 0
    File("inputs/input4.txt").forEachLine {
        line ->
        val ranges = line.split(",").map {
            it -> Range(it)
        }
        if (ranges[0].contains(ranges[1]) || ranges[1].contains(ranges[0])) {
            containsCounter += 1
        }
        if (ranges[0].overlaps(ranges[1]) || ranges[1].overlaps(ranges[0])) {
            overlapCounter += 1
        }

    }
    println(containsCounter)
    println(overlapCounter)
}

class Range(pair: String) {
    private val a: Int
    private val b: Int
    init {
        a = Integer.parseInt(pair.split('-')[0])
        b = Integer.parseInt(pair.split('-')[1])
    }

    fun contains(other: Range): Boolean {
        return a <= other.a && b >= other.b
    }

    fun overlaps(other: Range): Boolean {
        return other.a in a..b || other.b in a..b
    }

}
