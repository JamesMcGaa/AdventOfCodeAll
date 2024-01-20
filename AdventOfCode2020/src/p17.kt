import java.io.File

fun main() {
    var final = 0L
    val air = File("src/input17.txt").readLines()[0]
    val tower = Tower(air)
    val seen = HashMap<Triple<Int, Int, Int>, Pair<Int, Int>>() //Map of (board hash, steam, rocks) to (counter, height)
    var counter = 1
    var PERIOD: Long? = null
    var HEIGHT_GAIN: Long? = null
    while (true) {
        if (counter % 100000 == 0) {
            println(counter)
            println(tower.towerState.board.size)
        }
        tower.dropRock()
        val height = tower.towerState.board.maxBy { it.second }?.second ?: 0
        val towerStateKey = Triple(
            HashSet(tower.towerState.board.filter{it.second > height - 5}).hashCode(),
            tower.towerState.steamPointer,
            tower.towerState.rocksDropped
        )
        if (towerStateKey in seen) { //1862
            val oldCounter = seen[towerStateKey]!!.first
            val oldHeight = seen[towerStateKey]!!.second
            HEIGHT_GAIN = (tower.baseHeight - oldHeight).toLong()
            PERIOD = (counter - oldCounter).toLong()

            println(seen[towerStateKey])
            println("${counter}, ${tower.baseHeight}")
            println(tower.towerState)
            println(tower.baseHeight)
            println("Period: $PERIOD, HEIGHT: $HEIGHT_GAIN")
            break
        }

        seen[towerStateKey] = Pair(counter, tower.baseHeight)
        counter += 1
    }
    val TOGO = 1000000000000L - counter
    final += (TOGO / PERIOD!!) * HEIGHT_GAIN!!
    val remainder = TOGO % PERIOD
    for (i in 1..remainder) {
        tower.dropRock()
    }
    final += tower.baseHeight + tower.towerState.board.maxBy { it.second }!!.second
    println(final)
}

data class TowerState(
    var board: HashSet<Pair<Int, Int>> = HashSet<Pair<Int, Int>>(),
    var steamPointer: Int = 0,
    var rocksDropped: Int = 0
)

data class Tower(
    var air: String,
    var baseHeight: Int = 0,
    var rocks: HashSet<Pair<Int, Int>> = HashSet<Pair<Int, Int>>(),

    var towerState: TowerState = TowerState()
) {

    private fun cleanupState() {
        var maxHeight = rocks.maxBy { it.second }!!.second
        var minHeight = rocks.minBy { it.second }!!.second
        for (height in maxHeight downTo minHeight) {
            val results = mutableListOf<Boolean>()
            for (i in 1..7) {
                results.add(Pair(i, height) in towerState.board)
            }
            if (results.all { it }) {
                towerState.board = HashSet(towerState.board.filter { pair -> pair.second > height }
                    .map { pair -> Pair(pair.first, pair.second - height) })
                baseHeight += height
            }
        }

        rocks.clear()
    }

    fun dropRock() {
        generateRock()
        while (true) {
            pushRock()
            if (gravity()) {
                towerState.rocksDropped += 1
                cleanupState()
                return
            }
        }
    }

    private fun generateRock() {
        val height = towerState.board.maxBy { it.second }?.second ?: 0
        when (towerState.rocksDropped % 5) {
            0 -> {
                for (i in 0..3) {
                    rocks.add(Pair(3 + i, height + 4))
                }
            }
            1 -> {
                rocks.add(Pair(4, height + 4))
                rocks.add(Pair(3, height + 5))
                rocks.add(Pair(4, height + 5))
                rocks.add(Pair(5, height + 5))
                rocks.add(Pair(4, height + 6))
            }
            2 -> {
                rocks.add(Pair(3, height + 4))
                rocks.add(Pair(4, height + 4))
                rocks.add(Pair(5, height + 4))
                rocks.add(Pair(5, height + 5))
                rocks.add(Pair(5, height + 6))
            }
            3 -> {
                rocks.add(Pair(3, height + 4))
                rocks.add(Pair(3, height + 5))
                rocks.add(Pair(3, height + 6))
                rocks.add(Pair(3, height + 7))
            }
            4 -> {
                rocks.add(Pair(3, height + 4))
                rocks.add(Pair(3, height + 5))
                rocks.add(Pair(4, height + 4))
                rocks.add(Pair(4, height + 5))
            }
            else -> throw Exception()
        }
    }

    private fun pushRock() {
        val proposedRocks = HashSet(when (air[towerState.steamPointer]) {
            '<' -> {
                rocks.map { rock -> Pair(rock.first - 1, rock.second) }
            }
            '>' -> {
                rocks.map { rock -> Pair(rock.first + 1, rock.second) }
            }
            else -> throw Exception()
        })
        if (proposedRocks.all { it.first in 1..7 } && (proposedRocks intersect towerState.board).isEmpty()) {
            rocks = proposedRocks
        }
        towerState.steamPointer = Math.floorMod(towerState.steamPointer + 1, air.length)
    }

    private fun gravity(): Boolean {
        val proposedRocks = HashSet(
            rocks.map { rock -> Pair(rock.first, rock.second - 1) }
        )
        if (proposedRocks.any { it.second == 0 } || (proposedRocks intersect towerState.board).isNotEmpty()) {
            towerState.board.addAll(rocks)
            return true
        }
        rocks = proposedRocks
        return false
    }


}