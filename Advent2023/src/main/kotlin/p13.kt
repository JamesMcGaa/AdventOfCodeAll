import java.io.File

fun main() {
    val pictures = mutableListOf(Picture(mutableListOf()))
    File("inputs/input13.txt").forEachLine {
        if (it.isEmpty()) {
            pictures.add(Picture(mutableListOf()))
        } else {
            pictures.last().lines.add(it)
        }
    }

    var counterA = 0
    var counterB = 0
    pictures.forEach {
        counterA += it.getReflectionIndex(false).first()
        counterB += it.getAltReflectionIndex()
    }
    println(counterA)
    println(counterB)
}

data class Picture(
    val lines: MutableList<String>,
    var altLines: MutableList<String> = mutableListOf()
) {

    fun getAltReflectionIndex(): Int {
        for (i in lines.indices) {
            for (j in lines[i].indices) {

                altLines = lines.toMutableList()
                var lineToChange = lines[i].toMutableList()
                lineToChange[j] = if (lines[i][j] == '#') '.' else '#'
                altLines[i] = lineToChange.joinToString("") // Cant use regular toString

                val new = getReflectionIndex(true)
                val old = getReflectionIndex(false)
                val diff = new.toSet() subtract old.toSet()
                if (diff.isNotEmpty()) {
                    return diff.sum()
                }
            }
        }
        throw Exception("Alternative reflect not found")
    }

    fun getReflectionIndex(useAlt: Boolean): List<Int> {
        val indices = mutableListOf<Int>()
        findCenterRow@ for (centerRowIdx in 0..lines.size - 2) {
            var upperIdx = centerRowIdx
            var lowerIdx = centerRowIdx + 1
            while (upperIdx >= 0 && lowerIdx <= lines.lastIndex) {
                if (getRow(upperIdx, useAlt) != getRow(lowerIdx, useAlt)) {
                    continue@findCenterRow
                }
                upperIdx -= 1
                lowerIdx += 1
            }
            indices.add(100 * (centerRowIdx + 1))
        }

        findCenterCol@ for (centerColIdx in 0..lines[0].length - 2) {
            var leftIdx = centerColIdx
            var rightIdx = centerColIdx + 1
            while (leftIdx >= 0 && rightIdx <= lines[0].lastIndex) {
                if (getCol(leftIdx, useAlt) != getCol(rightIdx, useAlt)) {
                    continue@findCenterCol
                }
                leftIdx -= 1
                rightIdx += 1
            }
            indices.add(centerColIdx + 1)
        }

        return indices
    }

    fun getCol(idx: Int, useAlt: Boolean): String {
        var col = ""
        val selectedLines = if (useAlt) altLines else lines
        for (line in selectedLines) {
            col += line[idx]
        }
        return col
    }

    fun getRow(idx: Int, useAlt: Boolean): String {
        return if (useAlt) altLines[idx] else lines[idx]
    }
}
