package main.kotlin

import java.io.File
import kotlin.math.max

fun main() {
    val partA = File("inputs/input22.txt").readLines().map { it.toLong() }.sumOf {
        var start = it
        repeat(2000) {
            start = evolve(start)
        }
        start
    }
    println("Part A: $partA")

    val results = File("inputs/input22.txt").readLines().map {
        val ret = mutableMapOf<List<Int>, Int>()
        var current = it.toLong()
        val prices = mutableListOf<Int>((current % 10).toInt())
        val diffs = mutableListOf<Int>()
        repeat(2000) {
            current = evolve(current)
            prices.add((current % 10).toInt())
            diffs.add(prices[prices.size - 1] - prices[prices.size - 2])
            if (diffs.size >= 4) { // Not quite a sliding window, but they key is to do this during input generation
                val trailing = diffs.slice(diffs.indices.last - 3..diffs.indices.last)
                if (trailing !in ret) {
                    ret[trailing] = prices.last()
                }
            }
        }
        ret
    }
    println("Prices and diffs computed...")

    var bestSoFar = 0
    for (a in -9..9) {
        for (b in -9..9) {
            for (c in -9..9) {
                for (d in -9..9) {
                    val matches = results.sumOf { result -> result.getOrDefault(listOf(a, b, c, d), 0) }
                    bestSoFar = max(bestSoFar, matches)
                }
            }
        }
    }
    println(bestSoFar)
}

fun evolve(start: Long): Long {
    var secretNumber = start
    val v1 = secretNumber * 64L
    secretNumber = secretNumber xor v1
    secretNumber = secretNumber % 16777216
    val v2 = secretNumber / 32L
    secretNumber = secretNumber xor v2
    secretNumber = secretNumber % 16777216
    val v3 = secretNumber * 2048L
    secretNumber = secretNumber xor v3
    secretNumber = secretNumber % 16777216
    return secretNumber
}