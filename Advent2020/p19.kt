import java.io.File

/**
 * Credits for this problem go to
 * https://www.geeksforgeeks.org/cocke-younger-kasami-cyk-algorithm/
 * I used their copy of the CYK Algorithm
 *
 * The only major change needed is that we need to convert the input into CNF format which it almost satisfies
 */
fun main() {
    execute("inputs/input19_handCNFed.txt")
    execute("inputs/input19b_handCNFed.txt")
}

fun execute(fileName: String) {
    val words = mutableListOf<String>()
    val grammar = mutableMapOf<String, List<List<String>>>()
    var readingGrammar = true
    File(fileName).readLines().forEach {
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


    // These LHS need to be reduced to avoid unit statements
    grammar.entries.forEach { (lhs, rule) ->
        rule.forEach { rhs ->
            if (rhs.size != 2 && rhs.first().first().isDigit()) {
                println("$lhs, $rhs")
            }
        }
    }
    /**
     * Then apply this to the input for part B
     *
     * 8: 42 42 | 42 8
     * 0: 8 11 | 42 11
     * 11: 42 31 | 42 730
     * 730: 11 31
     *
     * For part A remove the 8 term and simplify 0 to
     * 0: 42 11
     */

    // Old metaprogramming approach for 8
    //    for (i in 1..47) {
    //        println("800${i}: 42 42 | 800${i + 1} 42")
    //    }
    // Step 1 eliminate start symbol - not needed
    // Step 2 remove null productions - not needed
    // Did need to remove 1 unit production
    // Step 3 eliminate mixed RHS - not needed


    fun cykParse(word: String): Boolean {
        val wordSize = word.length

        // T is a n x n table that contains a set of strings at each entry
        val T = MutableList(wordSize) { MutableList(wordSize) { mutableSetOf<String>() } }
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

        return "0" in T[0][wordSize - 1]
    }

    println(words.count { cykParse(it) })
}