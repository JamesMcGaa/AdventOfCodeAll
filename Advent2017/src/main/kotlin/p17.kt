const val STEP_AMOUNT = 304

fun main() {
    run17a(2017)
    run17b(50000000)
}

fun run17a(amount: Int) {
    val buffer = mutableListOf(0)
    var idx = 0
    for (i in 1..amount) {
        idx = (idx + STEP_AMOUNT) % buffer.size
        buffer.add(idx + 1, i)
        idx++
    }
    println(buffer)
    println(buffer[buffer.indexOf(amount) + 1])
}

fun run17b(amount: Int) {
    var bufferSize = 1
    var idx = 0
    var current = 0
    for (i in 1..amount) {
        idx = (idx + STEP_AMOUNT) % bufferSize
        if (idx == 0) {
            current = i
        }
        bufferSize++
        idx++
    }
    println(current)
}