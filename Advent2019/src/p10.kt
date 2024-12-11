@file:Suppress("LocalVariableName")

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.max


fun main() {
    val input = File("inputs/input10.txt").readLines()
    val X = input.size
    val Y = input[0].length

    val asteroids = mutableSetOf<Coord>()
    val allPoints = mutableSetOf<Coord>()
    for (i in 0 until X) {
        for (j in 0 until Y) {
            if (input[i][j] == '#') {
                asteroids.add(Coord(i, j))
            }
            allPoints.add(Coord(i, j))
        }
    }

    var maxAsteroids = -1
    var laserStation = Coord(-1, -1)
    for (point in asteroids) {
        val visibleAsteroids = findVisibleAsteroids(allPoints, asteroids, point)
        val asteroidCount = visibleAsteroids.size
        if (asteroidCount > maxAsteroids) {
            maxAsteroids = asteroidCount
            laserStation = point
        }
        maxAsteroids = max(maxAsteroids, asteroidCount)
    }
    println("Part A: $maxAsteroids, $laserStation")

    var destroyedCounter = 0
    while (true) {
        val visibleAsteroids = findVisibleAsteroids(allPoints, asteroids, laserStation)
        val upVec = Coord(-1, 0)
        val visibleAsteroidsClockwise = visibleAsteroids.toMutableList().map {
            val pointToAsteroid = it - laserStation
            Pair(it, upVec.clockwiseAngleToVector(pointToAsteroid))
        }.sortedBy { it.second }

        visibleAsteroidsClockwise.forEach {
            asteroids.remove(it.first)
            if (++destroyedCounter == 200) {
                println("Part B: ${100 * it.first.y + it.first.x}, ${it.first}") // X in problem is y is Coord, etc
                return
            }
        }
        throw Exception("Never got to laser the 200th asteroid")
    }
}

fun findVisibleAsteroids(allPoints: Set<Coord>, asteroids: Set<Coord>, point: Coord): Set<Coord> {
    val visible = allPoints.toMutableSet()
    visible.remove(point) // Cant see self
    for (asteroid in asteroids) {
        if (asteroid in visible) {
            val pointToAsteroid = asteroid - point
            val gcd =
                gcd(pointToAsteroid.x.absoluteValue, pointToAsteroid.y.absoluteValue) // Account for negatives!
            val normalizedProjection = pointToAsteroid / gcd
            if (normalizedProjection == Coord(0, 0)) throw Exception("Improper projection")
            var next = asteroid + normalizedProjection
            while (next in allPoints) {
                visible.remove(next)
                next += normalizedProjection
            }
        }
    }
    return asteroids intersect visible
}

fun gcd(a: Int, b: Int): Int {
    if (b == 0) {
        return a
    }
    return gcd(b, a % b)
}