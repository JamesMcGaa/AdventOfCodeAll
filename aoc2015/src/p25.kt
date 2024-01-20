fun main() {
    var current = 20151125L
    var coord = Coord(1,1)
    var radius = 1
    val grid = mutableMapOf<Coord, Long>()
    val end =  Coord(2981, 3075)

    while (coord != end) {
        grid[coord] = current
        current = (current * 252533L) % 33554393L

        if(coord.r == 1) {
            radius += 1
            println(radius)
            coord = Coord(radius, 1)
        } else {
            coord = Coord(coord.r - 1, coord.c + 1,)
        }
    }
    println(current)
}

data class Coord(
    val r: Int,
    val c: Int
)

/**
 * Brute force FTW
 *
 * - There is some talk about using modular exponentiation
 */