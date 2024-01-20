import java.io.File
import kotlin.math.max
import kotlin.math.min


data class Block(
    val xMin: Int,
    val xMax: Int,
    val yMin: Int,
    val yMax: Int,
    val zMin: Int,
    val zMax: Int,
    var isGrounded: Boolean = false,
    var isDisturbed: Boolean = false
) {
    fun down(): Block {
        return this.copy(zMin = zMin - 1, zMax = zMax - 1)
    }

    fun intersection(blockB: Block): Boolean {
        val newXMin = max(xMin, blockB.xMin)
        val newXMax = min(xMax, blockB.xMax)

        val newYMin = max(yMin, blockB.yMin)
        val newYMax = min(yMax, blockB.yMax)

        val newZMin = max(zMin, blockB.zMin)
        val newZMax = min(zMax, blockB.zMax)
        if (newXMin > newXMax || newYMin > newYMax || newZMin > newZMax) {
            return false
        }
        return true
    }

    override fun toString(): String {
        return xMin.toString() + xMax.toString() + yMin.toString() + yMax.toString() + zMin.toString() + zMax.toString() + isDisturbed.toString()
            .first()
    }
}

fun main() {
    FallingBlocks().chainReactions()
}

class FallingBlocks() {
    var blocks = mutableListOf<Block>()

    fun chainReactions() {
        var counterB = 0
        for (i in blocks.indices) {
            val copy = getBlocksCopyWithoutIndex(i)

            val ungrounded = ArrayDeque(copy)
            val grounded = mutableSetOf<Block>()
            while (ungrounded.isNotEmpty()) {
                val current = ungrounded.removeLast()

                // If we lie on the grass, mark as grounded
                if (current.zMin == 1) {
                    current.isGrounded = true
                    grounded.add(current)
                    continue
                }

                val potentialDown = current.down().apply { isDisturbed = true }

                var cantMoveDown = false
                val aggregate = ungrounded.toSet() + grounded
                neighborCheck@ for (neighbor in aggregate) {
                    if (neighbor.intersection(potentialDown)) {
                        if (neighbor.isGrounded) { // If we lie on a grounded neighbor, mark as grounded
                            current.isGrounded = true
                            grounded.add(current)
                        } else { // If we are above a floating neighbor, lets come back later
                            ungrounded.addFirst(current)
                        }
                        cantMoveDown = true
                        break@neighborCheck
                    }
                }
                if (!cantMoveDown) { // As long as no intersections were reported, we can move downwards
                    ungrounded.addFirst(potentialDown)
                }
            }
            counterB += grounded.filter { it.isDisturbed }.size
        }
        println(counterB)
    }

    fun getBlocksCopyWithoutIndex(i: Int): MutableList<Block> {
        val ret = mutableListOf<Block>()
        for (block in blocks) {
            ret.add(block.copy(isGrounded = false))
        }
        ret.removeAt(i)
        return ret
    }

    init {
        File("inputs/input22.txt").forEachLine {
            val lows = it.split("~")[0].split(",").map { it.toInt() }
            val highs = it.split("~")[1].split(",").map { it.toInt() }
            blocks.add(
                Block(
                    lows[0],
                    highs[0],
                    lows[1],
                    highs[1],
                    lows[2],
                    highs[2],
                )
            )
        }
        val ungrounded = ArrayDeque(blocks.toMutableList())
        val grounded = mutableSetOf<Block>()
        while (ungrounded.isNotEmpty()) {
            val current = ungrounded.removeLast()
            val potentialDown = current.down()

            // If we lie on the grass, mark as grounded
            if (current.zMin == 1) {
                current.isGrounded = true
                grounded.add(current)
                continue
            }

            var cantMoveDown = false
            val aggregate = ungrounded.toSet() + grounded
            neighborCheck@ for (neighbor in aggregate) {
                if (neighbor.intersection(potentialDown)) {
                    if (neighbor.isGrounded) { // If we lie on a grounded neighbor, mark as grounded
                        current.isGrounded = true
                        grounded.add(current)
                    } else { // If we are above a floating neighbor, lets come back later
                        ungrounded.addFirst(current)
                    }
                    cantMoveDown = true
                    break@neighborCheck
                }
            }
            if (!cantMoveDown) { // As long as no intersections were reported, we can move downwards
                ungrounded.addFirst(potentialDown)
            }
        }

        val bottomToTop = mutableMapOf<Block, MutableSet<Block>>()
        val topToBottom = mutableMapOf<Block, MutableSet<Block>>()
        for (block in grounded) {
            bottomToTop[block] = mutableSetOf()
            topToBottom[block] = mutableSetOf()
        }

        for (block in grounded) {
            val potentialDown = block.down()
            for (otherBlock in grounded) {
                if (otherBlock != block && otherBlock.intersection(potentialDown)) {
                    bottomToTop[otherBlock]!!.add(block)
                    topToBottom[block]!!.add(otherBlock)
                }
            }
        }

        var counterA = 0
        canRemoveCheck@ for (block in grounded) {
            for (supported in bottomToTop[block]!!) {
                if (topToBottom[supported]!!.size == 1) { // Cant remove the only support
                    continue@canRemoveCheck
                }
            }
            counterA += 1
        }
        blocks = grounded.toMutableList()
        println(counterA)
    }
}

/**
 * A very enjoyable straightforward simulation. I did this with just brute force and simulation. My solution was quite
 * slow taking 2 hours to run
 *
 * - Some other people kept a x,y to z map. Often sorted by z for some optimization
 *
 * - Some did a topological style sort on the support maps in part A to solve B without further simulation.
 *      Others just made a graph and solved it by removing a node and rerunning DFS
 */