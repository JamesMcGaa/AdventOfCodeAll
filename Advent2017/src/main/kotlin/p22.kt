import Utils.Coord
import Utils.Direction
import java.io.File

fun main() {
    part22A()
    part22B()
}

enum class VirusState {
    CLEAN, WEAKENED, INFECTED, FLAGGED
}

fun part22B() {
    val grid = Utils.readAsGrid("inputs/input22.txt", null) { it }
        .filterValues { it == '#' }.keys.associate { coord -> Pair(coord, VirusState.INFECTED) }.toMutableMap()
    val midPoint = File("inputs/input22.txt").readLines().size / 2
    val start = Coord(midPoint, midPoint)

    var currDir = Direction.UP
    var currPos = start
    var burstCounter = 0

    repeat(10000000) {
        when (grid.getOrDefault(currPos, VirusState.CLEAN)) {
            VirusState.CLEAN -> {
                currDir = currDir.ccw()
                grid[currPos] = VirusState.WEAKENED
            }

            VirusState.WEAKENED -> {
                burstCounter++
                grid[currPos] = VirusState.INFECTED
            }

            VirusState.INFECTED -> {
                currDir = currDir.clockwise()
                grid[currPos] = VirusState.FLAGGED
            }

            VirusState.FLAGGED -> {
                currDir = currDir.clockwise().clockwise()
                grid.remove(currPos)
            }
        }

        currPos = when (currDir) {
            Direction.UP -> currPos.copy(x = currPos.x - 1)
            Direction.DOWN -> currPos.copy(x = currPos.x + 1)
            Direction.LEFT -> currPos.copy(y = currPos.y - 1)
            Direction.RIGHT -> currPos.copy(y = currPos.y + 1)
        }
    }

    println("Part B: $burstCounter")
}

fun part22A() {
    val infected = Utils.readAsGrid("inputs/input22.txt", null) { it }
        .filterValues { it == '#' }.keys.toMutableSet()
    val midPoint = File("inputs/input22.txt").readLines().size / 2
    val start = Coord(midPoint, midPoint)

    var currDir = Direction.UP
    var currPos = start
    var burstCounter = 0

    repeat(10000) {
        if (currPos in infected) {
            currDir = currDir.clockwise()
            infected.remove(currPos)
        } else {
            burstCounter++
            currDir = currDir.ccw()
            infected.add(currPos)
        }

        currPos = when (currDir) {
            Direction.UP -> currPos.copy(x = currPos.x - 1)
            Direction.DOWN -> currPos.copy(x = currPos.x + 1)
            Direction.LEFT -> currPos.copy(y = currPos.y - 1)
            Direction.RIGHT -> currPos.copy(y = currPos.y + 1)
        }
    }

    println("Part A: $burstCounter")
}

// 2487506 low