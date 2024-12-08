import java.io.File

data class Coord(
    val x: Int,
    val y: Int,
) {
    operator fun plus(other: Coord): Coord {
        return Coord(x + other.x, y + other.y)
    }

    operator fun minus(other: Coord): Coord {
        return Coord(x - other.x, y - other.y)
    }

}

val X = File("inputs/input8.txt").readLines().size
val Y = File("inputs/input8.txt").readLines()[0].length

fun main() {
    val grid = mutableMapOf<Coord, Char>()
    val antennae = mutableMapOf<Char, MutableSet<Coord>>()
    val antinodesA = mutableSetOf<Coord>()
    val antinodesB = mutableSetOf<Coord>()
    File("inputs/input8.txt").readLines().forEachIndexed { r, s ->
        s.forEachIndexed { c, ch ->
            val coord = Coord(r, c)
            grid[coord] = ch
            if (ch != '.') {
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
                    var reflected1 = first + deltaToFirst
                    var reflected2 = second + deltaToSecond

                    if (coordInBounds(reflected1)) {
                        antinodesA.add(reflected1)
                    }
                    if (coordInBounds(reflected2)) {
                        antinodesA.add(reflected2)
                    }
                    while (coordInBounds(reflected1)) {
                        antinodesB.add(reflected1)
                        reflected1 += deltaToFirst
                    }
                    while (coordInBounds(reflected2)) {
                        antinodesB.add(reflected2)
                        reflected2 += deltaToSecond
                    }
                }
            }
        }
    }

    println(antinodesA.size)
    println(antinodesB.size)
}

fun coordInBounds(coord: Coord): Boolean = coord.x in 0 until X && coord.y in 0 until Y