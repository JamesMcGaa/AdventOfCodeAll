import java.io.File

fun main() {
    val lines = File("inputs/input22.txt").readLines()
    val processed = lines.subList(2, lines.lastIndex + 1)
    val nodes = mutableMapOf<Coord, Coord>() // (x,y) -> (cap, used)
    processed.forEachIndexed { index, line ->
        nodes[(Coord(index / 30, index % 30))] = Coord(
            line.split(" ").filter { it.isNotBlank() }[1].removeSuffix("T").toInt(),
            line.split(" ").filter { it.isNotBlank() }[2].removeSuffix("T").toInt(),
        )
    }

    println(nodes)

    var counterA = 0
    nodes.forEach { coordA, storageA ->
        nodes.forEach { coordB, storageB ->
            if (coordA != coordB && storageA.y != 0 && storageA.y <= storageB.x - storageB.y) counterA++
        }
    }
    println(counterA)

    for (y in 0..29) {
        var line = ""
        for (x in 0..32) {
//            line += "  ${nodes[Coord(x, y)]!!.y} / ${nodes[Coord(x, y)]!!.x}  "
            val usage = nodes[Coord(x, y)]!!.y.toFloat() / nodes[Coord(x, y)]!!.x
            if (usage < .2) {
                line += "_"
            }
            else if (nodes[Coord(x, y)]!!.y > 150) {
                line += "W"
            }
            else {
                line += "*"
            }
        }
        println(line)
    }
}