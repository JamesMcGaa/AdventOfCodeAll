package main.kotlin

import Utils.Coord
import Utils.iterprint
import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.min


fun main() {
    fun generalizedDist(
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
            ret.sortBy { ch ->
                when (ch) {
                    '<' -> 0
                    '^' -> 1
                    'v' -> 2
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


    fun expand(toGo: Int, inp: List<Char>): Int {
        if (toGo == 0) {
            return inp.size
        }
        var current = inp.last()
        val recurse = mutableListOf<Char>()
        for (ch in inp) {
            recurse.addAll(generalizedDist(dirpad, dirDistMemo, current, ch, isNumpad = false))
            current = ch
        }
        return expand(toGo - 1, recurse)
    }

    fun runForInput(input: String): Int {
        var current = 'A'
        var ret = mutableListOf<Char>()
        for (ch in input) {
            ret.addAll(generalizedDist(numpad, numpadDistMemo, current, ch, isNumpad = true))
            current = ch
        }
        return expand(2, ret)
    }


    val partA = File("inputs/input21.txt").readLines().sumOf {
        val res = runForInput(it)
        println(res)
        res * it.removeSuffix("A").toInt()
    }
    println(partA)
}