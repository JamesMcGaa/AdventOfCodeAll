@file:Suppress("unused")

import java.io.File
import java.math.BigInteger
import kotlin.math.absoluteValue

const val N = 10007

class Deck() {
    var head: LinkedListNode = LinkedListNode(0, this)
    var direction = 1


    init {
        initFromList((0 until N).toList())
    }

    fun initFromList(inp: List<Int>) {
        val newHead = LinkedListNode(inp.first(), this)
        var prev = newHead
        for (i in inp.subList(1, inp.size)) {
            val new = LinkedListNode(i, this)
            prev.next = new
            new.prev = prev
            prev = new
        }
        prev.next = newHead
        newHead.prev = prev
        head = newHead
    }

    fun toList(): List<Int> {
        val ret = mutableListOf<Int>()
        ret.add(head.amount)
        var current = head.next
        while (current != head) {
            ret.add(current.amount)
            current = current.next
        }
        return ret
    }

    fun cut(amount: Int) {
        repeat(amount.absoluteValue) {
            head = if (direction * amount >= 0) {
                head.next
            } else {
                head.prev
            }
        }
    }

    fun dealNewStack() {
        head = head.prev
        direction *= -1
    }

    fun dealIncrement(amount: Int) {
        initFromList(toIncrementList(amount))
    }

    fun toIncrementList(amount: Int): List<Int> {
        val ret = mutableListOf<Int>()
        repeat(N) {
            ret.add(-730)
        }

        var current = head
        var currentPtr = 0

        repeat(N) {
            ret[currentPtr] = current.amount
            current = current.next
            currentPtr = (currentPtr + amount) % N
        }

        return ret
    }

    fun dealIncrementLL(amount: Int) {
        var newHead = head
        var oldHead = head.splice()
        var tail = head
        while (oldHead.next != oldHead) {
            repeat(amount - 1) {
                oldHead = oldHead.next
            }

            val spliced = oldHead
            oldHead = oldHead.splice()

            spliced.prev = tail
            tail.next = spliced
            tail = spliced
        }
        tail.next = head
        head.prev = tail
        head = newHead
    }
}

data class LinkedListNode(
    val amount: Int,
    val deck: Deck,
) {
    private var _next: LinkedListNode? = null
    private var _prev: LinkedListNode? = null

    var next: LinkedListNode
        get() = if (deck.direction >=0) _next!! else _prev!!
        set(value) {
            if (deck.direction >=0) {
                _next = value
            } else {
                _prev = value
            }
        }

    var prev: LinkedListNode
        get() =  if (deck.direction >=0) _prev!! else _next!!
        set(value) {
            if (deck.direction >=0) {
                _prev = value
            } else {
                _next = value
            }
        }

    fun splice(): LinkedListNode {
        prev.next = next
        next.prev = prev
        val ret = next
        this._next = null
        this._prev = null
        return ret
    }
}

fun p22aLinkedList() {
    val deck = Deck()
    File("inputs/input22.txt").forEachLine {
        println(deck.toList())
        println(it)
        if (it.contains("cut")) {
            val amount = it.filter { it.isDigit() || it == '-' }.toInt()
            deck.cut(amount)
        } else if (it.contains("increment")) {
            val amount = it.filter { it.isDigit() }.toInt()
            deck.dealIncrement(amount)
        } else {
            deck.dealNewStack()
        }
    }
    println("final")
    println(deck.toList().indexOf(2019))
}

fun p22aRegularList() {
    var computer = (0 until N).toList()
    File("inputs/input22.txt").forEachLine {
        if (it.contains("cut")) {
            val amount = it.filter { it.isDigit() || it == '-' }.toInt()
            computer = if (amount > 0) {
                computer.subList(amount, computer.size) + computer.subList(0, amount)
            } else {
                computer.subList(computer.size - amount.absoluteValue, computer.size) + computer.subList(0, computer.size - amount.absoluteValue)
            }
        } else if (it.contains("increment")) {
            val amount = it.filter { it.isDigit() }.toInt()
            val newComputer = mutableListOf<Int>()
            repeat(N) {
                newComputer.add(-730)
            }
            var currentPtr = 0
            computer.forEach {
                newComputer[currentPtr] = it
                currentPtr = (currentPtr + amount) % N
            }
            computer = newComputer
        } else {
            computer = computer.reversed().toMutableList()
        }
    }
    println(computer.indexOf(2019))
}

fun simpleA() {
    val shuffles = File("inputs/input22.txt").readLines().map {
        if (it.contains("cut")) {
            val amount = it.filter { it.isDigit() || it == '-' }.toInt()
            Shuffle(0, amount, it)
        } else if (it.contains("increment")) {
            val amount = it.filter { it.isDigit() || it == '-' }.toInt()
            Shuffle(1, amount, it)
        } else {
            Shuffle(2, null, it)
        }
    }

    val deckSize = 10007L
    var currentIdx = 2019L
    shuffles.forEach {
        if (it.type == 0) {
            currentIdx = (currentIdx - it.amount!!) % deckSize
        } else if (it.type == 1) {
            currentIdx = (it.amount!! * currentIdx) % deckSize
        } else {
            currentIdx = (deckSize - 1) - currentIdx
        }
    }
    println(currentIdx)
}

