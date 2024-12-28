fun main() {
    val recipes = mutableListOf(3, 7)
    var firstElf = 0
    var secondElf = 1
    val input = 430971 // Does not handle leading 0s
    val inputSize = input.toString().length
    var iterations = 1
    var partBDone = false
    while (recipes.size < input + 10 || !partBDone) {
        var sum = recipes[firstElf] + recipes[secondElf]
        if (sum >= 10) {
            recipes.add(sum / 10)
            recipes.add(sum % 10)
        } else {
            recipes.add(sum)
        }

        if (!partBDone && recipes.size >= inputSize + 1) {
            // Need to consider when we add 2 recipes at once
            if (sum >= 10) {
                val almostEnd = recipes.subList(recipes.size - inputSize - 1, recipes.size - 1).listToInt()
                if (almostEnd == input) {
                    println("Part B: ${recipes.size - inputSize}")
                    partBDone = true
                }
            }
            val end = recipes.subList(recipes.size - inputSize, recipes.size).listToInt()
            if (end == input) {
                println("Part B: ${recipes.size - inputSize}")
                partBDone = true
            }
        }

        firstElf += recipes[firstElf] + 1
        firstElf %= recipes.size
        secondElf += recipes[secondElf] + 1
        secondElf %= recipes.size
        iterations++
    }
    println("Part A: ${recipes.subList(input, input + 10).joinToString("")}")
}

fun List<Int>.listToInt(): Int {
    var ret = 0
    for (i in this) {
        ret *= 10
        ret += i
    }
    return ret
}