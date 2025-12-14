import Utils.Coord

fun main() {
    println("Part A: ${p7a("inputs/input7.txt")}")
    println("Part B: ${p7b("inputs/input7.txt")}")
}

fun p7a(filename: String): Int {
    val grid = Utils.readAsGrid(filename) { it }
    val start = Utils.findCoord('S', grid).y
    var beamSet = mutableSetOf(start)
    var xRowPtr = 1
    val bottomRow = grid.keys.maxOf { it.x }
    var splitCount = 0
    while (xRowPtr != bottomRow) {
        val newBeamSet = mutableSetOf<Int>()
        for (beam in beamSet) {
            val projected = Coord(xRowPtr, beam)
            if (grid[projected] == '^') {
                splitCount++
                newBeamSet.add(beam - 1)
                newBeamSet.add(beam + 1)
            } else {
                newBeamSet.add(beam)
            }
        }
        beamSet = newBeamSet
        xRowPtr += 1
    }
    return splitCount
}

fun p7b(filename: String): Long {
    val grid = Utils.readAsGrid(filename) { it }
    val start = Utils.findCoord('S', grid).y
    var beamSet = mutableMapOf(start to 1L)
    var xRowPtr = 1
    val bottomRow = grid.keys.maxOf { it.x }
    while (xRowPtr != bottomRow) {
        val newBeamSet = mutableMapOf<Int, Long>()
        for (beam in beamSet) {
            val projected = Coord(xRowPtr, beam.key)
            if (grid[projected] == '^') {
                newBeamSet[beam.key - 1] = newBeamSet.getOrDefault(beam.key - 1, 0) + beam.value
                newBeamSet[beam.key + 1] = newBeamSet.getOrDefault(beam.key + 1, 0) + beam.value
            } else {
                newBeamSet[beam.key] = newBeamSet.getOrDefault(beam.key, 0) + beam.value
            }
        }
        beamSet = newBeamSet
        xRowPtr += 1
    }
    return beamSet.values.sum()
}
