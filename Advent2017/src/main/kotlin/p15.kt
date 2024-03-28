const val A_MULTIPLIER = 16807L
const val B_MULTIPLIER = 48271L
const val DIVIDER = 2147483647L
const val LOWEST_16_BITS = 65536L


fun main() {
    partA15()
    partB15()
}

fun partA15() {
    var aVal = 722L
    var bVal = 354L

    var counter = 0L
    for (i in 1..40000000) {
        aVal = (aVal * A_MULTIPLIER) % DIVIDER
        bVal = (bVal * B_MULTIPLIER) % DIVIDER
        if ((aVal % LOWEST_16_BITS) == (bVal % LOWEST_16_BITS)) {
            counter++
        }
    }
    println(counter)
}

fun partB15() {
    var aVal = 722L
    var bVal = 354L

    var counter = 0L
    for (i in 1..5000000) {
        do {
            aVal = (aVal * A_MULTIPLIER) % DIVIDER
        } while (aVal % 4L != 0L)
        do {
            bVal = (bVal * B_MULTIPLIER) % DIVIDER
        } while (bVal % 8L != 0L)
        if ((aVal % LOWEST_16_BITS) == (bVal % LOWEST_16_BITS)) {
            counter++
        }
    }
    println(counter)
}