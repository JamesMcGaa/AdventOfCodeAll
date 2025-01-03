@file:Suppress("LocalVariableName")

package main.kotlin

import Utils.Coord
import java.io.File

const val R = 7
const val C = 5

fun main() {
    val top = mutableListOf<List<Int>>()
    val down = mutableListOf<List<Int>>()
    val N = (File("inputs/input25.txt").readLines().size + 1) / (R + 1)
    for (chunk in 0 until N) {
        val grid = Utils.readAsGrid("inputs/input25.txt", (R + 1) * chunk until (R + 1) * chunk + R) { it }
        val isTop = (0 until C).map { grid[Coord(0, it)] == '#' }.all { it }
        val elevations = (0 until C).map { c ->
            (0 until R).count { r -> grid[Coord(r, c)] == '#' } - 1
        }
        val target = if (isTop) top else down
        target.add(elevations)
    }

    var count = 0L
    top.forEach { t ->
        down.forEach { d ->
            val combined = mutableListOf<Int>()
            for (i in t.indices) {
                combined.add(t[i] + d[i])
            }
            if (combined.max() <= R - 2) {
                count += 1
            }
        }
    }

    println("Part A (only): $count")
}

