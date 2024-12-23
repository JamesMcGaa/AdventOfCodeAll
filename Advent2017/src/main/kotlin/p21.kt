import Utils.Coord
import java.io.File

/**
 * Overall happy with this approach
 *
 * Reddit suggests just keeping track of the types of 3x3 subgrids, and keeping count of the number of each unique 3x3 grid since
 * they can be considered independently
 */
fun main() {
    // Map all rotations of the input to the output (not rotated)
    val enhancementRules = File("inputs/input21.txt").readLines().map { it.split(" => ").map { it.split("/") } }
    val patternToEnhancement = mutableMapOf<Map<Coord, Char>, Map<Coord, Char>>()
    for (rule in enhancementRules) {
        val enhancedOutput = strListToGrid(rule[1])
        var inputBase = strListToGrid(rule[0])
        var inputFlipped = Utils.flip(inputBase)
        patternToEnhancement[inputBase] = enhancedOutput
        patternToEnhancement[inputFlipped] = enhancedOutput
        repeat(3) {
            inputBase = Utils.rotateClockwise(inputBase)
            inputFlipped = Utils.rotateClockwise(inputFlipped)
            patternToEnhancement[inputBase] = enhancedOutput
            patternToEnhancement[inputFlipped] = enhancedOutput
        }
    }

    var grid = Utils.readAsGrid("inputs/input21_startingGrid.txt", null) { it }
    var gridSize = 3
    for (i in 1..18) {
        val chunkSize = if (gridSize % 2 == 0) {
            2
        } else {
            3
        }
        val outputPatternSize = chunkSize + 1
        val numChunks = gridSize / chunkSize

        val newGrid = mutableMapOf<Coord, Char>()
        for (chunkI in 0 until numChunks) {
            for (chunkJ in 0 until numChunks) {

                // Grab the chunk
                val subPattern = mutableMapOf<Coord, Char>()
                for (i in 0 until chunkSize) {
                    for (j in 0 until chunkSize) {
                        subPattern[Coord(i, j)] = grid[Coord(chunkSize * chunkI + i, chunkSize * chunkJ + j)]!!
                    }
                }

                // Map it to the new chunk
                val outputSubpattern = patternToEnhancement[subPattern]!!
                outputSubpattern.forEach { coord, ch ->
                    newGrid[Coord(outputPatternSize * chunkI + coord.x, outputPatternSize * chunkJ + coord.y)] = ch
                }
            }
        }
        gridSize = if (gridSize % 2 == 0) {
            3 * gridSize / 2
        } else {
            4 * gridSize / 3
        }
        grid = newGrid

        if (i == 5) {
            println("Part A: ${grid.filterValues { it == '#' }.size}")
        } else if (i == 18) {
            println("Part B: ${grid.filterValues { it == '#' }.size}")
        }
    }
}

fun strListToGrid(inp: List<String>): Map<Coord, Char> {
    val ret = mutableMapOf<Coord, Char>()
    inp.forEachIndexed { i, string ->
        string.forEachIndexed { j, ch ->
            ret[Coord(i, j)] = ch
        }
    }
    return ret
}