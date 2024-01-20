import java.io.File
import java.lang.Long.max
import kotlin.math.min

data class LongCoord(
    val x: Long,
    val y: Long
)

/**
 * Apparently you can just solve this with brute force over the integers in
 * O(num ints) * O(num ranges) which is feasible
 *
 * Some added some optimization with a bit of a sort instead of checking all ranges
 */
fun main() {
    val allBounds = mutableListOf<LongCoord>()
    File("inputs/input20.txt").forEachLine {
        allBounds.add(
            LongCoord(
                it.split("-").map { it.toLong() }[0],
                it.split("-").map { it.toLong() }[1]
            ).apply {
                if (this.y < this.x) {
                    println("wtf")
                }
            }
        )
    }

    var keepMerging = true
    outer@ while (keepMerging) {
        keepMerging = false
        for (boundA in allBounds) {
            for (boundB in allBounds) {
                if (boundA != boundB && listOf(boundA.x, boundB.x, boundA.y, boundB.y) == listOf(
                        boundA.x,
                        boundB.x,
                        boundA.y,
                        boundB.y
                    ).sorted() || // Mutual Center, nonzero ends on each side
                    (boundB != boundA && boundA.x <= boundB.x && boundA.y >= boundB.y) || // One is subset of other
                    (boundA != boundB && (boundA.y + 1 == boundB.x || boundA.y == boundB.x)) // Side by side
                ) {
                    allBounds.remove(boundA)
                    allBounds.remove(boundB)
                    allBounds.add(LongCoord(min(boundA.x, boundB.x), max(boundA.y, boundB.y)))
                    keepMerging = true
                    continue@outer
                }
            }
        }
    }
    allBounds.sortBy { it.x }

    println(allBounds) // Just to note theyre all separated by 1
    println(allBounds.size)

    println(allBounds[0].y + 1)
    var su = 0L
    for (i in allBounds.indices) {
        if (i != allBounds.lastIndex) {
            su += allBounds[i+1].x - allBounds[i].y - 1
        }
    }
    println(su)
}