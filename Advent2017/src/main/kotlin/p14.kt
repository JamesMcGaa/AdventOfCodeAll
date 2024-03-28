const val INPUT = "stpzcrnm"

fun main() {
    val hashes = mutableListOf<String>()
    for (i in 0..127) {
        hashes.add(knothashB("$INPUT-$i"))
    }

    val binaryHashes = hashes.map { hash ->
        hash
            .toCharArray()
            .map innerMap@{ hexCh ->
                var bin = Integer.parseInt(hexCh.toString(), 16).toString(2)
                while (bin.length < 4) {
                    bin = "0" + bin
                }
                return@innerMap bin
            }.joinToString("")
    }
    println(binaryHashes.sumOf { it.count { it == '1' } })

    val graph = mutableSetOf<Coord>()
    for (i in 0..127) {
        for (j in binaryHashes.indices) {
            if (binaryHashes[j][i] == '1') {
                graph.add(Coord(i, j))
            }
        }
    }

    // From P13
    val seenOverall = mutableSetOf<Coord>()
    var islandCounter = 0
    for (node in graph) {
        if (!seenOverall.contains(node)) {
            islandCounter += 1

            val seen = mutableSetOf<Coord>()
            val stack = mutableListOf(node)
            while (stack.isNotEmpty()) {
                val current = stack.removeLast()
                if (seen.contains(current)) continue

                seen.add(current)
                listOf(Coord(1, 0), Coord(-1, 0), Coord(0, 1), Coord(0, -1)).forEach {
                    val newCoord = Coord(current.x + it.x, current.y + it.y)
                    if (graph.contains(newCoord)) {
                        stack.add(newCoord)
                    }
                }
            }

            seenOverall.addAll(seen)
        }
    }
    println(islandCounter)
}