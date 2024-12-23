import java.io.File

data class BridgePart(
    val id: Int,
    val v1: Int,
    val v2: Int,
) {
    val manhattanDist = v1 + v2
}

fun main() {
    val input = File("inputs/input24.txt").readLines()
    val pipes = input.mapIndexed { idx, it ->
        val split = it.split('/').map { it.toInt() }
        BridgePart(idx, split[0], split[1])
    }
    val pipesByStartVal = mutableMapOf<Int, MutableSet<BridgePart>>()
    pipes.forEach {
        pipesByStartVal[it.v1] =
            pipesByStartVal.getOrDefault(it.v1, mutableSetOf()).apply { add(it) }
        pipesByStartVal[it.v2] =
            pipesByStartVal.getOrDefault(it.v2, mutableSetOf()).apply { add(it) }
    }
    assert(input.size == pipes.size) // No duplicates

    fun maxPossible(prevEnd: Int, soFar: Set<BridgePart>, shouldReturnDist: Boolean = false, targetDist: Int? = null): Int {
        val possiblePipes = pipesByStartVal.getOrDefault(prevEnd, mutableSetOf()) - soFar
        if (possiblePipes.isEmpty()) {
            if (shouldReturnDist) {
                return soFar.size
            } else if (targetDist != null) {
                if (soFar.size == targetDist) {
                    return soFar.sumOf { it.manhattanDist }
                }
                return 0
            }
            return soFar.sumOf { it.manhattanDist }
        }
        return possiblePipes.maxOf { newPipe ->
            if (newPipe.v1 == prevEnd) {
                maxPossible(newPipe.v2, soFar + setOf(newPipe), shouldReturnDist, targetDist)
            } else {
                maxPossible(newPipe.v1, soFar + setOf(newPipe), shouldReturnDist, targetDist)
            }
        }
    }

    val partA = maxPossible(0, setOf())
    val maxDist = maxPossible(0, setOf(), shouldReturnDist = true)
    val partB = maxPossible(0, setOf(), shouldReturnDist = false, targetDist = maxDist)

    println("Part A: $partA")
    println("Part B: $partB")
}