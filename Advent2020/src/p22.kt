import java.io.File

const val RIGHT = 0
const val DOWN = 1
const val LEFT = 2
const val UP = 3

const val RIGHT_EDGE = 49
const val LEFT_EDGE = 0
const val TOP_EDGE = 0
const val BOTTOM_EDGE = 49

fun main() {
    val lines = File("src/input22.txt").readLines()

    val height = lines.size - 2
    val width = lines.subList(0, height).maxBy { it.length }!!.length
    val firstNonzeroSecondCoord = lines[0].indexOfFirst { ch -> ch != ' ' }
    val startLoc = Pair(0, firstNonzeroSecondCoord)
    println("${height}, ${width}, $firstNonzeroSecondCoord")

    val board = HashMap<Pair<Int, Int>, Char>()
    for (i in lines.indices) {
        for (j in lines[i].indices) {
            board[Pair(i, j)] = lines[i][j]
        }
    }

    val sectors = HashMap<Triple<Int, Int, Int>, Char>()
    val offsets =
        mutableListOf(null, Pair(0, 50), Pair(0, 100), Pair(50, 50), Pair(100, 0), Pair(100, 50), Pair(150, 0))
    for (i in 0..49) {
        for (j in 0..49) {
            for (sector in 1..6) {
                sectors[Triple(i, j, sector)] = lines[i + offsets[sector]!!.first][j + offsets[sector]!!.second]
            }
        }
    }

    val operations = lines[lines.size - 1]
    val parsedOps = mutableListOf<String>()
    var curr = ""
    for (ch in operations) {
        when (ch) {
            'L', 'R' -> {
                if (curr.isNotEmpty()) {
                    parsedOps.add(curr)
                    curr = ""
                }
                parsedOps.add(ch.toString())
            }
            else -> curr += ch
        }
    }
    if (curr.isNotEmpty()) {
        parsedOps.add(curr)
    }
    println(parsedOps)

    val character = Character(board, width, height, startLoc)
    for (op in parsedOps) {
        character.handle(op)
    }
    println(1000 * (character.loc.first + 1) + 4 * (character.loc.second + 1) + character.facing)

    val characterB = CharacterB(sectors)
    for (op in parsedOps) {
        characterB.handle(op)
    }
    println(1000 * (characterB.loc.first + 1 + offsets[characterB.loc.third]!!.first) + 4 * (characterB.loc.second + 1 + offsets[characterB.loc.third]!!.second) + characterB.facing)
}

class CharacterB(val board: HashMap<Triple<Int, Int, Int>, Char>) {
    var loc = Triple(0, 0, 1)
    var facing = RIGHT

    fun handle(command: String) {
        print("$loc, $facing -> ")
        when (command) {
            "L" -> facing = Math.floorMod(facing - 1, 4)
            "R" -> facing = Math.floorMod(facing + 1, 4)
            else -> moveForward(Integer.parseInt(command))
        }
        print("$loc, $facing \n")
    }

    fun normalize(proposed: Triple<Int, Int, Int>): Pair<Triple<Int, Int, Int>, Int>? {
        var copied = proposed.copy()
        var proposedDirection: Int = facing
        when {
            proposed.first == -1 -> { // UP
                when (proposed.third) {
                    1 -> {
                        copied = copied.copy(first = copied.second, second = LEFT_EDGE, third = 6)
                        proposedDirection = RIGHT
                    }
                    2 -> {
                        copied = copied.copy(first = BOTTOM_EDGE, second = copied.second, third = 6)
                        proposedDirection = UP
                    }
                    3 -> {
                        copied = copied.copy(first = BOTTOM_EDGE, second = copied.second, third = 1)
                        proposedDirection = UP
                    }
                    4 -> {
                        copied = copied.copy(first = copied.second, second = LEFT_EDGE, third = 3)
                        proposedDirection = RIGHT
                    }
                    5 -> {
                        copied = copied.copy(first = BOTTOM_EDGE, second = copied.second, third = 3)
                        proposedDirection = UP
                    }
                    6 -> {
                        copied = copied.copy(first = BOTTOM_EDGE, second = copied.second, third = 4)
                        proposedDirection = UP
                    }
                    else -> throw Exception()
                }
            }

            proposed.first == 50 -> { // DOWN
                when (proposed.third) {
                    1 -> {
                        copied = copied.copy(first = TOP_EDGE, second = copied.second, third = 3)
                        proposedDirection = DOWN
                    }
                    2 -> {
                        copied = copied.copy(first = copied.second, second = RIGHT_EDGE, third = 3)
                        proposedDirection = LEFT
                    }
                    3 -> {
                        copied = copied.copy(first = TOP_EDGE, second = copied.second, third = 5)
                        proposedDirection = DOWN
                    }
                    4 -> {
                        copied = copied.copy(first = TOP_EDGE, second = copied.second, third = 6)
                        proposedDirection = DOWN
                    }
                    5 -> {
                        copied = copied.copy(first = copied.second, second = RIGHT_EDGE, third = 6)
                        proposedDirection = LEFT
                    }
                    6 -> {
                        copied = copied.copy(first = TOP_EDGE, second = copied.second, third = 2)
                        proposedDirection = DOWN
                    }
                    else -> throw Exception()
                }
            }

            proposed.second == -1 -> { // LEFT
                when (proposed.third) {
                    1 -> {
                        copied = copied.copy(first = 49 - copied.first, second = LEFT_EDGE, third = 4)
                        proposedDirection = RIGHT
                    }
                    2 -> {
                        copied = copied.copy(first = copied.first, second = RIGHT_EDGE, third = 1)
                        proposedDirection = LEFT
                    }
                    3 -> {
                        copied = copied.copy(first = TOP_EDGE, second = copied.first, third = 4)
                        proposedDirection = DOWN
                    }
                    4 -> {
                        copied = copied.copy(first = 49 - copied.first, second = LEFT_EDGE, third = 1)
                        proposedDirection = RIGHT
                    }
                    5 -> {
                        copied = copied.copy(first = copied.first, second = RIGHT_EDGE, third = 4)
                        proposedDirection = LEFT
                    }
                    6 -> {
                        copied = copied.copy(first = TOP_EDGE, second = copied.first, third = 1)
                        proposedDirection = DOWN
                    }
                    else -> throw Exception()
                }
            }

            proposed.second == 50 -> { // Right
                when (proposed.third) {
                    1 -> {
                        copied = copied.copy(first = copied.first, second = LEFT_EDGE, third = 2)
                        proposedDirection = RIGHT
                    }
                    2 -> {
                        copied = copied.copy(first = 49 - copied.first, second = RIGHT_EDGE, third = 5)
                        proposedDirection = LEFT
                    }
                    3 -> {
                        copied = copied.copy(first = BOTTOM_EDGE, second = copied.first, third = 2)
                        proposedDirection = UP
                    }
                    4 -> {
                        copied = copied.copy(first = copied.first, second = LEFT_EDGE, third = 5)
                        proposedDirection = RIGHT
                    }
                    5 -> {
                        copied = copied.copy(first = 49 - copied.first, second = RIGHT_EDGE, third = 2)
                        proposedDirection = LEFT
                    }
                    6 -> {
                        copied = copied.copy(first = BOTTOM_EDGE, second = copied.first, third = 5)
                        proposedDirection = UP
                    }
                    else -> throw Exception()
                }
            }
        }

        if (board[copied] == '#') {
            return null
        }
        return Pair(copied, proposedDirection)
    }


