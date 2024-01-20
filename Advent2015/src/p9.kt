import java.io.File
import kotlin.system.exitProcess

fun main() {
    val cities = mutableSetOf<String>()
    val map = hashMapOf<Pair<String, String>, Int>()
    File("input/input9.txt").forEachLine { line ->
        val dist = line.split("=")[1].trim().toInt()
        val from = line.split("=")[0].split("to")[0].trim()
        val to = line.split("=")[0].split("to")[1].trim()
        map[Pair(from, to)] = dist
        map[Pair(to, from)] = dist
        cities.add(to)
        cities.add(from)
    }
    println(map)
    val dists = cities.toList().permutations().map {
        var local = 0
        for (i in 0..it.lastIndex-1) {
            local += map[Pair(it[i], it[i+1])]!!
        }
        return@map local
    }
    println(dists.min())
    println(dists.max())
}

fun <E> List<E>.permutations(builtSequence: List<E> = listOf()): List<List<E>> =
    if (isEmpty()) listOf(builtSequence)
    else flatMap { (this - it).permutations(builtSequence + it) }