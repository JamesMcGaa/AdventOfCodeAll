@file:Suppress("unused")

import java.io.File
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

open class Coord(
    open val x: Int,
    open val y: Int,
) {
    val neighbors: List<Coord>
        get() = listOf(
            Coord(x = x-1, y = y), // UP
            Coord(x = x+1, y = y), // DOWN
            Coord(x = x, y = y-1), // LEFT
            Coord(x = x, y = y+1), // RIGHT
        )
    open operator fun plus(other: Coord): Coord {
        return Coord(x + other.x, y + other.y)
    }

    open operator fun minus(other: Coord): Coord {
        return Coord(x - other.x, y - other.y)
    }

    override fun equals(other: Any?): Boolean {
        return this.x == (other as? Coord)?.x && this.y == other?.y
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    operator fun div(denom: Int): Coord {
        return Coord(x / denom, y / denom)
    }

    private fun dot(other: Coord): Int {
        return this.x * other.x + this.y * other.y
    }

    private fun magnitude() = sqrt((this.x * this.x + this.y * this.y).toDouble())

    // Credits to https://stackoverflow.com/questions/5188561/signed-angle-between-two-3d-vectors-with-same-origin-within-the-same-plane

    // Probably could use https://stackoverflow.com/questions/14066933/direct-way-of-computing-the-clockwise-angle-between-two-vectors too
    fun clockwiseAngleToVector(other: Coord): Double {
        val dotProduct = this.dot(other)
        val magnitudeProduct = this.magnitude() * other.magnitude()

        if (magnitudeProduct == 0.0) {
            return 0.0 // Handle the case where one or both vectors have zero magnitude
        }

        val cosTheta = dotProduct / magnitudeProduct
        var angle = acos(cosTheta) * 57.2958 // Radians to degrees
        if (this.x * other.y - this.y * other.x > 0) {
            angle = 360 - angle
        }
        return angle
//        return 360 - angle // Technically this is the CCW dist from this to other until we convert
    }
}

class WireCoord(
    override val x: Int,
    override val y: Int,
    var stepsTaken: Int = 0,
) : Coord(x, y) {
    override operator fun plus(other: Coord): WireCoord {
        return WireCoord(x + other.x, y + other.y)
    }

    override operator fun minus(other: Coord): WireCoord {
        return WireCoord(x - other.x, y - other.y)
    }

}

fun main() {
    val input = File("inputs/input3.txt").readLines()
    val wire1 = populateWiresFromString(input[0])
    val wire2 = populateWiresFromString(input[1])

    val intersection = wire1.keys intersect wire2.keys
    println(intersection.minOf { abs(it.x) + abs(it.y) })
    println(intersection.minOf { abs(wire1[it]!!.stepsTaken) + abs(wire2[it]!!.stepsTaken) })
}

// Use a map so we can recover the original object
fun populateWiresFromString(inp: String): Map<WireCoord, WireCoord> {
    val dirMap = mutableMapOf(
        'R' to Coord(0, 1),
        'L' to Coord(0, -1),
        'U' to Coord(-1, 0),
        'D' to Coord(1, 0),
    )

    var current = WireCoord(0, 0)
    val wire = mutableMapOf<WireCoord, WireCoord>()
    inp.split(",").map {
        val dir = dirMap[it[0]]!!
        val amount = it.substring(1).toInt()
        for (i in 0 until amount) {
            current = (current + dir).apply { stepsTaken = current.stepsTaken + 1 }
            // Only take into account the first visit
            if (!wire.contains(current)) {
                wire[current] = current
            }
        }
    }
    return wire
}

/**
 * Inspecting the elements finds that this is overboard, we can brute force the input sample size ~10k points each
 */
data class Line(
    val start: Pair<Int, Int>,
    val end: Pair<Int, Int>,
    val orientation: Orientation,
) {

    // Returns the manhattan dist (if any)
    @Suppress("ControlFlowWithEmptyBody")
    fun intersection(other: Line): Int? {
        when {
            this.orientation == Orientation.VERTICAL && other.orientation == Orientation.VERTICAL -> {
                if (other.start.first in start.first..end.first || other.start.second in start.second..end.second) {
                    // Logic here + accounting for the minimum value with the intersection
                }
            }

            this.orientation == Orientation.HORIZONTAL && other.orientation == Orientation.HORIZONTAL -> {}
            this.orientation == Orientation.VERTICAL && other.orientation == Orientation.HORIZONTAL -> {}
            this.orientation == Orientation.HORIZONTAL && other.orientation == Orientation.VERTICAL -> {}
        }
        return null
    }
}

enum class Orientation {
    VERTICAL, HORIZONTAL
}