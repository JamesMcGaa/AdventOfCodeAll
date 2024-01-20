import java.io.File

fun main() {
    val characters = File("inputs/input15.txt").readLines()[0].split(",")
    println(characters.map { getHash(it) }.sum())

    val operations = characters.map { Operation(it) }
    val boxes = hashMapOf<Int, MutableList<Mirror>>()
    for (i in 0..255) {
        boxes[i] = mutableListOf()
    }

    operations.forEach {
        if (it.type == OperationType.ADD) {
            val boxIdx = getHash(it.label)
            val existingMirror = boxes[boxIdx]!!.filter { mirror -> mirror.label == it.label }.firstOrNull()
            if (existingMirror != null) {
                existingMirror.focalLen = it.addFocalLen!!
            } else {
                boxes[boxIdx]!!.add(Mirror(it.label, it.addFocalLen!!))
            }
        } else {
            val boxIdx = getHash(it.label)
            val existingMirror = boxes[boxIdx]!!.filter { mirror -> mirror.label == it.label }.firstOrNull()
            existingMirror?.let { boxes[boxIdx]!!.remove(it) }
        }
    }

    var counter = 0
    for (i in 0..255) {
        for (mirror in boxes[i]!!) {
            counter += (1 + i) * (1 + boxes[i]!!.indexOf(mirror)) * mirror.focalLen
        }
    }
    println(counter)
}

data class Mirror(var label: String, var focalLen: Int)

enum class OperationType {
    ADD, REMOVE
}

data class Operation(
    val string: String
) {
    var type: OperationType
    var label: String
    var addFocalLen: Int? = null

    init {
        if (string.last() == '-') {
            type = OperationType.REMOVE
            label = string.substring(0, string.length - 1)
        } else {
            type = OperationType.ADD
            label = string.split("=")[0]
            addFocalLen = string.split("=")[1].toInt()
        }
    }
}


fun getHash(input: String): Int {
    var counter = 0
    input.forEach { ch ->
        counter += ch.toInt()
        counter *= 17
        counter %= 256
    }
    return counter
}