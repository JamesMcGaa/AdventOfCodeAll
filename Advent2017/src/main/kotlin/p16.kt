import java.io.File
import java.util.*
val x = 10000


fun main() {
    var programs = run16(0)
    val oneSwap = run16(1).toList()
    println(oneSwap.joinToString(""))
    val xSwap = run16(x).toList()
    repeat(1000000000 / x) {
        var newPrograms = mutableListOf<Char>()
        xSwap.forEach { ch ->
            newPrograms.add(programs[ch - 'a'])
        }
        programs = newPrograms
    }
    println(programs.joinToString(""))
//    run16(100000) // Too slow
}

fun run16(times: Int): MutableList<Char> {
    val commands = File("inputs/input16.txt").readLines().first().split(",")
    var programs = mutableListOf<Char>()
    for (i in 0..15) {
        programs.add(('a'.toInt() + i).toChar())
    }
    repeat(times) {
        for (command in commands) {
            when (command[0]) {
                's' -> {
                    val amount = command.substring(1).toInt()
                    programs = programs.slice(programs.size - amount..programs.size - 1).plus(
                        programs.slice(
                            0..programs.size - amount - 1
                        )
                    )
                        .toMutableList()
                }

                'x' -> {
                    val indices = command.substring(1).split("/").map { it.toInt() }
                    Collections.swap(programs, indices[0], indices[1])
                }

                'p' -> {
                    val programLetters = command.substring(1).split("/").map { it[0] }
                    Collections.swap(
                        programs,
                        programs.indexOf(programLetters[0]),
                        programs.indexOf(programLetters[1])
                    )
                }
            }
        }
    }
    return programs
}