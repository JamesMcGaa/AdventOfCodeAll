import java.io.File
fun main() {
    var counterA = 0
    var ID = 0
    val cardToMatches = hashMapOf<Int, Int>()
    File("inputs/input4.txt").forEachLine {
        line ->
        val nums = line.split(":")[1].trim()
        val left = nums.split("|")[0].trim().split(" ").filter {it.isNotBlank()}.map {it.toInt()}.toSet()
        val right = nums.split("|")[1].trim().split(" ").filter {it.isNotBlank()}.map {it.toInt()}.toSet()
        counterA += (1L shl (left intersect right).size - 1).toInt()
        cardToMatches[ID] = (left intersect right).size
        ID += 1
    }

    val cards = mutableListOf<Int>()
    for (i in 1..ID) {
        cards.add(1)
    }

    for (i in 0..ID-1) {
        val matches = cardToMatches[i]!!
        for (j in i+1..i+matches) {
            if (j < ID) {
                cards[j] += cards[i]
            }
        }
    }
    println(counterA)
    println(cards.sum())
}
