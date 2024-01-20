import java.io.File

fun main() {
    val overallCapacity = 150
//    val capacities = listOf(5, 10, 15, 20)
//    var count = 0
//    for (a in 0..overallCapacity / capacities[0] + 1) {
//        for (b in 0..overallCapacity / capacities[1] + 1) {
//            for (c in 0..overallCapacity / capacities[2] + 1) {
//                for (d in 0..overallCapacity / capacities[3] + 1) {
//                    if (a * capacities[0] + b * capacities[1] + c * capacities[2] + d * capacities[3] == overallCapacity) {
//                        count += 1
//                    }
//                }
//            }
//        }
//    }
//    println(count)

    val combos = mutableListOf<List<Boolean>>()
    val containers = File("input/input17.txt").readLines().map { it.toInt() }
    var minContainers = containers.size - 1
    var containersSatisfyingMin = 0
    allCombinations(combos, mutableListOf(), containers.size)
    var counterA = 0
    for (combo in combos) {
        var counter = 0
        for (i in combo.indices) {
            if (combo[i]) {
                counter += containers[i]
            }
        }
        if (counter == overallCapacity) {
            counterA++
            val containersUsed = combo.filter { it }.size
            if (containersUsed < minContainers) {
                minContainers = containersUsed
                containersSatisfyingMin = 1
            } else if (containersUsed == minContainers) {
                containersSatisfyingMin++
            }
        }

    }
    println(counterA)
    println(containersSatisfyingMin)
}

fun allCombinations(list: MutableList<List<Boolean>>, current: MutableList<Boolean>, max: Int) {
    if (current.size == max - 1) {
        list.add(current + mutableListOf(true))
        list.add(current + mutableListOf(false))
    } else {
        allCombinations(list, (current + mutableListOf(true)).toMutableList(), max)
        allCombinations(list, (current + mutableListOf(false)).toMutableList(), max)
    }
}