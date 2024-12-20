import java.io.File


fun main() {
    val words = mutableListOf<String>()
    val grammar = mutableMapOf<String, List<List<String>>>()
    var readingGrammar = true
    File("inputs/input19b_examples.txt").readLines().forEach {
        if (it.isEmpty()) {
            readingGrammar = false
        } else if (readingGrammar) {
            val start = it.split(":")[0]
            val end = it.split(":")[1].split("|")
                .map { it.split(" ").map { it.filter { it.isDigit() || it.isLetter() } }.filter { it.isNotBlank() } }
            grammar[start] = end
        } else {
            words.add(it)
        }
    }

    grammar.entries.forEach { (lhs, rule) ->
        rule.forEach { rhs ->
            if (rhs.size != 2 && rhs.first().first().isDigit()) {
                println("$lhs, $rhs")
            }
        }
    }

//    for (i in 1..47) {
//        println("800${i}: 42 42 | 800${i + 1} 42")
//    }
    // Step 1 eliminate start symbol - not needed
    // Step 2 remove null productions - not needed
    // Did need to remove 1 unit production
    // Step 3 eliminate mixed RHS - not needed


    println(grammar)
    println(words)
    fun cykParse(word: String): Boolean {
        val wordSize = word.length

        // T is a n x n table that contains a set of strings at each entry
        val T = MutableList(wordSize) { MutableList(wordSize) { mutableSetOf<String>() } }
//        val T = mutableListOf<MutableList<MutableSet<String>>>()
//        repeat(wordSize) {
//            val ret = mutableListOf<MutableSet<String>>()
//            repeat(wordSize) {
//                ret.add(mutableSetOf<String>())
//            }
//            T.add(ret)
//        }

        for (j in 0 until wordSize) {
            grammar.forEach { start, patterns ->
                patterns.forEach { pattern ->
                    if (pattern.size == 1 && pattern.first() == word[j].toString()) {
                        T[j][j].add(start)
                    }
                }
            }

            for (i in (0..j).reversed()) {
                for (k in i..j) {
                    grammar.forEach { start, patterns ->
                        patterns.forEach { pattern ->
                            if (pattern.size == 2 && pattern[0] in T[i][k] && k + 1 < wordSize && pattern[1] in T[k + 1][j]) {
                                T[i][j].add(start)
                            }
                        }
                    }
                }
            }
        }

//        T.forEach { t ->
//            println(t)
//        }

//        println(T[0][wordSize - 1])
        return "0" in T[0][wordSize - 1]
    }

//    println(cykParse("bbabbbbaabaabba"))
    val target = setOf(
        "bbabbbbaabaabba",
        "babbbbaabbbbbabbbbbbaabaaabaaa",
        "aaabbbbbbaaaabaababaabababbabaaabbababababaaa",
        "bbbbbbbaaaabbbbaaabbabaaa",
        "bbbababbbbaaaaaaaabbababaaababaabab",
        "ababaaaaaabaaab",
        "ababaaaaabbbaba",
        "baabbaaaabbaaaababbaababb",
        "abbbbabbbbaaaababbbbbbaaaababb",
        "aaaaabbaabaaaaababaa",
        "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa",
        "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba",
    )
    println(words.filter { cykParse(it) } - target)
    println(target - words.filter { cykParse(it) })
    println(words.count { cykParse(it) })

}

//0: 8001 11 | 42 11