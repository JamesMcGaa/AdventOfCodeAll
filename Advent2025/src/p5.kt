import java.io.File

fun main() {
    println("Part A: ${p5a("inputs/input5.txt")}")
    println("Part B: ${p5b("inputs/input5.txt")}")
}

fun p5a(filename: String): Long {
    val input = File(filename).readLines()
    val breakIdx = input.indexOfFirst { it.isBlank() }
    val ranges = input.subList(0, breakIdx).map {
        it.split("-").first().toLong()..it.split("-").last().toLong()
    }
    val ingredients = input.subList(breakIdx + 1, input.size).map { it.toLong() }
    return ingredients.count { ingredient -> ranges.any { range -> range.contains(ingredient) } }.toLong()
}

fun p5b(filename: String): Long {
    val input = File(filename).readLines()
    val breakIdx = input.indexOfFirst { it.isBlank() }
    val ranges = input.subList(0, breakIdx).map {
        it.split("-").first().toLong()..it.split("-").last().toLong()
    }

    val rangeSet = Utils.DisjointRangeSet()
    for (range in ranges) {
        rangeSet.add(Utils.DisjointRangeSet.InclusiveRange(range.first, range.last))
    }

    return rangeSet.ranges.sumOf { it.size }
}