data class Shuffle(
    val type: Int,
    val amount: Int?,
    val line: String,
)

fun partBCycleAttempt() {
    val shuffles = File("inputs/input22.txt").readLines().map {
        if (it.contains("cut")) {
            val amount = it.filter { it.isDigit() || it == '-' }.toInt()
            Shuffle(0, amount, it)
        } else if (it.contains("increment")) {
            val amount = it.filter { it.isDigit() || it == '-' }.toInt()
            Shuffle(1, amount, it)
        } else {
            Shuffle(2, null, it)
        }
    }

    val deckSize = 119315717514047L
    val idxToIteration = mutableMapOf(2020L to 0L)
    var currentIteration = 0L
    var currentIdx = 2020L
    while (true) {
        if (currentIteration % 1000000L == 0L) {
            println(idxToIteration.size)
        }
        shuffles.forEach {
            if (it.type == 0) {
                currentIdx = (currentIdx - it.amount!!) % deckSize
            } else if (it.type == 1) {
                currentIdx = (it.amount!! * currentIdx) % deckSize
            } else {
                currentIdx = (deckSize - 1) - currentIdx
            }
        }
        if (currentIdx in idxToIteration) {
            println("$currentIdx, ${idxToIteration[currentIdx]}")
            return
        }
        idxToIteration[currentIdx] = ++currentIteration
    }
}


// Credits: Google Bard
fun modInverse(a: Long, m: Long, fermats: Boolean = false): Long {
    if (fermats) {
        return modularPow(a, m-2, m)
    }
    // Credits: Google Bard
    var x = 1L
    var y = 0L
    var m0 = m
    var a0 = a

    while (a0 > 1L) {
        val q = a0 / m0
        var t = m0

        m0 = a0 % m0
        a0 = t
        t = y

        y = x - q * y
        x = t
    }

    if (x < 0L) {
        x += m
    }

//    println("$($a, ${m-2}, $m): $x")
    return x
}

// Credits: https://en.wikipedia.org/wiki/Modular_exponentiation#Pseudocode
fun modularPow(b: Long, e: Long, modulus: Long): Long {
    if (modulus == 1L) {
        println("wei")
        return 0L
    }
    var result = 1L
    var base = b % modulus
    var exponent = e
    while (exponent > 0L) {
        if (exponent % 2L == 1L) {
            result = (result * base) % modulus
//            println(result)
        }
        exponent = exponent shr 1
        base = (base * base) % modulus
    }
//    println("$($b, $e, $modulus): $result")
    return result
}

fun main() {
    val shuffles = File("inputs/input22.txt").readLines().map {
        if (it.contains("cut")) {
            val amount = it.filter { it.isDigit() || it == '-' }.toInt()
            Shuffle(0, amount, it)
        } else if (it.contains("increment")) {
            val amount = it.filter { it.isDigit() || it == '-' }.toInt()
            Shuffle(1, amount, it)
        } else {
            Shuffle(2, null, it)
        }
    }

    // Credits https://www.reddit.com/r/adventofcode/comments/ee0rqi/comment/fbnkaju/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
    var incrementMultiplier = 1L
    var offset = 0L
    val deckSize = 119315717514047L
    val numShuffles = 101741582076661L

    shuffles.forEach {
        println("$incrementMultiplier, $offset")
        println(it.line)
        if (it.type == 0) {
            offset += incrementMultiplier * it.amount!!
            offset %= deckSize
        } else if (it.type == 1) {
            println("modinv")
            println(modInverse(it.amount!!.toLong(), deckSize, false))
            val new = BigInteger(incrementMultiplier.toString()) * BigInteger(
                modInverse(
                    it.amount!!.toLong(),
                    deckSize,
                    false
                ).toString()
            )
            val ret = new % BigInteger(deckSize.toString())
            incrementMultiplier = ret.toLong()
        } else {
            incrementMultiplier *= -1
            incrementMultiplier %= deckSize
            offset += incrementMultiplier
            offset %= deckSize
        }
    }
    if (incrementMultiplier < 0) {
        incrementMultiplier += deckSize
    }
    if (offset < 0) {
        offset += deckSize
    }
    println("final")
    println("$incrementMultiplier, $offset")

    //incrementMultiplier = 50861413717150,
    // offset = 46829725060782
//    val finalIncrement = modularPow(incrementMultiplier, numShuffles, deckSize)
    val finalIncrement = 113356032258830 // to avoid overflow
    //    val finalOffset = (offset * (1 - finalIncrement) * modInverse((1-incrementMultiplier) % deckSize, deckSize)) % deckSize
    var finalOffset = 117299609056615
    println("$finalIncrement, $finalOffset")
    println("${((finalOffset + 2020 * finalIncrement) % deckSize)}")
}

