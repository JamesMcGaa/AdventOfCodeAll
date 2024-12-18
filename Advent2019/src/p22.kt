import java.io.File
import kotlin.math.absoluteValue

val N = 10

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
            if (direction * amount >= 0) {
                head = head.next
            } else {
                head = head.prev
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

//    fun dealIncrement(amount: Int) {
//        var newHead = head
//        var oldHead = head.splice()
//        var tail = head
//        while (oldHead.next != oldHead) {
//            repeat(amount - 1) {
//                oldHead = oldHead.next
//            }
//
//            val spliced = oldHead
//            oldHead = oldHead.splice()
//
//            spliced.prev = tail
//            tail.next = spliced
//            tail = spliced
//        }
//        tail.next = head
//        head.prev = tail
//    }
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

fun main() {
    val deck = Deck()
    File("inputs/input22.txt").forEachLine {
        println(deck.toList())
        println(it)
        if (it.contains("cut")) {
            val amount = it.filter { it.isDigit() }.toInt()
            deck.cut(amount)
        } else if (it.contains("increment")) {
            val amount = it.filter { it.isDigit() }.toInt()
            deck.dealIncrement(amount)
        } else {
            deck.dealNewStack()
        }
    }
    println("final")
    println(deck.toList())
}