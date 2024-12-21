import Utils.Coord
import java.io.File
import kotlin.properties.Delegates

data class Square(
    val id: Int,
    var grid: MutableMap<Coord, Char>
) {
    var up by Delegates.notNull<String>()
    var down by Delegates.notNull<String>()
    var left by Delegates.notNull<String>()
    var right by Delegates.notNull<String>()

    var markedIndices = mutableListOf<Int>()

    val edges: Set<String>
        get() = setOf(up, down, left, right)

    val fullEdges: Set<String>
        get() = setOf(
            up,
            down,
            left,
            right,
            up.reversed(),
            down.reversed(),
            left.reversed(),
            right.reversed(),
        )

    fun rotate() {
        var oldUp = up
        var oldDown = down
        var oldLeft = left
        var oldRight = right
        up = oldLeft.reversed()
        right = oldUp
        down = oldRight.reversed()
        left = oldDown
        grid = rotateNormalized(grid).toMutableMap()
    }

    fun flip() {
        var oldLeft = left
        var oldRight = right
        right = oldLeft
        left = oldRight
        up = up.reversed()
        down = down.reversed()
        grid = flip(grid).toMutableMap()
    }

    init {
        val up = mutableListOf<Char>()
        val down = mutableListOf<Char>()
        val left = mutableListOf<Char>()
        val right = mutableListOf<Char>()
        for (i in 0..9) {
            up.add(grid[Coord(0, i)]!!)
            down.add(grid[Coord(9, i)]!!)
            left.add(grid[Coord(i, 0)]!!)
            right.add(grid[Coord(i, 9)]!!)
        }
        this.up = up.joinToString("")
        this.down = down.joinToString("")
        this.left = left.joinToString("")
        this.right = right.joinToString("")
    }
}

