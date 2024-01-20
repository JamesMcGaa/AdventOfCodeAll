import java.io.File
import kotlin.Exception

fun main(args: Array<String>) {
    val lines = File("inputs/input11.txt").readLines()
    val highestMonkeyIndex = 7
    val numRounds = 10000 // 20 for part a
    for (i in 0..highestMonkeyIndex) {
        val items =
            lines[7 * i + 1].split(":")[1].split(", ").map { it.strip() }.map { Integer.parseInt(it).toLong() }.toMutableList()

        val isMultiply: Boolean
        var isSelfVal = false
        var opVal: Int
        if (lines[7 * i + 2].indexOf("+") != -1) {
            isMultiply = false
            opVal = Integer.parseInt(lines[7 * i + 2].split("+")[1].strip())
        } else {
            isMultiply = true
            try {
                opVal = Integer.parseInt(lines[7 * i + 2].split("*")[1].strip())
            } catch (e: Exception) {
                opVal = -100000000
                isSelfVal = true
            }
        }

        val divisible = Integer.parseInt(lines[7 * i + 3].split(" ").last().strip())
        val trueMonkey = Integer.parseInt(lines[7 * i + 4].split(" ").last().strip())
        val falseMonkey = Integer.parseInt(lines[7 * i + 5].split(" ").last().strip())

        println("${isMultiply}, ${isSelfVal}, ${opVal}, ${divisible}, ${trueMonkey}, ${falseMonkey}")

        Monkey(i, items, isMultiply, isSelfVal, opVal.toLong(), divisible.toLong(), trueMonkey, falseMonkey)
    }

    for (i in 1..numRounds) {
        Monkey.ALL_MONKEYS.forEach { monkey ->
            monkey.operate()
        }
    }
    println(Monkey.ALL_MONKEYS.map {it.inspectionCounter })
    val topTwo = Monkey.ALL_MONKEYS.map {it.inspectionCounter }.sorted().takeLast(2)
    println(topTwo[0] * topTwo[1].toLong())
}

class Monkey(
    val idx: Int,
    val items: MutableList<Long>,
    val isMultiply: Boolean, // else is add
    val isSelfVal: Boolean, // old = old * old
    val opVal: Long,
    val divisible: Long,
    val trueMonkey: Int,
    val falseMonkey: Int,
) {
    var inspectionCounter = 0

    init {
        ALL_MONKEYS.add(this)
        globalLCMKinda *= divisible
    }

    companion object {
        val ALL_MONKEYS = ArrayList<Monkey>()
        var globalLCMKinda = 1L // ideally a lcm but w/e lol
    }

    fun operate() {
        inspectionCounter += items.size
        for (i in items.indices) {
            if (isMultiply) {
                if (isSelfVal) {
                    items[i] *= items[i]
                } else {
                    items[i] *= opVal
                }
            } else {
                items[i] += opVal
            }
//            items[i] /= 3L
            items[i] = items[i] % globalLCMKinda
        }

        val iterator = items.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()

            if (item % divisible == 0L) {
                ALL_MONKEYS[trueMonkey]!!.items.add(item)
            } else {
                ALL_MONKEYS[falseMonkey]!!.items.add(item)
            }
            iterator.remove()
        }
    }
}
