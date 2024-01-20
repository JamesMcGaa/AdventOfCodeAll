import java.io.File

fun main(){
    runP5(false)
    runP5(true)
}

fun runP5(isPartB: Boolean) {
    val jumps = File("inputs/input5.txt").readLines().map { it.toInt() }.toMutableList()
    var counter = 0
    var pointer = 0
    while (pointer in jumps.indices) {
        val oldPointer = pointer
        pointer += jumps[pointer]
        if (isPartB && jumps[oldPointer] >= 3) jumps[oldPointer]-- else jumps[oldPointer]++
        counter += 1
    }
    println(counter)
}
