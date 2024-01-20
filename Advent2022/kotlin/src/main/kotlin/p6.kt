import java.io.File

fun main(args: Array<String>) {

    val line = File("inputs/input6.txt").readLines()[0]
    for (i in 0 until line.length - 13) {
        if (line.substring(i, i+14).toList().distinct().size == 14) {
            println(i+14)
            return
        }
    }
}


