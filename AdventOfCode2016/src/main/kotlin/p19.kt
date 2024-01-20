val NUM_ELVES = 3014603

/**
 * ArrayDeque was way faster than MutableList
 *
 * - This is the Josephus problem apparently
 *
 * - Print the first few out, and observe the pattern
 *
 * - Use a circular linked list
 *
 * - 2-3 Finger Tree
 */
fun main() {
    runP19(false)
    runP19(true)
}

fun runP19(isPartB: Boolean) {
    var elfQueue = ArrayDeque<Coord>() // (elfNum, amount)
    for (i in 0..NUM_ELVES - 1) {
        elfQueue.add(Coord(i, 1))
    }

    while (elfQueue.size > 1) {
        if (!isPartB) {
            val newElfQueue = ArrayDeque<Coord>() // (elfNum, amount)
            for (i in 0..elfQueue.size / 2 - 1) { // leaves out last element in odds, which takes over from start
                val cur = 2 * i
                val next = Math.floorMod(2 * i + 1, elfQueue.size)
                newElfQueue.add(
                    Coord(elfQueue[cur].x, elfQueue[cur].y + elfQueue[next].y)
                )
            }
            if (elfQueue.size % 2 == 1) {
                val first = newElfQueue.removeFirst()
                newElfQueue.add(
                    Coord(elfQueue[elfQueue.lastIndex].x, first.y + elfQueue[elfQueue.lastIndex].y)
                )
            }
            elfQueue = newElfQueue
        }

        else {
            if (elfQueue.size % 10000 == 0) {
                println(elfQueue.size)
            }
            val middle = elfQueue.removeAt(elfQueue.size / 2)
            val first = elfQueue.removeFirst()
            elfQueue.add(Coord(first.x, first.y + middle.y))
        }

    }
    println(elfQueue.first().x + 1)
}