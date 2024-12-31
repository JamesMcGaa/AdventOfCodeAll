import Utils.Coord
import java.io.File

typealias BranchState = Pair<MutableSet<Coord>, MutableSet<Coord>>

/**
 * Credits go to https://www.reddit.com/r/adventofcode/comments/a7uk3f/comment/ec5y3lm/
 */
fun main() {
    val input = File("inputs/input20.txt").readLines().first().removePrefix("^").removeSuffix("$")

    val origin = Coord(0, 0)
    var allReachableSoFar = mutableSetOf<Coord>(origin) // we start at the origin
    val graph = mutableMapOf<Coord, MutableSet<Coord>>()

    val stackOfBranches = mutableListOf<BranchState>()
    var currentState: BranchState = Pair(mutableSetOf(origin), mutableSetOf())

    input.forEach { ch ->
        when (ch) {
            '(' -> {
                stackOfBranches.add(currentState) // Remember where we are now
                currentState = Pair(allReachableSoFar.toMutableSet(), mutableSetOf()) // Remember where we start our call at so we can start at the same spot per our subgroups
            }

            '|' -> {
                currentState.second.addAll(allReachableSoFar)
                allReachableSoFar = currentState.first
            }

            ')' -> {
                allReachableSoFar = currentState.second // Get all the end states we reached this branch and set it to the new all reachable
                currentState = stackOfBranches.removeLast() // Reset to the where we left off
            }

            else -> {
                when (ch) { // We are forced to move each of our currently reachable paths
                    'N' -> {
                        allReachableSoFar = allReachableSoFar.map { coordStart ->
                            val coordEnd = coordStart.up
                            graph.getOrPut(coordStart) { mutableSetOf() }.apply { add(coordEnd) }
                            graph.getOrPut(coordEnd) { mutableSetOf() }.apply { add(coordStart) }
                            return@map coordEnd
                        }.toMutableSet()
                    }

                    'E' -> {
                        allReachableSoFar = allReachableSoFar.map { coordStart ->
                            val coordEnd = coordStart.right
                            graph.getOrPut(coordStart) { mutableSetOf() }.apply { add(coordEnd) }
                            graph.getOrPut(coordEnd) { mutableSetOf() }.apply { add(coordStart) }
                            return@map coordEnd
                        }.toMutableSet()
                    }

                    'W' -> {
                        allReachableSoFar = allReachableSoFar.map { coordStart ->
                            val coordEnd = coordStart.left
                            graph.getOrPut(coordStart) { mutableSetOf() }.apply { add(coordEnd) }
                            graph.getOrPut(coordEnd) { mutableSetOf() }.apply { add(coordStart) }
                            return@map coordEnd
                        }.toMutableSet()
                    }

                    'S' -> {
                        allReachableSoFar = allReachableSoFar.map { coordStart ->
                            val coordEnd = coordStart.down
                            graph.getOrPut(coordStart) { mutableSetOf() }.apply { add(coordEnd) }
                            graph.getOrPut(coordEnd) { mutableSetOf() }.apply { add(coordStart) }
                            return@map coordEnd
                        }.toMutableSet()
                    }

                    else -> throw Exception("Illegal char")
                }

            }
        }
    }

    val dists = Utils.generalizedBFS(
        graph,
        origin,
        isLegal = { coord, grid -> coord in grid },
        neighbors = { coord, _ -> graph[coord]!! },
    )

    println("Part A: ${dists.values.max()}")
    println("Part B: ${dists.count { it.value >= 1000 }}")

}