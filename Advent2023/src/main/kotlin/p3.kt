import java.io.File
typealias Key = Pair<Int, Int>
typealias Keyset = HashSet<Key>

fun main() {
    var rowIdx = 0
    val coords = hashMapOf<Key, Char>()
    val keyToLeft = hashMapOf<Key, Key>()
    val leftToVal = hashMapOf<Key, Int>()
    File("inputs/input3.txt").forEachLine {
        line ->
            line.forEachIndexed {
                idx, ch -> coords[Pair(idx, rowIdx)] = ch
            }
            rowIdx += 1

    }

    var counter = 0
    coords.forEach { (key, ch) ->
        var x = key.first
        val y = key.second
        if (ch.isDigit() && (!coords.contains(Pair(x-1,y)) || !coords[Pair(x-1,y)]!!.isDigit())) {
            var num = ch.toString()
            val xLeft = x
            keyToLeft[key] = key
            while (coords[Pair(x+1,y)]?.isDigit() == true) {
                num += coords[Pair(x+1,y)]!!
                x += 1
                keyToLeft[Pair(x,y)] = key
            }
            val xRight = x

            var isValid = false
            for (i in xLeft - 1.. xRight + 1) {
                for (j in y - 1.. y + 1) {
                    val neighbor = coords[Pair(i,j)]
                    if (neighbor != null && !neighbor.isDigit() && neighbor != '.') {
                        isValid = true
                    }
                }
            }

            if(isValid) {
                counter += num.toInt()
                leftToVal[key] = num.toInt()
            }
        }
    }
    println(counter)

    var counter2 = 0
    coords.forEach { (key, ch) ->
        if (ch == '*') {
            val seen = hashSetOf<Key>()
            for (i in -1..1) {
                for (j in -1..1) {
                    val potentialLeft = keyToLeft[Pair(key.first + i, key.second + j)]
                    potentialLeft?.let {
                        seen.add(potentialLeft)
                    }
                }
            }
            if (seen.size == 2) {
                counter2 += seen.map {leftToVal[it]!!}.reduce(Int::times)
            }
        }
    }
    println(counter2)

}
