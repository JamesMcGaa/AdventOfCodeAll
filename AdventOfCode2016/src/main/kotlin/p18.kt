fun main() {
//    val TOTAL_ROWS = 40
    val TOTAL_ROWS = 400000
    val INPUT = "^^^^......^...^..^....^^^.^^^.^.^^^^^^..^...^^...^^^.^^....^..^^^.^.^^...^.^...^^.^^^.^^^^.^^.^..^.^"
    val rows = mutableMapOf<Coord, Boolean>() //isTrap
    for (col in INPUT.indices) {
        rows[Coord(0, col)] = INPUT[col] == '^'
    }
    for (row in 1..TOTAL_ROWS-1) {
        for (col in INPUT.indices) {
            val left = rows[Coord(row - 1, col - 1)] ?: false
            val center = rows[Coord(row - 1, col)] ?: false
            val right = rows[Coord(row - 1, col + 1)] ?: false
            rows[Coord(row, col)] =
                left && center && !right ||
                !left && center && right
            || left && !center && !right
                || !left && !center && right
        }
    }
    println(rows.values.count { !it })
}