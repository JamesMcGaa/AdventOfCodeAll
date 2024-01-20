import java.io.File
import java.lang.Integer.max
import kotlin.Exception
import kotlin.math.absoluteValue

fun main() {
    val valves = mutableListOf<Valve>()
    File("inputs/input16.txt").forEachLine {
        valves.add(Valve(it))
    }
    val nonzeroValves = valves.filter { valve -> valve.flow > 0 }
    val valveMap = hashMapOf<String, Valve>()
    valves.forEach { valve ->
        valveMap[valve.ID] = valve
    }

    var memo = hashMapOf(BoardState(0,"AA","AA") to 0)
    var frontier = hashSetOf(BoardState(0,"AA","AA"))

    for (time in 1 .. 26) {
        println("Time: ${time}, Frontier Size: ${frontier.size}, Memo Size: ${memo.size}")
        var newFrontier = hashSetOf<BoardState>()
        var newMemo =  hashMapOf<BoardState, Int>()
        while (frontier.isNotEmpty()) {
            val current = frontier.pop()!!
            val currentState = current.state
            val humanValve = valveMap[current.humanValve]!!
            val elephantValve = valveMap[current.elephantValve]!!
            val inc = getInc(currentState, nonzeroValves)

            // If its nonzero and not already activated
            val intermediateStates = hashSetOf<BoardState>()
            if (nonzeroValves.contains(humanValve)) {
                val valveIdx = nonzeroValves.indexOf(humanValve)
                if (!isOn(currentState, valveIdx)) {
                    intermediateStates.add(BoardState(switchOn(current.state, valveIdx), current.humanValve, current.elephantValve))
                }
            }
            humanValve.adj.forEach {
                adjID ->
                intermediateStates.add(BoardState(current.state, adjID, current.elephantValve))
            }

            val finalStates = hashSetOf<BoardState>()
            intermediateStates.forEach { intermediateState ->
                if (nonzeroValves.contains(elephantValve)) {
                    val valveIdx = nonzeroValves.indexOf(elephantValve)
                    if (!isOn(intermediateState.state, valveIdx)) {
                        finalStates.add(BoardState(switchOn(intermediateState.state, valveIdx), intermediateState.humanValve, intermediateState.elephantValve))
                    }
                }
                elephantValve.adj.forEach {
                        adjID ->
                    finalStates.add(BoardState(intermediateState.state, intermediateState.humanValve, adjID))
                }
            }

            finalStates.forEach {finalState ->
                newFrontier.add(finalState)
                newMemo[finalState] = max(memo[current]!! + inc, newMemo.getOrDefault(finalState, 0))
            }
        }

        memo = HashMap(newMemo)
        frontier = newFrontier.toList().sortedBy { boardState -> newMemo[boardState] }.takeLast(100000).toHashSet() // Cheese
    }

    println(memo.values.max())

}

data class BoardState(
   val state: Int,
   val humanValve: String,
   val elephantValve: String,
)

fun isOn(state: Int, index: Int): Boolean {
    return state and (1 shl index) > 0
}
fun switchOn(state: Int, index: Int): Int {
    return state or (1 shl index)
}

fun getInc(state: Int, nonzeroValves: List<Valve>): Int {
    var totalFlow = 0
    for (i in 0 .. 14) {
        if ((state and (1 shl i)) > 0) {
            totalFlow += nonzeroValves[i].flow
        }
    }
    return totalFlow
}

fun <T> MutableSet<T>.pop(): T? = this.first().also{this.remove(it)}

class Valve (
    input: String
) {
    val flow: Int
    val ID: String
    val adj: List<String>

    init {
        ID = input.substring(6,8)
        flow = Integer.parseInt(input.substring(23, input.indexOf(';')))
        adj = input.substring(input.indexOf("tunnel") + 22).split(',').map { it.trim() }
    }
}

 
