import java.io.File
import kotlin.system.exitProcess

fun main() {
    var counter = 0
    File("input/input8.txt").forEachLine { line ->
        var i = 0
        var repCounter = 0
        while (i in line.indices) {
            val ch = line[i]
            var nextCh = line.getOrNull(i + 1)
            repCounter += 1
            if (ch == '\\') {
                when (nextCh) {
                    '\\' -> i += 1
                    '"' -> i += 1
                    'x' -> i += 3
                    else -> Unit
                }
            }
            i += 1
        }
        counter += line.length - repCounter + 2
    }
    println(counter)

    counter = 0
    File("input/input8.txt").forEachLine { line ->
        var local = 0
        line.forEach {
            if (it == '"' || it == '\\') {
                local += 1
            }
        }
        local += 2 // for the outer quotes
        println(local)
        counter += local
    }
    println(counter)
}
