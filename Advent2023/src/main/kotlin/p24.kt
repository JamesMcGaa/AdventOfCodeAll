import java.io.File
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import java.lang.Exception

const val LOWER_BOUNDS = 200000000000000L
const val UPPER_BOUNDS = 400000000000000L

data class Hail(
    val px: Long,
    val py: Long,
    val pz: Long,
    val a: Long,
    val b: Long,
    val c: Long,
) {
    val x_c = -1 * b
    val y_c = a
    val v_c = a * py - b * px

    fun z(t: Double): Double {
        return pz + t*c
    }

    fun t(x: Double): Double {
        return (x - px).toDouble() / a.toDouble()
    }
}


fun main() {
    val hails = mutableListOf<Hail>()
    File("inputs/input24.txt").forEachLine {
        hails.add(
            Hail(
                it.split(" ").filter { it.isNotBlank() }[0].removeSuffix(",").toLong(),
                it.split(" ").filter { it.isNotBlank() }[1].removeSuffix(",").toLong(),
                it.split(" ").filter { it.isNotBlank() }[2].removeSuffix(",").toLong(),
                it.split(" ").filter { it.isNotBlank() }[4].removeSuffix(",").toLong(),
                it.split(" ").filter { it.isNotBlank() }[5].removeSuffix(",").toLong(),
                it.split(" ").filter { it.isNotBlank() }[6].removeSuffix(",").toLong(),
            )
        )
    }


    var counterA = 0
    for (hailA in hails) {
        for (hailB in hails) {
            if (hailA != hailB && hailA.hashCode() <= hailB.hashCode()) {
                val a = mk.ndarray(
                    mk[
                        mk[hailA.x_c, hailA.y_c],
                        mk[hailB.x_c, hailB.y_c]
                    ]
                )
                val b = mk.ndarray(mk[hailA.v_c, hailB.v_c])

                try {
                    val sol = mk.linalg.solve(a, b)
                    val tA = hailA.t(sol.get(0))
                    val tB = hailB.t(sol.get(0))

                    if (tA >= 0 && tB >= 0 && sol.get(0) >= LOWER_BOUNDS && sol.get(0) <= UPPER_BOUNDS
                        && sol.get(1) >= LOWER_BOUNDS && sol.get(1) <= UPPER_BOUNDS
                    ) {
                        counterA += 1
                    }
                } catch (e: Exception) {
                }
            }
        }
    }
    println(counterA)

    var validsA: Set<Long>? = null
    var validsB: Set<Long>? = null
    var validsC: Set<Long>? = null
    for (hail in hails) {
        for (other in hails) {
            if (hail != other && hail.a == other.a) {
                val valids = mutableSetOf<Long>()
                for (vel in -100000L..100000L) {
                    if (vel == hail.a) {
                        if (hail.px == other.px) {
                            valids.add(vel)
                        }
                        continue
                    }
                    if ((other.px - hail.px) % (vel - hail.a) == 0L) {
                        valids.add(vel)
                    }
                }
                validsA = (validsA ?: valids) intersect valids
            }

            if (hail != other && hail.b == other.b) {
                val valids = mutableSetOf<Long>()
                for (vel in -1000L..1000L) {
                    if (vel == hail.b) {
                        if (hail.py == other.py) {
                            valids.add(vel)
                        }
                        continue
                    }
                    if ((hail.py - other.py) % (vel - hail.b) == 0L) {
                        valids.add(vel)
                    }
                }
                validsB = (validsB ?: valids) intersect valids
            }

            if (hail != other && hail.c == other.c) {
                val valids = mutableSetOf<Long>()
                for (vel in -1000L..1000L) {
                    if (vel == hail.c) {
                        if (hail.pz == other.pz) {
                            valids.add(vel)
                        }
                        continue
                    }
                    if ((hail.pz - other.pz) % (vel - hail.c) == 0L) {
                        valids.add(vel)
                    }
                }
                validsC = (validsC ?: valids) intersect valids
            }
        }
    }

    val trueA = validsA!!.first()
    val trueB = validsB!!.first()
    val trueC = validsC!!.first()


    val shifted = hails.map { it.copy(a = it.a - trueA, b = it.b - trueB, c = it.c - trueC) }.toMutableList()
    val hailA = shifted.removeLast()
    val hailB = shifted.removeLast()
    val a = mk.ndarray(
        mk[
            mk[hailA.x_c, hailA.y_c],
                    mk[hailB.x_c, hailB.y_c]
        ]
    )
    val b = mk.ndarray(mk[hailA.v_c, hailB.v_c])
    val sol = mk.linalg.solve(a, b)
    val tB = hailB.t(sol.get(0))

    println(hailB.z(tB) + sol.get(0) + sol.get(1))
}

/**
 * Another tricky one. This numpy module in kotlin is cool though. Needed to note the trick that speeds are integer
 *  and that the additional velocity of the stone must evenly divide the gap between equal moving speeds. I needed help for this one
 *
 *  - Lots of clever observations and tricks here
 *
 *  - One member noted we could regard us standing still and the hailstones all moving at exactly v_i - V_SOLUTION and looking for 1 intersection
 *          parsing pairwise to see if they intersect gradually and looking for all velocities
 *
 *  - Others just used standard algebra tricks to reduce p2 to something more managable
 */