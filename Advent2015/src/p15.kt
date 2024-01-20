import java.io.File
import java.lang.Long.max

fun main() {
    val baseCookies = mutableListOf<Cookie>()
    File("input/input15.txt").forEachLine {
        val name = it.split(":")[0]
        val params = it.split(":")[1].filter { it.isDigit() || it.isWhitespace() || it == '-' }.split(" ")
            .filter { it.isNotBlank() }
            .map { it.toLong() }
        baseCookies.add(Cookie(params[0], params[1], params[2], params[3], params[4]))
    }
    var bestSoFarA = 0L
    var bestSoFarB = 0L
    for (a in 0..100) {
        for (b in 0..100) {
            for (c in 0..100) {
                for (d in 0..100) {
                    if (a + b + c + d == 100) {
                        val superCookie = baseCookies[0] * a + baseCookies[1] * b + baseCookies[2] * c + baseCookies[3] * d
                        bestSoFarA = max(bestSoFarA, superCookie.scoreA)
                        bestSoFarB = max(bestSoFarB, superCookie.scoreB)
                    }
                }
            }

        }
    }
    println(bestSoFarA)
    println(bestSoFarB)
}

data class Cookie(
    val a: Long,
    val b: Long,
    val c: Long,
    val d: Long,
    val e: Long,
) {
    val scoreA = max(a, 0) * max(b, 0) * max(c, 0) * max(d, 0)
    val scoreB = if (e == 500L) max(a, 0) * max(b, 0) * max(c, 0) * max(d, 0) else 0L

    operator fun times(times: Int): Cookie {
        return this.copy(
            a = this.a * times,
            b = this.b * times,
            c = this.c * times,
            d = this.d * times,
            e = this.e * times
        )
    }

    operator fun plus(other: Cookie): Cookie {
        return this.copy(
            a = this.a + other.a,
            b = this.b + other.b,
            c = this.c + other.c,
            d = this.d + other.d,
            e = this.e + other.e,
        )
    }
}

/**
 * Pure brute force
 *
 * - Others used LP solvers like ortools in Python
 * - Some just hard copied the input rather than parsing
 * - One madlad did an optimizer picking various directions and assuming convex, global max hull etc
 */



