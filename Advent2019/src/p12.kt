import java.io.File
import kotlin.math.absoluteValue

data class Moon(
    var x: Int,
    var y: Int,
    var z: Int,
    var vx: Int = 0,
    var vy: Int = 0,
    var vz: Int = 0,
) {
    private val xBuffer = mutableListOf<Int>()
    private val yBuffer = mutableListOf<Int>()
    private val zBuffer = mutableListOf<Int>()

    private val potentialEnergy: Int
        get() = x.absoluteValue + y.absoluteValue + z.absoluteValue
    private val kineticEnergy: Int
        get() = vx.absoluteValue + vy.absoluteValue + vz.absoluteValue
    val totalEnergy: Int
        get() = potentialEnergy * kineticEnergy

    fun updateVelocityAndMove() {
        vx += xBuffer.sum()
        vy += yBuffer.sum()
        vz += zBuffer.sum()
        xBuffer.clear()
        yBuffer.clear()
        zBuffer.clear()

        x += vx
        y += vy
        z += vz
    }

    fun converge(other: Moon) {
        if (x < other.x) {
            xBuffer.add(1)
        } else if (x > other.x) {
            xBuffer.add(-1)
        }

        if (y < other.y) {
            yBuffer.add(1)
        } else if (y > other.y) {
            yBuffer.add(-1)
        }

        if (z < other.z) {
            zBuffer.add(1)
        } else if (z > other.z) {
            zBuffer.add(-1)
        }
    }
}

fun main() {
    val moonsA = getMoons()
    repeat(1000) {
        cycleMoons(moonsA)
    }
    println("Part A: ${moonsA.sumOf { it.totalEnergy }}")

    val lcmArgs = mutableListOf<Long>()
    for (xyzSelector in 0..2) {
        val moons = getMoons()
        var iterationCounter = 0
        val indexReps = mutableListOf(getSingleStrRep(xyzSelector, moons))
        val indexRepsSet = mutableSetOf(getSingleStrRep(xyzSelector, moons))
        infinite@ while (true) {
            cycleMoons(moons)
            iterationCounter += 1
            val rep = getSingleStrRep(xyzSelector, moons)
            if (rep in indexRepsSet) {
                println("$xyzSelector, ${indexReps.indexOf(rep)}, $iterationCounter")
                /**
                 * Here we notice that these are all cycles of length iterationCounter that return to the start
                 * hence the answer is there LCM
                 */
                lcmArgs.add(iterationCounter.toLong())
                break@infinite
            }
            indexRepsSet.add(rep)
            indexReps.add(rep)
        }
    }

    println("Part B: ${lcm(lcmArgs[0], lcmArgs[1], lcmArgs[2])}")
}

fun getSingleStrRep(idx: Int, moons: List<Moon>): String {
    return moons.map {
        when (idx) {
            0 -> Pair(it.x, it.vx).toString()
            1 -> Pair(it.y, it.vy).toString()
            2 -> Pair(it.z, it.vz).toString()
            else -> throw Exception("improper idx")
        }
    }.toString()
}

fun getMoons(): List<Moon> {
    return File("inputs/input12.txt").readLines().map { line ->
        val split = line.filter { ch -> ch.isDigit() || ch in listOf(',', '-') }.split(",").map {
            it.toInt()
        }
        Moon(split[0], split[1], split[2])
    }
}

fun cycleMoons(moons: List<Moon>) {
    for (m1 in moons) {
        for (m2 in moons) {
            if (m1 != m2) {
                m1.converge(m2)
            }
        }
    }
    for (moon in moons) {
        moon.updateVelocityAndMove()
    }
}


// Courtesy of https://stackoverflow.com/questions/147515/least-common-multiple-for-3-or-more-numbers
fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}

fun lcm(a: Long, b: Long, c: Long): Long {
    return lcm(a, lcm(b, c))
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) {
        return a
    }
    return gcd(b, a % b)
}