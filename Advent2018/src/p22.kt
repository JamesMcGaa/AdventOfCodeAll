import Utils.Coord

// Inputs
const val depth = 4848L
val target = Coord(15,700)

const val geologicIndexModulus = 20183L

val geoMemo = mutableMapOf<Coord, Long>()
fun geologicIndex(coord: Coord): Long {
    if (coord in geoMemo) {
        return geoMemo[coord]!!
    }
    val ret = if (coord == Coord(0, 0) || coord == target) {
        0L
    } else if (coord.x == 0) {
        (coord.y * 48271L) % geologicIndexModulus
    } else if (coord.y == 0) {
        (coord.x * 16807L) % geologicIndexModulus
    } else {
        (erosionLevel(coord.copy(x = coord.x - 1)) * erosionLevel(coord.copy(y = coord.y - 1))) % geologicIndexModulus
    }
    geoMemo[coord] = ret
    return ret
}

val erosionMemo = mutableMapOf<Coord, Long>()
fun erosionLevel(coord: Coord): Long {
    if (coord in erosionMemo) {
        return erosionMemo[coord]!!
    }
    val ret = (geologicIndex(coord) + depth) % geologicIndexModulus
    erosionMemo[coord] = ret
    return ret
}

fun caveType(coord: Coord): Long {
    return erosionLevel(coord) % 3L
}

data class ClimbingState(
    val coord: Coord,
    val tools: String,
)

fun main() {

    var su = 0L
    for (x in 0..target.x) {
        for (y in 0..target.y) {
            su += caveType(Coord(x, y))
        }
    }
    println("Part A: $su")

    val TORCH = "TORCH"
    val CLIMBING_GEAR = "CLIMBING_GEAR"
    val NEITHER = "NEITHER"

    val start = ClimbingState(Coord(0, 0), TORCH)
    val end = ClimbingState(target, TORCH)

    val allNodes = mutableSetOf<ClimbingState>()

    /**
     * To get these bounds, run as normal on 0..targetX 0..targetY - this gives a dist UPPER that upper bounds the max bounds
     *
     * No
     */
    //977 too high
    val xBound = 100
    val yBound = 977
    for (x in 0..xBound) {
        for (y in 0..yBound) {
            for (tool in setOf(TORCH, CLIMBING_GEAR, NEITHER)) {
                allNodes.add(ClimbingState(Coord(x, y), tool))
            }
        }
    }

    fun legalTools(current: Long): Set<String> {
        return when (current) {
            0L -> setOf(CLIMBING_GEAR, TORCH)
            1L -> setOf(CLIMBING_GEAR, NEITHER)
            2L -> setOf(TORCH, NEITHER)
            else -> throw Exception()
        }
    }

    val dists = Utils.generalizedDijkstra(
        start,
        allNodes.toSet(),
        neighbors = { node ->
            val legal = mutableSetOf<ClimbingState>()
            // Change tools to something currently legal
            legal.addAll(legalTools(caveType(node.coord)).map { tool -> ClimbingState(node.coord, tool) })
            // Look at neighbors and filter to ones that are legal withour current tool
            legal.addAll(node.coord.manhattanNeighbors.filter { neighbor ->
                neighbor.x in 0..xBound && neighbor.y in 0..yBound && node.tools in legalTools(
                    caveType(neighbor)
                )
            }
                .map { coord -> ClimbingState(coord, node.tools) })
            legal
        },
        edgeWeight = { v1, v2 -> if (v1.coord == v2.coord) 7 else 1 },
    ).first

    println("Part B: ${dists[end]}")
}