import java.io.File

fun main() {
    partA()
//    partB()
}

fun partB() {
    val grid = File("inputs/input2.txt").readLines()[0].split(",").map { ABVal(0,0,it.toInt()) }.toMutableList()
    var pointer = 0
    grid[1] = ABVal(1,0,0)
    grid[2] = ABVal(0,1,0)
    while (true) {
        when (grid[pointer].raw) {
            1 -> {
                grid[grid[pointer + 3]] = grid[grid[pointer + 1]] + grid[grid[pointer + 2]]
            }

            2 -> {
                grid[grid[pointer + 3]] = grid[grid[pointer + 1]] * grid[grid[pointer + 2]]
            }
            99 -> {
                break
            }
            else -> throw Exception("Illegal opcode")
        }
        pointer += 4
    }
    println("Part A: $grid[0]")
}

fun partA() {
    val grid = File("inputs/input2.txt").readLines()[0].split(",").map { it.toInt() }.toMutableList()
    var pointer = 0
    grid[1] = 12
    grid[2] = 2
    while (true) {
        when (grid[pointer]) {
            1 -> {
                grid[grid[pointer + 3]] = grid[grid[pointer + 1]] + grid[grid[pointer + 2]]
            }

            2 -> {
                grid[grid[pointer + 3]] = grid[grid[pointer + 1]] * grid[grid[pointer + 2]]
            }
            99 -> {
                break
            }
            else -> throw Exception("Illegal opcode")
        }
        pointer += 4
    }
    println("Part A: $grid[0]")
}

data class ABVal(
    val a: Int,
    val b: Int,
    val raw: Int,
) {
    operator fun plus(other: ABVal): ABVal {
        return ABVal(this.a + other.a, this.b + other.b, this.raw + other.raw)
    }
}
