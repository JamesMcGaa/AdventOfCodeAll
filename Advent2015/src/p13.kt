import java.io.File
import java.util.*

fun main() {
    val names = mutableListOf<String>()
    val dists = hashMapOf<Pair<String, String>, Int>()
    File("input/input13.txt").forEachLine { line ->
        val start = line.split(" ")[0]
        val parity = line.split(" ")[2]
        val units = line.split(" ")[3].toInt()
        val end = line.split(" ")[10].removeSuffix(".")
        if (!names.contains(start)) {
            names.add(start)
        }
        dists[Pair(start, end)] = units * (if (parity == "gain") 1 else -1)
    }

    var optimal = names.permutations().map { getWeight(it, dists) }.max()
    println(optimal)

    // Part B
    for (name in names) {
        dists[Pair(name, "self")] = 0
        dists[Pair("self", name)] = 0
    }
    names.add("self")

    optimal = names.permutations().map { getWeight(it, dists) }.max()
    println(optimal)
}

fun getWeight(permutation: List<String>, dists: HashMap<Pair<String, String>, Int>): Int{
    var counter = 0
    for (i in permutation.indices) {
        counter += dists[Pair(permutation[i], permutation[(i + 1) % permutation.size])]!!
        counter += dists[Pair(permutation[(i + 1) % permutation.size], permutation[i])]!!
    }
    return counter
}