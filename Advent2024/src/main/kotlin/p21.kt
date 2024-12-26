package main.kotlin

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.min

fun main() {
    val numpad = Utils.readAsGrid<Char>("inputs/input21_numpad.txt")
    val dirpad = Utils.readAsGrid<Char>("inputs/input21_dirpad.txt")
    Utils.printGrid(numpad)
    Utils.printGrid(dirpad)
    Utils.printGrid(dirpad)
    Utils.printGrid(dirpad)

    val numpadDistMemo = mutableMapOf<Pair<Char, Char>, List<Char>>()
    fun numpadDist(start: Char, end: Char): List<Char> {
        if (Pair(start, end) in numpadDistMemo) {
            return numpadDistMemo[Pair(start, end)]!!
        }
        val startCoord = Utils.findCoord(start, numpad)
        val endCoord = Utils.findCoord(end, numpad)
        val ret = mutableListOf<Char>()

        val dontStartVertical = startCoord.y == 0

        val horizontalAmount = endCoord.y - startCoord.y
        val verticalAmount = endCoord.x - startCoord.x
        if (dontStartVertical) {
            repeat(horizontalAmount.absoluteValue) {
                ret.add(if (horizontalAmount > 0) '>' else '<')
            }
            repeat(verticalAmount.absoluteValue) {
                ret.add(if (verticalAmount > 0) 'v' else '^')
            }
        } else {
            repeat(verticalAmount.absoluteValue) {
                ret.add(if (verticalAmount > 0) 'v' else '^')
            }
            repeat(horizontalAmount.absoluteValue) {
                ret.add(if (horizontalAmount > 0) '>' else '<')
            }
        }
        ret.add('A')
        numpadDistMemo[Pair(start, end)] = ret
        return ret
    }

    // Could probably merge this with above
    val dirDistMemo = mutableMapOf<Pair<Char, Char>, List<Char>>()
    fun dirpadDist(start: Char, end: Char, upFirst: Boolean): List<Char> {
        if (Pair(start, end) in dirDistMemo) {
            return dirDistMemo[Pair(start, end)]!!
        }
        val startCoord = Utils.findCoord(start, dirpad)
        val endCoord = Utils.findCoord(end, dirpad)
        val ret = mutableListOf<Char>()

        val dontStartVertical = startCoord.y == 0
        val dontStartHorizontal = startCoord.x == 0 && endCoord.y == 0

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
            if (upFirst) {
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
            }
        }
        ret.add('A')
        numpadDistMemo[Pair(start, end)] = ret

        return ret
    }


    fun expand(toGo: Int, inp: List<Char>): Int {
        if (toGo == 0) {
            return inp.size
        }
        var current = inp.last()
        val recurseUp = mutableListOf<Char>()
        val recurseHorz = mutableListOf<Char>()
        for (ch in inp) {
            recurseUp.addAll(dirpadDist(current, ch, true))
            recurseHorz.addAll(dirpadDist(current, ch, false))
            current = ch
        }
        return min(expand(toGo - 1, recurseUp), expand(toGo - 1, recurseHorz))
    }

    fun runForInput(input: String): Int {
        var current = 'A'
        var ret = mutableListOf<Char>()
        for (ch in input) {
            ret.addAll(numpadDist(current, ch))
            current = ch
        }
        return expand(2, ret)
    }


    val partA = File("inputs/input21.txt").readLines().sumOf {
//        println(runForInput(it))
//        println(it.removeSuffix("A").toInt())
//        println("-----------")
        runForInput(it) * it.removeSuffix("A").toInt()
    }

    println(partA)
}

// 137420 too high