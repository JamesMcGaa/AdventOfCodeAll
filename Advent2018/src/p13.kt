import Utils.Coord
import Utils.Direction
import Utils.freqCount

data class Cart(
    var coord: Coord,
    var dir: Direction,
    var turnIdx: Int = 0
) {
    fun advance(grid: Map<Coord, Char>) {
        when (grid[coord]) {
            '\\' -> {
                dir = when (dir) {
                    Direction.UP, Direction.DOWN -> dir.ccw()
                    Direction.RIGHT, Direction.LEFT -> dir.clockwise()
                }
            }

            '/' -> {
                dir = when (dir) {
                    Direction.UP, Direction.DOWN -> dir.clockwise()
                    Direction.RIGHT, Direction.LEFT -> dir.ccw()
                }
            }

            '+' -> {
                when (turnIdx) {
                    0 -> dir = dir.ccw()
                    2 -> dir = dir.clockwise()
                }
                turnIdx = (turnIdx + 1) % 3
            }
        }
        coord = coord.moveDir(dir)
    }
}

fun main() {

    // Input parsing
    val grid = Utils.readAsGrid("inputs/input13.txt", null) { it }
    var carts = mutableListOf<Cart>()
    grid.forEach { coord, ch ->
        val direction =
            when (ch) {
                '^' -> Direction.UP
                'v' -> Direction.DOWN
                '<' -> Direction.LEFT
                '>' -> Direction.RIGHT
                else -> null
            }

        if (direction != null) {
            carts.add(Cart(coord, direction))
            grid[coord] = when (ch) {
                '^', 'v' -> '|'
                '<', '>' -> '-'
                else -> throw Exception("Improper ch")
            }
        }
    }
    
    var donePartA = false
    while (carts.size > 1) {
        val collidedCarts = mutableSetOf<Cart>()
        carts.sortedWith(compareBy<Cart> { it.coord.x }.thenBy { it.coord.y }).forEach {
            if (it !in collidedCarts) {
                it.advance(grid)
                val collisions = carts.map { it.coord }.freqCount().filter { it.value > 1 }.keys
                collidedCarts.addAll(carts.filter { it.coord in collisions })
                if (collisions.isNotEmpty() && !donePartA) {
                    val collision = collisions.first()
                    println("Part A: ${collision.y},${collision.x}")
                    donePartA = true
                }
            }
        }
        carts = carts.filter { it !in collidedCarts }.toMutableList()
    }

    val remaining = carts.first().coord
    println("Part B: ${remaining.y},${remaining.x}")
}