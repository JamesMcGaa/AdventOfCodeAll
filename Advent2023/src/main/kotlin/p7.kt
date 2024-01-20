import java.io.File

fun Char.rank(wildcardOn: Boolean): Int {
    val cards = if (wildcardOn) listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
    else listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    return cards.indexOf(this)
}

enum class TYPE(val rank: Int) {
    FIVE_KIND(7),
    FOUR_KIND(6),
    FULL_HOUSE(5),
    THREE_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1),
}

data class Hand(
    val hand: String,
    val bet: Long,
) {

    fun freqList(wildcardOn: Boolean): List<Int> {
        if (wildcardOn) {
            val jCount = hand.count { it == 'J' }
            val noJs =
                hand.toCharArray().filter { it != 'J' }.toList().groupingBy { it }.eachCount().values.toMutableList()
            noJs.sort()
            if (noJs.isEmpty()) { // JJJJJ case
                noJs.add(0)
            }
            noJs[noJs.lastIndex] += jCount // Always optimal to cast Js as most frequent
            return noJs
        } else {
            return hand.toCharArray().toList().groupingBy { it }.eachCount().values.toList()
        }
    }

    fun type(wildcardOn: Boolean): TYPE {
        return if (freqList(wildcardOn).contains(5)) {
            TYPE.FIVE_KIND
        } else if (freqList(wildcardOn).contains(4)) {
            TYPE.FOUR_KIND
        } else if (freqList(wildcardOn).contains(3) && freqList(wildcardOn).contains(2)) {
            TYPE.FULL_HOUSE
        } else if (freqList(wildcardOn).contains(3)) {
            TYPE.THREE_KIND
        } else if (freqList(wildcardOn).count { it == 2 } == 2) {
            TYPE.TWO_PAIR
        } else if (freqList(wildcardOn).contains(2)) {
            TYPE.ONE_PAIR
        } else {
            TYPE.HIGH_CARD
        }
    }

    companion object {
        val COMPARATOR_A = getComparator(false)
        val COMPARATOR_B = getComparator(true)
        private fun getComparator(wildcardOn: Boolean): Comparator<Hand> = Comparator<Hand> { a, b ->
            when {
                a.type(wildcardOn) > b.type(wildcardOn) -> -1
                a.type(wildcardOn) < b.type(wildcardOn) -> 1
                else ->
                    tiebreaker(a, b, wildcardOn)

            }
        }

        private fun tiebreaker(a: Hand, b: Hand, wildcardOn: Boolean): Int {
            for (i in 0..4) {
                if (a.hand[i].rank(wildcardOn) > b.hand[i].rank(wildcardOn)) {
                    return -1
                } else if (a.hand[i].rank(wildcardOn) < b.hand[i].rank(wildcardOn)) {
                    return 1
                }
            }
            return 0
        }
    }
}


fun main() {
    val HANDS = mutableListOf<Hand>()
    File("inputs/input7.txt").forEachLine { line ->
        HANDS.add(
            Hand(
                line.split(" ")[0],
                line.split(" ")[1].toLong(),
            )
        )
    }
    val HANDS_A = HANDS.toMutableList()
    HANDS_A.sortWith(Hand.COMPARATOR_A)
    var counterA = 0L
    HANDS_A.forEachIndexed { index, hand -> counterA += hand.bet * (index + 1) }
    println(counterA)

    val HANDS_B = HANDS.toMutableList()
    HANDS_B.sortWith(Hand.COMPARATOR_B)
    var counterB = 0L
    HANDS_B.forEachIndexed { index, hand -> counterB += hand.bet * (index + 1) }
    println(counterB)
}


