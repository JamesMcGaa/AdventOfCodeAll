import Utils.Coord
import java.io.File

val INPUT_8 = File("inputs/input8.txt").readLines()
val X = INPUT_8.size
val Y = INPUT_8[0].length

fun main() {
    val grid = mutableMapOf<Coord, Char>()

    val antennae = mutableMapOf<Char, MutableSet<Coord>>()
    val antinodesA = mutableSetOf<Coord>()
    val antinodesB = mutableSetOf<Coord>()
    INPUT_8.forEachIndexed { r, s ->
        s.forEachIndexed { c, ch ->
            val coord = Coord(r, c)
            grid[coord] = ch
            if (ch.isLetter() || ch.isDigit()) {
                antennae[ch] = antennae.getOrDefault(ch, mutableSetOf(coord)).apply { add(coord) }
            }
        }
    }

    antennae.forEach { (_, coords) ->
        coords.forEach { first ->
            coords.forEach { second ->
                if (first != second) {
                    val deltaToFirst = (first - second)
                    val deltaToSecond = (second - first)
                    var projectedFromFirst = first + deltaToFirst
                    var projectedFromSecond = second + deltaToSecond

                    // Part A
                    if (coordInBounds(projectedFromFirst)) {
                        antinodesA.add(projectedFromFirst)
                    }
                    if (coordInBounds(projectedFromSecond)) {
                        antinodesA.add(projectedFromSecond)
                    }

                    // Part B
                    antinodesB.add(first)
                    antinodesB.add(second)
                    while (coordInBounds(projectedFromFirst)) {
                        antinodesB.add(projectedFromFirst)
                        projectedFromFirst += deltaToFirst
                    }
                    while (coordInBounds(projectedFromSecond)) {
                        antinodesB.add(projectedFromSecond)
                        projectedFromSecond += deltaToSecond
                    }
                }
            }
        }
    }

    for (i in 0..<X) {
        for (j in 0..<Y) {
            if (Coord(i,j) in antinodesB) {
                print("#")
            }
            else {
                print(grid[Coord(i,j)])
            }
        }
        print("\n")
    }

    println("Part A: ${antinodesA.size}")
    println("Part B: ${antinodesB.size}")
}

fun coordInBounds(coord: Coord): Boolean = coord.x in 0..<X && coord.y in 0..<Y