import java.io.File

fun main(args: Array<String>) {
    var counter = 0
    File("inputs/input3.txt").forEachLine {
        line ->
        val left = line.substring(0, line.length / 2)
        val right = line.substring(line.length / 2)
        left.forEach { ch ->
            if (right.contains(ch)) {
                counter += chVal(ch)
                return@forEachLine
            }
        }
    }
    println(counter)

    var intersectionCounter = 0
    val lines = File("inputs/input3.txt").readLines()
    for(i: Int in 0 until lines.size / 3) {
        val unionSet = strToSet(lines[3*i]) intersect  strToSet(lines[3*i+1]) intersect strToSet(lines[3*i+2])
        intersectionCounter += chVal(unionSet.toList().get(0))
    }
    println(intersectionCounter)
}

fun chVal(ch: Char): Int {
    if(ch.toInt() >= 'a'.toInt()) {
        return ch.toInt() - 'a'.toInt() + 1
    }
    return ch.toInt() - 'A'.toInt() + 27
}

fun strToSet(st: String): Set<Char> {
    val set = HashSet<Char>()
    st.forEach {
        set.add(it)
    }
    return set
}

