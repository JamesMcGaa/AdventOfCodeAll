import java.io.File
import java.util.*

/**
 * Learned a bit of java.util.Collections
 *
 * Some people brute forced all permutations for partB
 *
 * Others did smarter logic for the "rotate based case" - I could probably upgrade this
 */
fun main() {
    runP21(false, "abcdefgh")
    runP21(true, "bdfhgeca")
    runP21(true, "fbgdceah")
}

fun runP21(isPartB: Boolean, input: String) {
    var password = input.toMutableList()
    val lines = File("inputs/input21.txt").readLines().toMutableList()
    if (isPartB) { // this time go top to bottom
        lines.reverse()
    }
    lines.forEach { line ->
        val split = line.split(" ")
        when (split[0]) {
            "swap" -> {
                when (split[1]) {
                    "position" -> {
                        Collections.swap(password, split[2].toInt(), split[5].toInt())
                    }

                    "letter" -> {
                        for (i in password.indices) {
                            if (password[i] == split[2][0]) {
                                password[i] = split[5][0]
                            } else if (password[i] == split[5][0]) {
                                password[i] = split[2][0]
                            }
                        }
                    }
                }
            }

            "rotate" -> {
                val shift: Int = when (split[1]) {
                    "based" -> {
                        if (isPartB) {
                            var ret: Int? = null
                            for (attemptedShift in password.indices) {
                                val copy = password.toMutableList()
                                Collections.rotate(copy, attemptedShift)
                                var wouldHaveBeenShift = copy.indexOf(split[6][0])
                                if (wouldHaveBeenShift >= 4) wouldHaveBeenShift += 2 else wouldHaveBeenShift += 1
                                Collections.rotate(copy, wouldHaveBeenShift)
                                if (copy == password) {
                                    ret = attemptedShift * -1
                                }
                            }
                            if (ret == null) {
                                throw Exception("Did not find right offset")
                            }
                            ret
                        } else {
                            var shift = password.indexOf(split[6][0])
                            if (shift >= 4) shift += 2 else shift += 1
                            shift
                        }
                    }

                    "left" -> {
                        -1 * split[2].toInt()
                    }

                    "right" -> {
                        split[2].toInt()
                    }

                    else -> throw Exception("bad rotation")
                }
                Collections.rotate(password, if (isPartB) -1 * shift else shift)
            }

            "reverse" -> {
                val x = split[2].toInt()
                val y = split[4].toInt()
                val newPass = password.subList(0, x).toMutableList().apply {
                    addAll(password.subList(x, y + 1).reversed())
                    addAll(password.subList(y + 1, password.lastIndex + 1))
                }
                password = newPass
            }

            "move" -> {
                if (isPartB) {
                    val movedChar = password.removeAt(split[5].toInt())
                    password.add(split[2].toInt(), movedChar)
                } else {
                    val movedChar = password.removeAt(split[2].toInt())
                    password.add(split[5].toInt(), movedChar)
                }
            }
        }
    }

    println(password.joinToString(""))
}