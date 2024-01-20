import java.io.File

fun main() {
    val commandsUnsorted =
        File("inputs/input3.txt").readLines()
            .map { it.split(" ").filter { it.isNotBlank() }.map { it.toInt() }}
    val commands =
        File("inputs/input3.txt").readLines()
            .map { it.split(" ").filter { it.isNotBlank() }.map { it.toInt() }.sorted() }
    println(commands.filter { it[2] < it[0] + it[1] }.size)
    val commandsB = mutableListOf<List<Int>>()
    for (i in 0..(commandsUnsorted.size / 3) - 1) {
        for (col in 0..2) {
            commandsB.add(
                mutableListOf(
                    commandsUnsorted[3 * i][col],
                    commandsUnsorted[3 * i + 1][col],
                    commandsUnsorted[3 * i + 2][col],
                ).sorted()
            )
        }
    }
    println(commandsB.filter { it[2] < it[0] + it[1] }.size)
}