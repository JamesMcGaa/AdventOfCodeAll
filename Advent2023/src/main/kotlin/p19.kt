import java.io.File

data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int,
) {
    val ratingNumber = x + m + a + s
}

data class PartRange(
    val xRange: Pair<Int, Int>,
    val mRange: Pair<Int, Int>,
    val aRange: Pair<Int, Int>,
    val sRange: Pair<Int, Int>,
    var currentWorkflow: String
) {
    val isValid =
        xRange.first <= xRange.second && mRange.first <= mRange.second && aRange.first <= aRange.second && sRange.first <= sRange.second

    val volume: Long =
        (xRange.second - xRange.first + 1).toLong() * (mRange.second - mRange.first + 1).toLong() * (aRange.second - aRange.first + 1).toLong() * (sRange.second - sRange.first + 1).toLong()
}

data class Workflow(
    val identifier: String,
    val conditions: List<Conditional>,
    val default: String
) {
    fun runForPart(part: Part): String {
        for (condition in conditions) {
            if (condition.matchesPart(part)) {
                return condition.dest
            }
        }
        return default
    }
}

data class Conditional(
    val category: Char,
    val ineq: Char,
    val comparison: Int,
    val dest: String,
) {
    fun matchesPart(part: Part): Boolean {
        when (category) {
            'x' -> {
                return if (ineq == '>') part.x > comparison else part.x < comparison
            }

            'm' -> {
                return if (ineq == '>') part.m > comparison else part.m < comparison
            }

            'a' -> {
                return if (ineq == '>') part.a > comparison else part.a < comparison
            }

            's' -> {
                return if (ineq == '>') part.s > comparison else part.s < comparison
            }

            else -> throw Exception("Bad Category")
        }
    }

    fun bisectRange(range: PartRange): MutableList<PartRange> {
        when (category) {
            'x' -> {
                return if (ineq == '>') {
                    mutableListOf(
                        range.copy(
                            xRange = range.xRange.copy(first = comparison + 1),
                            currentWorkflow = dest
                        ), // [x+1, end] in dest
                        range.copy(xRange = range.xRange.copy(second = comparison)) // [start, x] in current
                    )
                } else {
                    mutableListOf(
                        range.copy(
                            xRange = range.xRange.copy(second = comparison - 1),
                            currentWorkflow = dest
                        ), // [start, x - 1] in dest
                        range.copy(xRange = range.xRange.copy(first = comparison)) // [x, end] in current
                    )
                }
            }

            'm' -> {
                return if (ineq == '>') {
                    mutableListOf(
                        range.copy(
                            mRange = range.mRange.copy(first = comparison + 1),
                            currentWorkflow = dest
                        ), // [x+1, end] in dest
                        range.copy(mRange = range.mRange.copy(second = comparison)) // [start, x] in current
                    )
                } else {
                    mutableListOf(
                        range.copy(
                            mRange = range.mRange.copy(second = comparison - 1),
                            currentWorkflow = dest
                        ), // [start, x - 1] in dest
                        range.copy(mRange = range.mRange.copy(first = comparison)) // [x, end] in current
                    )
                }
            }

            'a' -> {
                return if (ineq == '>') {
                    mutableListOf(
                        range.copy(
                            aRange = range.aRange.copy(first = comparison + 1),
                            currentWorkflow = dest
                        ), // [x+1, end] in dest
                        range.copy(aRange = range.aRange.copy(second = comparison)) // [start, x] in current
                    )
                } else {
                    mutableListOf(
                        range.copy(
                            aRange = range.aRange.copy(second = comparison - 1),
                            currentWorkflow = dest
                        ), // [start, x - 1] in dest
                        range.copy(aRange = range.aRange.copy(first = comparison)) // [x, end] in current
                    )
                }
            }

            's' -> {
                return if (ineq == '>') {
                    mutableListOf(
                        range.copy(
                            sRange = range.sRange.copy(first = comparison + 1),
                            currentWorkflow = dest
                        ), // [x+1, end] in dest
                        range.copy(sRange = range.sRange.copy(second = comparison)) // [start, x] in current
                    )
                } else {
                    mutableListOf(
                        range.copy(
                            sRange = range.sRange.copy(second = comparison - 1),
                            currentWorkflow = dest
                        ), // [start, x - 1] in dest
                        range.copy(sRange = range.sRange.copy(first = comparison)) // [x, end] in current
                    )
                }
            }

            else -> throw Exception("Bad Category")
        }
    }
}

fun strToConditional(input: String): Conditional {
    val dest = input.split(":")[1]
    val content = input.split(":")[0]
    val ineq = content[1]
    val category = content[0].toChar()
    val comparison = content.substring(2).toInt()
    return Conditional(category, ineq, comparison, dest)
}

fun main() {
    val workFlows = mutableMapOf<String, Workflow>()
    val partsList = mutableMapOf<Part, String>()

    var loadingConditions = true
    File("inputs/input19.txt").forEachLine {
        if (it.isBlank()) {
            loadingConditions = false
            return@forEachLine
        }

        if (loadingConditions) {
            val begin = it.indexOf('{')
            val identifier = it.substring(0, begin)
            val content = it.substring(begin + 1, it.lastIndex).split(",").toMutableList()
            val default = content.removeLast()
            workFlows[identifier] = Workflow(
                identifier, content.map { strToConditional(it) }, default
            )
        } else {
            val tokens = it.removePrefix("{").removeSuffix("}").split(",")
            partsList[
                Part(
                    tokens[0].substring(2).toInt(),
                    tokens[1].substring(2).toInt(),
                    tokens[2].substring(2).toInt(),
                    tokens[3].substring(2).toInt(),
                )
            ] = "in"
        }
    }

    for (part in partsList.keys) {
        var currentWorkflow = partsList[part]!!
        while (!listOf("A", "R").contains(currentWorkflow)) {
            currentWorkflow = workFlows[currentWorkflow]!!.runForPart(part)
        }
        partsList[part] = currentWorkflow
    }

    var counterA = 0
    for (part in partsList.keys) {
        if (partsList[part]!! == "A") {
            counterA += part.ratingNumber
        }
    }
    println(counterA)

    // Part B

    val successSet = mutableSetOf<PartRange>()
    val partRanges = mutableListOf<PartRange>(
        PartRange(
            Pair(1, 4000),
            Pair(1, 4000),
            Pair(1, 4000),
            Pair(1, 4000),
            "in"
        )
    )
    while (partRanges.isNotEmpty()) {
        var currentRange: PartRange? = partRanges.removeLast()

        if (currentRange!!.currentWorkflow == "A") {
            successSet.add(currentRange!!)
            continue
        }
        if (currentRange!!.currentWorkflow == "R") {
            continue
        }

        val currentWorkflow = workFlows[currentRange.currentWorkflow]!!
        for (condition in currentWorkflow.conditions) {
            if (currentRange != null) {
                val split = condition.bisectRange(currentRange)
                val newRange = split.filter { it.isValid }.getOrNull(0)
                currentRange = split.filter { it.isValid }.getOrNull(1)
                newRange?.let { partRanges.add(it) }
            }
        }

        currentRange?.let {
            partRanges.add(it.copy(currentWorkflow = currentWorkflow.default))
        }

    }
    println(successSet.map { it.volume }.sum())
    return

}

/**
 * Straightforward but involved representation here
 *
 * - Some people actually just eval() ed the input with some tricks
 *
 * - Others had a dedicated range library
 */