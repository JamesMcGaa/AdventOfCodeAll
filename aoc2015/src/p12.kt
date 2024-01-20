import java.io.File
import java.util.*

fun main() {
    var counter = 0
    File("input/input12.txt").forEachLine {
        counter += numCount(it)
    }
    println(counter)

    File("input/input12.txt").forEachLine { line ->
        val stack = mutableListOf<NestedJSON>()
        var current = NestedJSON("", mutableListOf(), false)
        stack.add(current)
        var stringBeingMade = ""
        line.forEach { ch ->
            when (ch) {
                '[', '{' -> {
                    val newNest = NestedJSON("", mutableListOf(), ch == '{')
                    current.children.add(newNest)
                    stack.add(current)
                    current = newNest
//                    stack.add(newNest)
                }

                ']', '}' -> {
                    if (stringBeingMade.isNotBlank()) {
                        current.children.add(NestedJSON(stringBeingMade, mutableListOf(), false))
                        stringBeingMade = ""
                    }

                    var popped = stack.removeLast()
//                    if (popped == current) {
//                        popped = stack.removeLast()
//                    }
                    current = popped
                }

                ',', ':' -> {
                    current.children.add(NestedJSON(stringBeingMade, mutableListOf(), false))
                    stringBeingMade = ""
                }

                else -> stringBeingMade += ch
            }
        }
        println(current)
        println(current.getValue())
    }
}

data class NestedJSON(
    var literal: String,
    var children: MutableList<NestedJSON>,
    val isBracket: Boolean
) {
    override fun toString(): String {
        if (literal.isNotBlank()) {
            return "<" + literal + ">"
        } else {
            return "[" + children.joinToString(",") + "]"
        }
    }

    fun isRed(): Boolean {
        return literal.contains("red") || (isBracket && children.any { it.literal.contains("red") })
    }

    fun getValue(): Int {
        if (isRed()) {
            return 0
        }
        return numCount(literal) + children.sumBy {it.getValue()}
    }
}

fun numCount(input: String): Int {
    var counter = 0
    var stripped = ""
    input.forEach {
        if ("-0123456789".contains(it)) {
            stripped += it
        } else {
            stripped += " "
        }
    }
    return stripped.split(" ").filter { it.isNotBlank() }.map { it.toInt() }.sum()
}

