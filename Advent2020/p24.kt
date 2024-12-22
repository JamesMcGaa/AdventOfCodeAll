import Utils.Coord
import java.io.File

fun main() {
    var blackTiles = mutableSetOf<Coord>()

    File("inputs/input24.txt").forEachLine {
        val dest = traverse(Coord(0, 0), it)
        if (dest in blackTiles) {
            blackTiles.remove(dest)
        } else {
            blackTiles.add(dest)
        }
    }

    println("Part A: ${blackTiles.size}")

    repeat(100) {
        val newFrontier = mutableMapOf<Coord, Int>()
        blackTiles.forEach { blackTile ->
            blackTile.hexagonalNeighbors().forEach { blacksNeighbor ->
                newFrontier[blacksNeighbor] = newFrontier.getOrDefault(blacksNeighbor, 0) + 1
            }
        }
        val newBlack = mutableSetOf<Coord>()
        newFrontier.forEach { coord, adjCount ->
            if (coord !in blackTiles && adjCount == 2) { // White tile with 2 neighbors
                newBlack.add(coord)
            }
        }
        blackTiles.forEach { blackCoord ->
            if ((blackCoord.hexagonalNeighbors() intersect blackTiles).size in setOf(
                    1,
                    2
                )
            ) { // Carry over certain black tiles
                newBlack.add(blackCoord)
            }
        }
        blackTiles = newBlack
    }

    println("Part B: ${blackTiles.size}")
}

fun traverse(start: Coord, remaining: String): Coord {
    if (remaining.isEmpty()) {
        return start
    }
    return when {
        remaining.startsWith("ne") -> traverse(Coord(start.x + 1, start.y + 1), remaining.removePrefix("ne"))
        remaining.startsWith("nw") -> traverse(Coord(start.x - 1, start.y + 1), remaining.removePrefix("nw"))
        remaining.startsWith("se") -> traverse(Coord(start.x + 1, start.y - 1), remaining.removePrefix("se"))
        remaining.startsWith("sw") -> traverse(Coord(start.x - 1, start.y - 1), remaining.removePrefix("sw"))
        remaining.startsWith("e") -> traverse(Coord(start.x + 2, start.y), remaining.removePrefix("e"))
        remaining.startsWith("w") -> traverse(Coord(start.x - 2, start.y), remaining.removePrefix("w"))
        else -> throw Exception("Bad hexagonal direction")
    }
}

fun Coord.hexagonalNeighbors(): Set<Coord> {
    return setOf(
        Coord(this.x + 1, this.y + 1),
        Coord(this.x - 1, this.y + 1),
        Coord(this.x + 1, this.y - 1),
        Coord(this.x - 1, this.y - 1),
        Coord(this.x + 2, this.y),
        Coord(this.x - 2, this.y),
    )
}