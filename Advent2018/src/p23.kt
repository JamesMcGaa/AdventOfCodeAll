import Utils.Coord
import java.io.File
import kotlin.math.absoluteValue


fun main() {
    val nanobots =
        File("inputs/input23.txt").readLines().map {
            val nums = Utils.extractLongListFromString(it, legalBreaks = setOf(' ', ',')).map { it.toLong() }
            Nanobot(MultiCoord(nums[0], nums[1], nums[2]), nums[3])
        }
    val largest = nanobots.maxByOrNull { it.r }!!
    val partA = nanobots.count {(largest.pos - it.pos).manhattanDist <= largest.r }
    println(partA)

}
// 670 high

data class Nanobot(
    var pos: MultiCoord,
    var r: Long
)

data class MultiCoord(
    var x: Long,
    var y: Long,
    var z: Long,
) {
    val manhattanDist = x.absoluteValue + y.absoluteValue + z.absoluteValue

    operator fun plus(other: MultiCoord): MultiCoord {
        return MultiCoord(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: MultiCoord): MultiCoord {
        return MultiCoord(x - other.x, y - other.y, z - other.z)
    }
}