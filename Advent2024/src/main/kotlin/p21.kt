package main.kotlin

import Utils
import Utils.Coord
import java.io.File
import kotlin.math.absoluteValue


fun main() {
    fun generalizedShortestPath(
        numOrDistPad: MutableMap<Coord, Char>,
        memo: MutableMap<Pair<Char, Char>, List<Char>>,
        start: Char,
        end: Char,
        isNumpad: Boolean
    ): List<Char> {
        if (Pair(start, end) in memo) {
            return memo[Pair(start, end)]!!
        }
        val startCoord = Utils.findCoord(start, numOrDistPad)
        val endCoord = Utils.findCoord(end, numOrDistPad)
        var ret = mutableListOf<Char>()

        val dontStartVertical = if (isNumpad) {
            startCoord.y == 0 && endCoord.x == 3
        } else {
            startCoord.y == 0 && endCoord.x == 0
        }

        val dontStartHorizontal = if (isNumpad) {
            startCoord.x == 3 && endCoord.y == 0
        } else {
            startCoord.x == 0 && endCoord.y == 0
        }


        val horizontalAmount = endCoord.y - startCoord.y
        val verticalAmount = endCoord.x - startCoord.x

        if (dontStartVertical) {
            repeat(horizontalAmount.absoluteValue) {
                ret.add(if (horizontalAmount > 0) '>' else '<')
            }
            repeat(verticalAmount.absoluteValue) {
                ret.add(if (verticalAmount > 0) 'v' else '^')
            }
        } else if (dontStartHorizontal) {
            repeat(verticalAmount.absoluteValue) {
                ret.add(if (verticalAmount > 0) 'v' else '^')
            }
            repeat(horizontalAmount.absoluteValue) {
                ret.add(if (horizontalAmount > 0) '>' else '<')
            }
        } else {
            repeat(horizontalAmount.absoluteValue) {
                ret.add(if (horizontalAmount > 0) '>' else '<')
            }
            repeat(verticalAmount.absoluteValue) {
                ret.add(if (verticalAmount > 0) 'v' else '^')
            }
            // In our DirPad, we want to do the < then v then ^ == > so we end up closest to A at the end
            ret.sortBy { ch ->
                when (ch) {
                    '<' -> 0
                    'v' -> 1
                    '^' -> 2
                    '>' -> 3
                    else -> throw Exception()
                }
            }
            val finalRet = mutableListOf<Char>()
            var current = startCoord
            outer@ while (ret.isNotEmpty()) {
                for (ch in listOf('<', '^', 'v', '>')) {
                    if (ch in ret) {
                        val proj = when (ch) {
                            '<' -> current.left
                            '^' -> current.up
                            'v' -> current.down
                            '>' -> current.right
                            else -> throw Exception()
                        }
                        if (numOrDistPad[proj] != ' ') {
                            current = proj
                            ret.remove(ch)
                            finalRet.add(ch)
                            continue@outer
                        }
                    }
                }
                throw Exception("Improper direction list")
            }
            ret = finalRet
        }
        ret.add('A')
        memo[Pair(start, end)] = ret
        return ret
    }

    val numpad = Utils.readAsGrid<Char>("inputs/input21_numpad.txt")
    val dirpad = Utils.readAsGrid<Char>("inputs/input21_dirpad.txt")
    val numpadDistMemo = mutableMapOf<Pair<Char, Char>, List<Char>>()
    val dirDistMemo = mutableMapOf<Pair<Char, Char>, List<Char>>()


    /**
     * Legacy - we can't afford to keep all these in memory
     */
    fun expand(toGo: Int, inp: List<Char>): Int {
        if (toGo == 0) {
            return inp.size
        }
        var current = inp.last()
        val recurse = mutableListOf<Char>()
        for (ch in inp) {
            recurse.addAll(generalizedShortestPath(dirpad, dirDistMemo, current, ch, isNumpad = false))
            current = ch
        }
        return expand(toGo - 1, recurse)
    }

    val expand2Memo = mutableMapOf<Pair<Int, List<Char>>, Long>()
    fun expand2(toGo: Int, inp: List<Char>): Long {
        if (toGo == 0) {
            return inp.size.toLong()
        }
        val key = Pair(toGo, inp)
        if (key in expand2Memo) {
            return expand2Memo[key]!!
        }
        val shifted = listOf('A') + inp
        var res = 0L
        for (idx in inp.indices) {
            val shortestPath = generalizedShortestPath(dirpad, dirDistMemo, shifted[idx], inp[idx], isNumpad = false)
            res += expand2(toGo-1, shortestPath)
        }
        expand2Memo[key] = res
        return res
    }

    fun runForInput(input: String, depth: Int): Long {
        var current = 'A'
        var ret = mutableListOf<Char>()
        for (ch in input) {
            ret.addAll(generalizedShortestPath(numpad, numpadDistMemo, current, ch, isNumpad = true))
            current = ch
        }
        return expand2(depth, ret)
    }


    val partA = File("inputs/input21.txt").readLines().sumOf {
        val res = runForInput(it, 2)
        res * it.removeSuffix("A").toInt()
    }
    val partB = File("inputs/input21.txt").readLines().sumOf {
        val res = runForInput(it, 25)
        res * it.removeSuffix("A").toInt()
    }
    println("Part A: $partA")
    println("Part A: $partB")
}