import java.io.File
import java.util.*
import kotlin.properties.Delegates

data class Scanner(val depth: Int, val range: Int, var current: Int = 0, var polarity: Int = 1) {
    val isCaught: Boolean
        get() = current == 0
    val severity: Int
        get() = depth * range

    fun intersects(delay: Int): Boolean {
        return (delay + depth) % (2*range - 2) == 0
    }

    fun advance() {
        if (current == range - 1 && polarity == 1) {
            polarity = -1
        }
        if (current == 0 && polarity == -1) {
            polarity = 1
        }
        current += polarity
    }
}

fun main() {
    var maxDepth by Delegates.notNull<Int>()
    val depthToScanner = mutableMapOf<Int, Scanner>()
    File("inputs/input13.txt").forEachLine {
        val depth = it.split(":")[0].trim().toInt()
        val range = it.split(":")[1].trim().toInt()
        depthToScanner[depth] = Scanner(depth, range)
        maxDepth = depth // will end up at the end
    }

    val partA = runForDepth(
        deepcopy(depthToScanner), 0, maxDepth, false
    )
    println("Part A: ${partA}")

    val ranges: IntArray = depthToScanner.values.map {
        2 * it.range - 2
    }.toIntArray()
    val lcm = lcm(*ranges)
    println("lcm: ${lcm}")

    var startDelay = 0
    while (true) {
        if (!depthToScanner.values.map {it.intersects(startDelay)}.any {it}) {
            println("Part B: ${startDelay}")
            return
        }
        startDelay++
    }
}

fun deepcopy(depthToScanner: MutableMap<Int, Scanner>): MutableMap<Int, Scanner> {
    val new = mutableMapOf<Int, Scanner>()
    for (pair in depthToScanner) {
        new[pair.key] = pair.value.copy()
    }
    return new
}

fun runForDepth(
    depthToScanner: Map<Int, Scanner>,
    startDepth: Int,
    maxDepth: Int,
    retNegIfCaught: Boolean
): Int {
    var counter = 0
    repeat(startDepth) {
        depthToScanner.values.forEach { it.advance() }
    }

    for (depth in 0..maxDepth) {
        if (depthToScanner[depth]?.isCaught == true) {
            if (retNegIfCaught) return -1
            counter += depthToScanner[depth]!!.severity
        }
        depthToScanner.values.forEach { it.advance() }
    }
    return counter

}

// From Stack Overflow
fun gcd(x: Int, y: Int): Int {
    return if (y == 0) x else gcd(y, x % y)
}

fun lcm(vararg numbers: Int): Int {
    return Arrays.stream(numbers).reduce(
        1
    ) { x: Int, y: Int ->
        x * (y / gcd(
            x,
            y
        ))
    }
}
