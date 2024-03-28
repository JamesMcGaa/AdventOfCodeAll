import java.io.File

fun main() {
    File("inputs/input9.txt").readLines().map { score(it) }
}

fun score(input: String): Int {

    // Strip !s
    var raw = input
    while (raw.contains('!')) {
        var ptr = 0
        var new = ""
        while (ptr in raw.indices) { // Ranges define contains() for 'in'
            if (raw[ptr] == '!') {
                ptr += 2
            } else {
                new += raw[ptr]
                ptr += 1
            }
        }
        raw = new
    }

    // Remove garbage
    val processed = raw
    var ptr = 0
    var final = ""
    var isGarbage = false
    var garbageCounter = 0
    while (ptr in processed.indices) { // Ranges define contains() for 'in'
        if (isGarbage) garbageCounter++
        when (raw[ptr]) {
            '<' -> {
                isGarbage = true
            }
            '>' -> {
                isGarbage = false
                garbageCounter--
            }
            '{', '}' -> {
                if (!isGarbage){
                    final += raw[ptr]
                }
            }
            else -> Unit
        }
        ptr++
    }


    var depth = 0
    var counter = 0
    for (ch in final) {
        if (ch == '{') {
            depth++
            counter += depth // WLOG calculate counter on open
        } else {
            depth--
        }
    }
    println(final)
    println(counter)
    println(garbageCounter)

    return counter
}