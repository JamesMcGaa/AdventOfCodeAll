import java.io.File

val input = File("inputs/input22.txt").readLines()
val num = (input.size - 3) / 2
const val d1a = 1
val d1b = d1a + num
val d2a = d1b + 2
val d2b = d2a + num

fun main() {
    part22A()
    part22B()
}

fun part22B() {
    val deck1 = input.subList(d1a, d1b).map { it.toInt() }.toMutableList()
    val deck2 = input.subList(d2a, d2b).map { it.toInt() }.toMutableList()

    fun winner(deckA: MutableList<Int>, deckB: MutableList<Int>): Boolean {
        val memo = mutableSetOf<String>()
        while (deckA.isNotEmpty() && deckB.isNotEmpty()) {
            val key = deckA.toString() + deckB.toString()
            if (key in memo) {
                return true // WINS WHOLE GAME
            }
            memo.add(key)

            // Play a round (recursively if needed)
            val startA = deckA.removeFirst()
            val startB = deckB.removeFirst()
            val wonSubround = if (startA <= deckA.size && startB <= deckB.size) {
                winner(deckA.slice(0 until startA).toMutableList(), deckB.slice(0 until startB).toMutableList())
            } else {
                startA > startB
            }

            if (wonSubround) {
                deckA.add(startA)
                deckA.add(startB)
            } else {
                deckB.add(startB)
                deckB.add(startA)
            }
        }
        return deckA.isNotEmpty() // Return the results of this sub-game
    }

    winner(deck1, deck2) // Mutates the decks
    println("Part B: ${combatScore(deck1, deck2)}")
}

// 6661 too low

fun part22A() {
    val deck1 = input.subList(d1a, d1b).map { it.toInt() }.toMutableList()
    val deck2 = input.subList(d2a, d2b).map { it.toInt() }.toMutableList()

    while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
        val play1 = deck1.removeFirst()
        val play2 = deck2.removeFirst()
        if (play1 > play2) {
            deck1.add(play1)
            deck1.add(play2)
        } else {
            deck2.add(play2)
            deck2.add(play1)
        }
    }

    println("Part A: ${combatScore(deck1, deck2)}")
}

fun combatScore(deck1: List<Int>, deck2: List<Int>): Int {
    var winner = if (deck1.isNotEmpty()) deck1 else deck2
    var ret = 0
    winner.forEachIndexed { index, i ->
        ret += i * (winner.size - index)
    }
    return ret
}