import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val LONG_DIST_ROWS = hashSetOf<Int>()
    val LONG_DIST_COLS = hashSetOf<Int>()
    val MULTIPLYING_FACTOR = 1_000_000
    val BOARD = File("inputs/input11.txt").readLines().map { it.toCharArray().toMutableList() }.toMutableList()

    val PLANETS_B = hashSetOf<Pair<Int, Int>>()
    for (i in 0..BOARD.lastIndex){
        for (j in 0..BOARD[i].lastIndex){
            if (BOARD[i][j] == '#') {
                PLANETS_B.add(Pair(i,j))
            }
        }
    }

    val boardA = BOARD.map {it.toMutableList()}.toMutableList()
    val rowLen = boardA[0].size
    for (i in (0 .. boardA.lastIndex).reversed()) {
        if (boardA[i].toSet().size == 1) {
            LONG_DIST_ROWS.add(i)
            boardA.add(i, List(rowLen){ '.' }.toMutableList())
        }
    }
    for (i in (0 .. rowLen - 1).reversed()) {
        val col = boardA.map {it -> it[i]}.toSet()
        if (col.size == 1) {
            LONG_DIST_COLS.add(i)
            for (j in 0 .. boardA.lastIndex) {
                boardA[j].add(i, '.' )
            }
        }
    }

    val PLANETS = hashSetOf<Pair<Int, Int>>()
    for (i in 0..boardA.lastIndex){
        for (j in 0..boardA[i].lastIndex){
            if (boardA[i][j] == '#') {
                PLANETS.add(Pair(i,j))
            }
        }
    }

    var counterA = 0
    for (planet in PLANETS){
        for (otherPlanet in PLANETS) { // Trick: Manhattan distance
            counterA += abs(planet.first - otherPlanet.first)
            counterA += abs(planet.second - otherPlanet.second)
        }
    }
    println(counterA / 2)

    var counterB = 0L
    for (planet in PLANETS_B){
        for (otherPlanet in PLANETS_B) {
            val rowMin = min(planet.first, otherPlanet.first)
            val rowMax = max(planet.first, otherPlanet.first)
            if (rowMin != rowMax) {
                for (i in rowMin + 1 .. rowMax) {
                    if (i in LONG_DIST_ROWS) {
                        counterB += MULTIPLYING_FACTOR
                    }
                    else {
                        counterB += 1
                    }
                }
            }

            val colMin = min(planet.second, otherPlanet.second)
            val colMax = max(planet.second, otherPlanet.second)
            if (colMin != colMax) {
                for (i in colMin + 1 .. colMax ) {
                    if (i in LONG_DIST_COLS) {
                        counterB += MULTIPLYING_FACTOR
                    }
                    else {
                        counterB += 1
                    }
                }
            }
        }
    }
    println(counterB / 2)
}

