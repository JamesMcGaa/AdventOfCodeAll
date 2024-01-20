import java.io.File

fun main() {
    runP6(false)
}

fun runP6(isPartB: Boolean) {
    val banks = File("inputs/input6.txt").readLines()[0]
        .split("\t").filter { it.isNotBlank() }.map { it.toInt() }
        .toMutableList()
    val seen = mutableMapOf<Int, Int>()
    var counterA = 0
    while (!seen.contains(banks.hashCode())) {
        seen[banks.hashCode()] = counterA
        counterA += 1
        val idx = banks.indexOf(banks.max())
        val original = banks[idx]
        val amountToDist = original / banks.size
        val numWithExtra = original % banks.size
        banks[idx] = 0

        for (offset in banks.indices) {
            if (offset != 0 && offset <= numWithExtra) {
                banks[Math.floorMod(idx + offset, banks.size)] += amountToDist + 1
            } else {
                banks[Math.floorMod(idx + offset, banks.size)] += amountToDist
            }
        }
    }
    println(counterA)
    println(counterA - seen[banks.hashCode()]!!)
}
