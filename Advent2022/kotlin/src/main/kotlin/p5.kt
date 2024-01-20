import java.io.File

fun main(args: Array<String>) {

    val lines = File("inputs/input5.txt").readLines()
    val stacks = Stacks(lines.subList(0, 8))
    lines.subList(10, lines.size).forEach { line ->
//        stacks.processMove(Move(line))
        stacks.processFullStackMove(Move(line))
    }
    println(stacks.printLast())

}

class Move(move: String) {
    val from: Int
    val to: Int
    val count: Int
    init {
        from = Integer.parseInt(move.split(" ")[3]) - 1
        to = Integer.parseInt(move.split(" ")[5]) - 1
        count = Integer.parseInt(move.split(" ")[1])
    }
}

class Stacks(initialState: List<String>) {
    val stacks: MutableList<MutableList<Char>> = mutableListOf()

    init {
        for (i in 0..8){
            stacks.add(mutableListOf())
        }

        initialState.forEach {
            line ->
            for (i in 0..8) {
                if (line[4*i + 1] != ' ') {
                    stacks[i].add(line[4*i + 1])
                }
            }
        }

        stacks.forEach { stack ->
            stack.reverse()
        }
    }

    fun processMove(move: Move) {
        for (i in 0 until move.count) {
            stacks[move.to].add(stacks[move.from].removeLast())
        }
    }

    fun processFullStackMove(move: Move) {
        val temp = mutableListOf<Char>()
        for (i in 0 until move.count) {
            temp.add(stacks[move.from].removeLast())
        }
        temp.reverse()
        stacks[move.to].addAll(temp)
    }
    fun printLast(): String{
        var lastString = ""
        stacks.forEach { stack ->
            lastString += stack.last()
        }
        return lastString
    }

}

