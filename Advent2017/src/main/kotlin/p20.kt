import java.io.File

class Particle(inp: List<MutableList<Int>>) {
    val pos = inp[0]
    val vel = inp[1]
    val acc = inp[2]

    fun advance() {
        for (i in 0 until 3) {
            vel[i] += acc[i]
            pos[i] += vel[i]
        }
    }
}

fun main() {
    val particles = File("inputs/input20.txt").readLines()
        .map {
            Particle(it.split("=").subList(1, 4).map {
                it.split(",").mapNotNull {
                    val filtered = it.filter { it.isDigit() || it == '-' }
                    if (filtered.isNotBlank()) filtered.toInt() else null
                }.toMutableList()
            })
        }
    val minIdx = particles.indexOf(particles.minBy { magnitudeSquared(it.acc) })
    println("Part A: $minIdx")


    /**
     * Probably could be more elegant here and narrow down based on the magnitude of pos + pairwise
     * but brute force suffices
     */
    var particleSet = particles.toMutableSet()
    repeat(100000) { // Arbitrary, but well above the highest individual value in the input
        particleSet.forEach { it.advance() }
        val coordCounts = particleSet.map { it.pos }.groupingBy { it }.eachCount()
        val collisionCoords = mutableSetOf<MutableList<Int>>()
        coordCounts.forEach { coord, count ->
            if (count >= 2) {
                collisionCoords.add(coord)
            }
        }
        particleSet = particleSet.filter {it.pos !in collisionCoords}.toMutableSet()
    }
    println("Part B: ${particleSet.size}")
}

// We can skip the sqrt at the end to avoid floating-point-equality-land
fun magnitudeSquared(arr: List<Int>): Int {
    return arr.sumOf { it * it }
}
