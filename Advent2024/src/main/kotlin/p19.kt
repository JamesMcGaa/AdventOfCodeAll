package main.kotlin

import java.io.File

fun main() {
    val input = File("inputs/input19.txt").readLines()
    val tokens = input.first().split(",").map { it.trim() }
    val words = input.subList(2, input.size)

    var memo = mutableMapOf<String, Long>() // Important to use ,Long...
    fun canMake(left: String): Long {
        if (left.isEmpty()) {
            return 1L
        }
        if (left in memo) {
            return memo[left]!!
        } else {
            var ret = 0L
            for (token in tokens) {
                if (left.startsWith(token)) {
                    ret += canMake(left.removePrefix(token))
                }
            }
            memo[left] = ret
            return ret
        }
    }

    words.forEach { canMake(it) }
    val partA = words.count { memo.getOrDefault(it, 0) > 0 }
    val partB = words.sumOf { memo.getOrDefault(it, 0) }
    println("Part A: $partA")
    println("Part B: $partB")
}