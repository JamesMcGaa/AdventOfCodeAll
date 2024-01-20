fun main() {
    val triples = mutableMapOf<Int, Set<Char>>()
    val INPUT = "cuanljph"
    var counter = 0
    val keys = mutableSetOf<Int>()
    while (keys.size < 70) { // Overshoot slightly
        var hash = md5(INPUT + counter.toString())
        repeat(2016) { // Part B
            hash = md5(hash)
        }
        val containedTriples = mutableSetOf<Char>()
        val digits = '0'..'9'
        val letters = 'a'..'f'
        val regex = Regex("([a-z0-9])\\1\\1")
        val matchResult = regex.find(hash)
        if (matchResult != null) {
            containedTriples.add(matchResult.value[0])
        }

        for (ch in digits + letters) {
            if (hash.contains(ch.toString().repeat(5))) {
                for (i in counter - 1000..counter - 1) {
                    if (triples[i]?.contains(ch) == true) {
                        keys.add(i)
                        println(keys.size)
                    }
                }
            }
        }
        triples[counter] = containedTriples
        counter += 1
    }
    println(triples)
    println(keys.sorted())
    println(keys.sorted()[63])
}