fun main() {
    val input = File("inputs/input20.txt").readLines()
    val squares = mutableListOf<Square>()
    for (i in 0..input.size / 12) {
        val id = input[12 * i].filter { it.isDigit() }.toInt()
        val grid = Utils.readAsGrid("inputs/input20.txt", 12 * i + 1..12 * i + 10) { it }

        squares.add(Square(id, grid).apply {
            if (id == 2383) {
                repeat(3) { rotate() } // See comment below
            }
        })
    }

    var partA = 1L
    val corners = mutableSetOf<Square>()
    for (square in squares) {
        var matchCount = 0
        edges@ for (edge in square.edges) {
            var matched = false

            otherSquares@ for (other in squares) {
                if (other != square) {
                    for (otherEdge in other.fullEdges) {
                        if (otherEdge == edge) {
                            square.markedIndices.add(square.edges.indexOf(edge))
                            if (matched) { // Never hit
                                println("Beware, multiple matching edges case")
                            }
                            matched = true
                        }
                    }
                }
            }

            if (matched) {
                matchCount++
            }
        }

        if (matchCount == 2) {
            /**
             * Note here our first corner has down and left corners that match, so we will
             *  rotate it thrice beforehand to get the top left corner
             */
            partA *= square.id.toLong()
            corners.add(square)
        }
    }

    println("Part A: $partA")

    val topRightCorner = squares.first { it.id == 2383 } // See comment above
    val fullImage = mutableMapOf<Coord, Square>()
    fullImage[Coord(0, 0)] = topRightCorner

    /**
     * Fix the left edge top to bottom
     */
    for (i in 1 until 12) {
        val above = fullImage[Coord(i - 1, 0)]!!
        val matched = (if (i == 11) squares else squares - corners).first { it != above && above.down in it.fullEdges }
        when {
            matched.up == above.down -> {
                // None
            }

            matched.right == above.down -> {
                repeat(3) { matched.rotate() }
            }

            matched.down == above.down -> {
                matched.flip()
                repeat(2) { matched.rotate() }
            }

            matched.left == above.down -> {
                matched.rotate()
                matched.flip()
            }

            matched.up.reversed() == above.down -> {
                matched.flip()
            }

            matched.right.reversed() == above.down -> {
                matched.flip()
                matched.rotate()
            }

            matched.down.reversed() == above.down -> {
                repeat(2) { matched.rotate() }
            }

            matched.left.reversed() == above.down -> {
                matched.rotate()
            }
        }
        assert(matched.up == above.down)
        fullImage[Coord(i, 0)] = matched
    }

    /**
     * Go row by row and fill in left to right the whole grid
     */
    for (j in 1 until 12) {
        for (i in 0 until 12) {
            val left = fullImage[Coord(i, j - 1)]!!
            assert(squares.filter { it != left && left.right in it.fullEdges }.size == 1)
            val matched = squares.first { it != left && left.right in it.fullEdges }
            when {
                matched.up == left.right -> {
                    matched.rotate()
                    matched.flip()
                }

                matched.right == left.right -> {
                    matched.flip()
                }

                matched.down == left.right -> {
                    matched.rotate()
                }

                matched.left == left.right -> {
                    // Nothing
                }

                matched.up.reversed() == left.right -> {
                    repeat(3) { matched.rotate() }
                }

                matched.right.reversed() == left.right -> {
                    repeat(2) { matched.rotate() }
                }

                matched.down.reversed() == left.right -> {
                    matched.flip()
                    matched.rotate()
                }

                matched.left.reversed() == left.right -> {
                    matched.flip()
                    repeat(2) { matched.rotate() }
                }
            }
            assert(matched.left == left.right)
            fullImage[Coord(i, j)] = matched
        }
    }

    // Assert the edges line up
    fullImage.forEach { key, value ->
        fullImage.forEach { key2, value2 ->
            if (key.x == key2.x && key.y + 1 == key2.y) {
                assert(value.right == value2.left)
            }
            if (key.y == key2.y && key.x + 1 == key2.x) {
                assert(value.down == value2.up)
            }
        }
    }

//    // Its easier to remove matching borders in list format
//    var wholeImageList = MutableList(120) { mutableListOf<Char>() }
//    for (i in 0 until 12) {
//        for (row in 0 until 10) {
//            for (j in 0 until 12) { // Iterate over outer squares
//                for (col in 0 until 10) {
//                    wholeImageList[10 * i + row].add(fullImage[Coord(i, j)]!!.grid[Coord(row, col)]!!)
//                }
//            }
//        }
//    }
//    // Drop cols 9, 19... 109 but not 119
//    for (j in (9..109 step 10).reversed()) {
//        for (i in wholeImageList.indices) {
//            wholeImageList[i].removeAt(j)
//        }
//    }
//    // Same thing for rows
//    for (i in (9..109 step 10).reversed()) {
//        wholeImageList.removeAt(i)
//    }
//    println(wholeImageList.size) // Should be 109x109
//    println(wholeImageList.first().size)

    // Its easier to remove matching borders in list format
    var wholeImageList = MutableList(96) { mutableListOf<Char>() }
    for (i in 0 until 12) {
        for (row in 1 until 9) {
            for (j in 0 until 12) { // Iterate over outer squares
                for (col in 1 until 9) {
                    wholeImageList[8 * i + row-1].add(fullImage[Coord(i, j)]!!.grid[Coord(row, col)]!!)
                }
            }
        }
    }
//    println(wholeImageList.size) // Should be 96x96
//    println(wholeImageList.first().size)

    // Now we have it in standard grid format
    var wholeImage = mutableMapOf<Coord, Char>()
    for (i in wholeImageList.indices) {
        for (j in wholeImageList.first().indices) {
            wholeImage[Coord(i, j)] = wholeImageList[i][j]
        }
    }



    // Get all sea monster orientations
    val seaMonster = Utils.readAsGrid("inputs/input20_seaMonster.txt", null) { it }
    val flipped = flip(seaMonster)
    val seaMonsterAllOrientations = mutableListOf(seaMonster, flipped)
    for (i in 1..3) {
        var base = seaMonster.toMutableMap()
        var flippedBase = flipped
        repeat(i) {
            base = rotateNormalized(base).toMutableMap()
            flippedBase = rotateNormalized(flippedBase)
        }
        seaMonsterAllOrientations.add(base)
        seaMonsterAllOrientations.add(flippedBase)
    }
    for (orientation in seaMonsterAllOrientations) {
        assert(orientation.size == seaMonster.size)
//        Utils.printGrid(orientation)
    }
//    wholeImage = Utils.readAsGrid("inputs/input20b_example.txt", null, { it })

    val wholeImageHashtagSet = wholeImage.filterValues { it == '#' || it == 'O' }.keys
    val seamonsterCoords = mutableSetOf<Coord>()
    for (start in wholeImage.keys) {
        for (orientation in seaMonsterAllOrientations) {
            val mappedSet = orientation.filterValues{it == '#'}.mapKeys { entry -> Coord(entry.key.x + start.x, entry.key.y + start.y) }.keys
//            println((mappedSet intersect wholeImageHashtagSet).size)
            if ((mappedSet intersect wholeImageHashtagSet).size == mappedSet.size) { // We got a monster
                seamonsterCoords.addAll(mappedSet)
            }
        }
    }

//    val adj = wholeImage.mapValues {if (it.key in seamonsterCoords) 'O' else it.value}
    val roughSeas = (wholeImageHashtagSet - seamonsterCoords).size
    println("Part B: $roughSeas")
}

fun rotateNormalized(grid: Map<Coord, Char>): Map<Coord, Char> {
    var newGrid = mutableMapOf<Coord, Char>()
    grid.forEach { coord, ch ->
        newGrid[Coord(coord.y, -coord.x)] = ch
    }
    val minX = newGrid.keys.minOf { it.x }
    val minY = newGrid.keys.minOf { it.y }
    return newGrid.mapKeys { (coord, _) -> Coord(coord.x - minX, coord.y - minY) }
}

fun flip(grid: Map<Coord, Char>): Map<Coord, Char> {
    var newGrid = mutableMapOf<Coord, Char>()
    val maxY = grid.keys.maxOf { it.y }
    grid.forEach { coord, ch ->
        newGrid[Coord(coord.x, maxY - coord.y)] = ch
    }
    return newGrid
}
