import java.io.File

fun main() {
    val lines = File("input/input19.txt").readLines()
    var rules = mutableListOf<Pair<String, String>>()
    for (i in 0..lines.lastIndex - 2) {
        val split = lines[i].split(" => ")
        rules.add(Pair(split[0], split[1]))
    }

    val base = lines[lines.lastIndex]

    val possible = mutableSetOf<String>()

    for (rule in rules) {
        val regex = rule.first.toRegex()
        val matches = regex.findAll(base)
            .map { it.range.first }
            .toList()
        for (matchIdx in matches) {
            val new = base.substring(0, matchIdx) + rule.second + base.substring(matchIdx + rule.first.length)
            possible.add(new)
        }
    }

    println(possible.size)


    rules = rules.sortedBy { it.second.length }.reversed().toMutableList()
    println(rules)
    var current = base
    var counter = 0
    outer@while (current.isNotBlank()) {
        counter++
        for (rule in rules) {
            val regex = rule.second.toRegex()
            val matches = regex.findAll(current)
                .map { it.range.first }
                .toList()
            for (matchIdx in matches) {
                val new = current.substring(
                    0,
                    matchIdx
                ) + rule.first + current.substring(matchIdx + rule.second.length)
                if (new == "e") {
                    println("Found in ${counter} steps")
                    return
                }
                if (new.isNotBlank()) {
                    current = new
                    continue@outer
                }
            }
        }

    }

//    var frontier = mutableSetOf<String>(base)
//    var counter = 0
//    while (true) {
//        counter += 1
//        var newFrontier = mutableSetOf<String>()
//        for (candidate in frontier) {
//            for (rule in rules) {
//                val regex = rule.second.toRegex()
//                val matches = regex.findAll(candidate)
//                    .map { it.range.first }
//                    .toList()
//                for (matchIdx in matches) {
//                    val new = candidate.substring(
//                        0,
//                        matchIdx
//                    ) + rule.first + candidate.substring(matchIdx + rule.second.length)
//                    if (new == "e") {
//                        println("Found in ${counter} steps")
//                        return
//                    }
//                    if (new.isNotBlank() || !new.contains("e")) {
//                        newFrontier.add(new)
//                    }
//                }
//            }
//        }
//        frontier = newFrontier
//    }
}

/**
 * Cool problem. For part 2 I originally tried going forwards, then backwards by brute force to no
 *  avail (millions by 6 cycles in either direction). I got help for this problem
 *
 *  - Many people used a greedy strategy that I used
 *
 *  - One used just random iteration
 *
 *  - One inspected the input and saw they all form a particular pattern. They generalized it from there
 */
