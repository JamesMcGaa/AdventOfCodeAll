import Utils.Coord
import java.io.File

data class BathroomGuard(
    var pos: Coord,
    var vel: Coord,
) {
    fun advance() {
        pos = pos + vel
        if (pos.x >= 101) {
            pos.x -= 101
        }
        if (pos.y >= 103) {
            pos.y -= 103
        }
        if (pos.x < 0) {
            pos.x += 101
        }
        if (pos.y < 0) {
            pos.y += 103
        }
    }

    // Mid x is 50
    // Mid y is 51
    val quadrant: Int
        get() {
            if (pos.y >= 52 && pos.x >= 51) {
                return 1
            }
            if (pos.y <= 50 && pos.x <= 49) {
                return 2
            }
            if (pos.y >= 52 && pos.x <= 49) {
                return 3
            }
            if (pos.y <= 50 && pos.x >= 51) {
                return 4
            }

            return 0
        }
}

fun printGuards(guards: List<BathroomGuard>) {
    val guardPositions = guards.map {it.pos}.toSet()
    for (i in 0 until 101) {
        var row = ""
        for (j in 0 until 103) {
            row += if (Coord(i,j) in guardPositions) {
                '#'
            } else {
                '.'
            }
        }
        println(row)
     }
    println("-------------------------------------------------------------------------------------------")
}

fun main() {
    val guards = mutableListOf<BathroomGuard>()
    File("inputs/input14.txt").readLines().map {
        val nums = it.replace(" ", ",").filter { it.isDigit() || it in listOf(',', '-') }.split(",").map { it.toInt() }
        guards.add(BathroomGuard(Coord(nums[0], nums[1]), Coord(nums[2], nums[3])))
    }

    repeat(100) {
        for (guard in guards) {
            guard.advance()
        }
    }

    var partA = 1
    for (quandrant in 1..4) {
        partA *= guards.filter {it.quadrant == quandrant}.size
    }
    println("Part A: $partA")

    var iterationCount = 0
    while (true) {
        iterationCount++
        for (guard in guards) {
            guard.advance()
        }
        val quadrants = guards.map {it.quadrant}
        val guardPositions = guards.map {it.pos}.toSet()
        if (
            guardPositions.size == guards.size // Credit here to Reddit
//            quadrants.count {it == 2} == quadrants.count {it == 3} &&
//            quadrants.count {it == 1} == quadrants.count {it == 4}
//            && guards.all { it.pos.x == 0 || it.pos.x == 100 || it.pos.y == 0 || it.pos.y == 102 || (it.pos.neighbors intersect guardPositions).isNotEmpty() }
            ){
            println("Part B: $iterationCount")
            printGuards(guards)
            return
        }
    }
}