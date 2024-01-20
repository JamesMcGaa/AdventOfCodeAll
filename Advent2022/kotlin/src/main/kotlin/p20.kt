import java.io.File

fun main() {
  var root: Node? = null
  var prev: Node? = null
  var tail: Node? = null
  File("input20.txt").forEachLine { line ->
    val value = Integer.parseInt(line.trim()).toLong()
    tail = Node(value, null, tail)
    if (root == null) {
      root = tail
    }
    if (prev != null) {
      prev!!.next = tail
    }
    prev = tail
  }
  tail!!.next = root
  root!!.prev = tail

  val toBeMixed = mutableListOf<Node>(root!!)
  var current = root!!.next
  while (current != root) {
      toBeMixed.add(current!!)
      current = current.next
  }
  val NUM_ITERATIONS = 10 // PART B
  for (i in 1..NUM_ITERATIONS) {
      for (node in toBeMixed) {
        node.advance()
      }
  }


  root!!.printChain()

  println(Node.Companion.ZERO_NODE!!.getNth(1000).value + Node.Companion.ZERO_NODE!!.getNth(2000).value + Node.Companion.ZERO_NODE!!.getNth(3000).value )

}

class Node(var value: Long, var next: Node?, var prev: Node?) {

  init {
    COUNT += 1
    if (value == 0L) {
      ZERO_NODE = this
    }
    value *= 811589153L // PART B
  }

  companion object {
    var COUNT = 0
    var ZERO_NODE: Node? = null
  }

  fun printChain() {
    val values = mutableListOf(value)
    var current = next
    while (current != this) {
      values.add(current!!.value)
      current = current.next
    }
    println(values)
  }

  fun getEffectiveValue(): Long {
    return (value % (COUNT-1)) + (COUNT - 1)
  }

  fun advance() {
    // Excise
    val p = prev
    val n = next
    p!!.next = n
    n!!.prev = p

    // Advance
    var current = p
    for (i in 1 .. getEffectiveValue()) {
      current = current!!.next
    }

    // Insert
    val newNext = current!!.next 
    current.next = this
    this.prev = current
    newNext!!.prev = this
    this.next = newNext
  }

  fun getNth(n: Int): Node {
    var current = this
    for (i in 1 .. n) {
      current = current.next!!
    }
    return current
  }

}
