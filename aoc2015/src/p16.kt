import java.io.File
import java.lang.Long.max

fun main() {
    val truth = mapOf<String, Int>(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    )

    File("input/input16.txt").forEachLine {
        val name = it.substring(it.indexOf(":") + 1)
        val pairs = name.split(',').map { it.trim() }.map { it.split(":").map { it.trim() } }
        pairs.forEach {
            val key = it[0]
            val value = it[1].toInt()
//            if (truth[key] != value) {
//                return@forEachLine
//            }

            when(key) {
                "cats", "trees" -> {
                    if (truth[key]!! >= value) {
                        return@forEachLine
                    }
                }
                "pomeranians", "goldfish" -> {
                    if (truth[key]!! <= value) {
                        return@forEachLine
                    }
                }
                else -> {
                    if (truth[key] != value) {
                        return@forEachLine
                    }
                }
            }
        }
        println(it)
    }

}


