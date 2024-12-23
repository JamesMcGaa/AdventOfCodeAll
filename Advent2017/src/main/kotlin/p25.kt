fun main() {
    val inputHandParsed = mapOf(
        "A" to listOf(1, 1, "B", 0, -1, "C"),
        "B" to listOf(1, -1, "A", 1, 1, "D"),
        "C" to listOf(1, 1, "A", 0, -1, "E"),
        "D" to listOf(1, 1, "A", 0, 1, "B"),
        "E" to listOf(1, -1, "F", 1, -1, "C"),
        "F" to listOf(1, 1, "D", 1, 1, "A"),
    )
    var state = "A"
    var times = 12173597
    var cursor = 0
    val tapeOnesIndices = mutableSetOf<Int>()
    repeat(times) {
        val commands = inputHandParsed[state]!!
        val offset = if (cursor in tapeOnesIndices) 3 else 0
        val write = commands[offset] as Int
        val cursorMovement = commands[offset + 1] as Int
        val newState = commands[offset + 2] as String

        if (write == 0) {
            tapeOnesIndices.remove(cursor)
        } else {
            tapeOnesIndices.add(cursor)
        }
        cursor += cursorMovement
        state = newState
    }

    println("Part A (only for 25): ${tapeOnesIndices.size}")
}