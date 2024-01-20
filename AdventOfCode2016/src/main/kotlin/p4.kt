import java.io.File

fun main() {
    val rooms =
        File("inputs/input4.txt").readLines().map {
            val split = it.split("-")
            val name = split.subList(0, split.lastIndex).joinToString("")
            val rawName = split.subList(0, split.lastIndex).joinToString(" ")
            val end = split[split.lastIndex]
            val sectorId = end.substring(0, end.indexOf("[")).toInt()
            val checkSum = end.substring(end.indexOf("[")).removePrefix("[").removeSuffix("]")
            Room(name, sectorId, checkSum, rawName)
        }

    println(rooms.filter { it.checkSum == it.trueChecksum }.map { it.sectorId }.sum())
    for (room in rooms) {
        println("${room.sectorId}, ${room.getRealName()}")
    } // CTRL-F for north / northpole
}

data class Room(
    val name: String,
    val sectorId: Int,
    val checkSum: String,
    val rawName: String,
) {
    val trueChecksum = name.groupingBy { it }.eachCount().toList().sortedWith(
        compareBy({ -1 * it.second }, { it.first })
    ).subList(0, 5).map { it.first }.joinToString("")

    fun getRealName(): String {
        var ret = ""
        for (ch in rawName) {
            if (ch.isWhitespace()) {
                ret += ' '
            } else {
                ret += ((((ch - 'a') + sectorId) % 26) + 'a'.toInt()).toChar()
            }
        }
        return ret
    }
}