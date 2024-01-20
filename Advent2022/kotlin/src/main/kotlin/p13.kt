import java.io.File
import kotlin.Exception

fun main() {
    val lines = File("inputs/input13.txt").readLines()
    var counter = 0
    val allBounds = mutableListOf<Bounds>()
    for (i in 0 .. lines.size / 3) {
        val lineA = Line(lines[3*i])
        val lineB = Line(lines[3*i + 1])
        val a = lineA.root
        val b = lineB.root
        allBounds.add(a)
        allBounds.add(b)
        if (a.compare(b) == 1) {
            counter += i+1
        }
    }
    val dividerA = Line("[[2]]").root
    val dividerB = Line("[[6]]").root
    allBounds.add(dividerA)
    allBounds.add(dividerB)
    allBounds.sortWith(Comparator<Bounds> { a, b ->
        a.compare(b)
    })
    allBounds.reverse()
    println(counter)
    println((allBounds.indexOf(dividerA) + 1) * (allBounds.indexOf(dividerB) + 1))
}

class Line(input: String) {
    val startToEnd: HashMap<Int, Int> = HashMap()
    val root: Bounds
    init {
        val stack = mutableListOf<Int>()
        input.indices.forEach { index ->
            val ch = input[index]
            if (ch == '[') {
                stack.add(index)
            } else if (ch == ']') {
                val open = stack.removeLast()
                startToEnd[open] = index
            }
        }
        root = Bounds(input, this, 1, input.length-2, false, -100000)
    }
}

// Bounds are inclusive
class Bounds(input: String, private val parent: Line, low: Int, high: Int, private val isInt: Boolean, private val intVal: Int) {

    private val items = mutableListOf<Bounds>()

    init {
        var counter = low
        var current = ""

        while (!isInt && counter <= high) {
            when (val ch = input[counter]) {
                '[' -> {
                    items.add(Bounds(input, parent, counter + 1, parent.startToEnd[counter]!! - 1, false, -100000))
                    counter = parent.startToEnd[counter]!!
                }
                ',' -> {
                    if (current.isNotEmpty()) { // after []
                        items.add(Bounds(input, parent, INVALID, INVALID, true, Integer.parseInt(current.trim())))
                        current = ""
                    }
                }
                else -> {
                    current += ch
                }
            }
            counter += 1
        }
        if (current.isNotEmpty()) { // End of input
            try {
                items.add(Bounds(input, parent, INVALID, INVALID, true, Integer.parseInt(current.trim())))
            } catch (_: Exception) {}
        }
    }

    fun compare(other: Bounds): Int {
        if (isInt && other.isInt) {
            return if (intVal < other.intVal) {
                1
            } else if (intVal == other.intVal) {
                0
            } else {
                -1
            }
        } else if (!isInt && !other.isInt) {
            var a = 0
            var b = 0
            while (a < this.items.size && b < other.items.size) {
                val evalResult = items[a].compare(other.items[b])
                if (evalResult != 0) {
                    return evalResult
                }
                a++
                b++
            }
            return if (a == this.items.size && b == other.items.size) {
                0
            } else if (a == this.items.size) {
                1
            } else {
                -1
            }
        }
        else {
            return if (isInt) {
                val fakeString = "[${intVal}]"
                Bounds(fakeString, parent, 1, fakeString.length - 2, false, INVALID).compare(other)
            } else {
                val fakeString = "[${other.intVal}]"
                this.compare(Bounds(fakeString, parent, 1, fakeString.length - 2, false, INVALID))
            }
        }
    }

    override fun toString(): String {
        return if (isInt) {
            intVal.toString()
        } else {
            items.toString()
        }
    }

    companion object {
        private const val INVALID = -10000
    }

}
