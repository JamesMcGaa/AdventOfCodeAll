import java.io.File
import java.lang.Exception
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main(args: Array<String>) {

    val tailVisited = HashSet<Pair<Int, Int>>()
    val ropes = mutableListOf<Pair<Int, Int>>()
    val numRopes = 10
    for(i in 0 until numRopes) {
        ropes.add(Pair(0,0))
    }
    File("inputs/input9.txt").forEachLine { line ->
        val moves = line.split(" ")
        val amount = Integer.parseInt(moves[1])
        for (i in 1..amount) {
            ropes[0] = when (moves[0]) {
                "R" -> Pair(ropes[0].first + 1, ropes[0].second)
                "L" -> Pair(ropes[0].first - 1, ropes[0].second)
                "U" -> Pair(ropes[0].first, ropes[0].second + 1)
                "D" -> Pair(ropes[0].first, ropes[0].second - 1)
                else -> throw Exception()
            }
            for (i in 1 until numRopes) {
                ropes[i] = newTailLoc(ropes[i], ropes[i-1])
            }
            tailVisited.add(ropes[numRopes - 1])
        }
    }
    println(tailVisited.size)
}

fun newTailLoc(tailLoc: Pair<Int, Int>, headLoc: Pair<Int, Int>): Pair<Int, Int> {
    val deltaX = headLoc.first - tailLoc.first
    val deltaY = headLoc.second - tailLoc.second
    if (deltaX.absoluteValue > 1 || deltaY.absoluteValue > 1) {
        return Pair(tailLoc.first + deltaX.sign, tailLoc.second + deltaY.sign)
    }
    return tailLoc
}
