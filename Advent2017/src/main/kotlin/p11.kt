import java.io.File
import java.lang.Integer.max
import kotlin.math.abs
import kotlin.math.min

fun main() {
    File("inputs/input11.txt").forEachLine { line ->
        var rightCounter = 0.0
        var upCounter = 0.0
        var maxDistSoFar = 0
        line.split(",").forEach { move ->
            when (move) {
                "n" -> {
                    upCounter += 1.0
                }

                "s" -> {
                    upCounter -= 1.0
                }

                "ne" -> {
                    upCounter += 0.5
                    rightCounter += 1.0
                }

                "nw" -> {
                    upCounter += 0.5
                    rightCounter -= 1.0
                }

                "se" -> {
                    upCounter -= 0.5
                    rightCounter += 1.0
                }

                "sw" -> {
                    upCounter -= 0.5
                    rightCounter -= 1.0
                }
            }
            maxDistSoFar = max(maxDistSoFar, dist(upCounter, rightCounter))
        }
        println("${upCounter}, ${rightCounter}, ${dist(upCounter, rightCounter)}, ${maxDistSoFar}")
    }
}

fun dist(upCounter: Double, rightCounter: Double): Int {
    var counter = 0
    counter += abs(rightCounter.toInt())
    // Calculate horizontal movement, offsetting vertical by the optimal .5 each
    counter += (abs(upCounter) - min((abs(rightCounter) / 2), abs(upCounter))).toInt()
    return counter
}