    private fun moveForward(amount: Int) {
        if (amount == 0) {
            return
        }
        val proposedCoord = when (facing) {
            RIGHT -> Triple(loc.first, loc.second + 1, loc.third)
            DOWN -> Triple(loc.first + 1, loc.second, loc.third)
            LEFT -> Triple(loc.first, loc.second - 1, loc.third)
            UP -> Triple(loc.first - 1, loc.second, loc.third)
            else -> throw Exception()
        }
        val result = normalize(proposedCoord) ?: return
        loc = result.first
        facing = result.second
        moveForward(amount - 1)
    }
}

class Character(val board: HashMap<Pair<Int, Int>, Char>, val width: Int, val height: Int, startLoc: Pair<Int, Int>) {
    var loc = startLoc
    var facing = 0

    fun handle(command: String) {
        print("$loc, $facing -> ")
        when (command) {
            "L" -> facing = Math.floorMod(facing - 1, 4)
            "R" -> facing = Math.floorMod(facing + 1, 4)
            else -> moveForward(Integer.parseInt(command))
        }
        print("$loc, $facing \n")
    }

    private fun moveForward(amount: Int) {
        if (amount == 0) {
            return
        }
        when (facing) {
            0 -> {
                var adj = Math.floorMod(loc.second + 1, width)
                if (board[Pair(loc.first, adj)] == '#') {
                    return
                }
                while (board[Pair(loc.first, adj)] == null || board[Pair(loc.first, adj)] == ' ') {
                    adj = Math.floorMod(adj + 1, width)
                }
                if (board[Pair(loc.first, adj)] == '#') {
                    return
                }
                loc = Pair(loc.first, adj)
                moveForward(amount - 1)
            }

            1 -> {
                var adj = Math.floorMod(loc.first + 1, height)
                if (board[Pair(adj, loc.second)] == '#') {
                    return
                }
                while (board[Pair(adj, loc.second)] == null || board[Pair(adj, loc.second)] == ' ') {
                    adj = Math.floorMod(adj + 1, height)
                }
                if (board[Pair(adj, loc.second)] == '#') {
                    return
                }
                loc = Pair(adj, loc.second)
                moveForward(amount - 1)
            }

            2 -> {
                var adj = Math.floorMod(loc.second - 1, width)
                if (board[Pair(loc.first, adj)] == '#') {
                    return
                }
                while (board[Pair(loc.first, adj)] == null || board[Pair(loc.first, adj)] == ' ') {
                    adj = Math.floorMod(adj - 1, width)
                }
                if (board[Pair(loc.first, adj)] == '#') {
                    return
                }
                loc = Pair(loc.first, adj)
                moveForward(amount - 1)
            }

            3 -> {
                var adj = Math.floorMod(loc.first - 1, height)
                if (board[Pair(adj, loc.second)] == '#') {
                    return
                }
                while (board[Pair(adj, loc.second)] == null || board[Pair(adj, loc.second)] == ' ') {
                    adj = Math.floorMod(adj - 1, height)
                }
                if (board[Pair(adj, loc.second)] == '#') {
                    return
                }
                loc = Pair(adj, loc.second)
                moveForward(amount - 1)
            }

            else -> throw Exception()
        }
    }
}