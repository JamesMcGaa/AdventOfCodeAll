import java.io.File

fun main() {
    val input = File("inputs/input4.txt").readLines().map {it.split(" ")}
    println(input.filter {it.size == it.toSet().size}.size)
    println(input.map{ it.map { it.toSortedSet().joinToString("") } }.filter {it.size == it.toSet().size}.size)
}
