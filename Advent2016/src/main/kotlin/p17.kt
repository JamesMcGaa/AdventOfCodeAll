import java.lang.Integer.max

var INPUT = "awrkjxxr"
val END_COORD = Coord(3, -3)
val LEGALS = mutableSetOf('b', 'c', 'd', 'e', 'f')

fun main() {
    for (test in listOf("ihgpwlah", "kglvqrro", "ulqzkmiv", "awrkjxxr")) {
        INPUT = test
        run()
    }
    for (test in listOf("ihgpwlah", "kglvqrro", "ulqzkmiv", "awrkjxxr")) {
        INPUT = test
        run(true)
    }
}

fun run(isPartB: Boolean = false) {
    val start = StringPath(Coord(0, 0), "")
    var frontier = mutableSetOf<StringPath>(start)
    var longestSoFar = 0
    while (frontier.isNotEmpty()) { // Note DFS would be more memory efficient for part B
        val newFronter = mutableSetOf<StringPath>()
        frontier.forEach {
            if (it.coord == END_COORD) {
                if (!isPartB) {
                    println(it.path)
                    return
                } else {
                    longestSoFar = max(longestSoFar, it.path.length)
                    return@forEach
                }
            }
            newFronter.addAll(it.getNeighbors())
        }
        frontier = newFronter
    }

    if (isPartB) {
        println(longestSoFar)
    } else {
        throw Exception("Did not find exit for part A")
    }

}

class StringPath(
    val coord: Coord,
    val path: String
) {
    val hash = md5(INPUT + path)
    val isLegal = coord.x <= 3 && coord.x >= 0 && coord.y >= -3 && coord.y <= 0
    fun getNeighbors(): List<StringPath> {
        val ret = mutableListOf<StringPath>()
        if (LEGALS.contains(hash[0])) {
            ret.add(StringPath(coord.copy(y = coord.y + 1), path + "U"))
        }
        if (LEGALS.contains(hash[1])) {
            ret.add(StringPath(coord.copy(y = coord.y - 1), path + "D"))
        }
        if (LEGALS.contains(hash[2])) {
            ret.add(StringPath(coord.copy(x = coord.x - 1), path + "L"))
        }
        if (LEGALS.contains(hash[3])) {
            ret.add(StringPath(coord.copy(x = coord.x + 1), path + "R"))
        }
        return ret.filter { it.isLegal }
    }
}
