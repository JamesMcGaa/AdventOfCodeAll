import Utils.CircularLinkedListNode

/**
 * Could have used a deque too
 */
fun main() {
    val numPlayers = 424
    val numMarbles = 71144 * 100

    val scoreTable = mutableMapOf<Int, Long>()
    var current = CircularLinkedListNode(0)
    var currentPlayer = 0

    for (marble in 1..numMarbles) {
        if (marble % 23 == 0) {
            val spliced = current.prev.prev.prev.prev.prev.prev.prev.splice()
            current = spliced.second
            scoreTable[currentPlayer] = scoreTable.getOrDefault(currentPlayer, 0) + marble + spliced.first.value
        } else {
            current = current.next.insert(CircularLinkedListNode(marble))
        }

        currentPlayer = (currentPlayer + 1) % numPlayers

        if (marble == numMarbles / 100) {
            println("Part A: ${scoreTable.values.max()}")
        } else if (marble == numMarbles) {
            println("Part B: ${scoreTable.values.max()}")
        }
    }
}