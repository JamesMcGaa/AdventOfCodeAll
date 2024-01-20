import java.io.File

fun main() {
    val rows = File("inputs/input2.txt").readLines().map { line ->
        line.split("\t").filter { it.isNotBlank() }.map { it.toInt() }
    }
    val partA = rows.sumOf {
        it.max() - it.min()
    }
    println(partA)

    var partB = 0
    for (row in rows) {
        for (valA in row){
            for (valB in row){
                if (valA > valB && valA % valB == 0) partB += valA / valB
            }
        }
    }
    println(partB)
}