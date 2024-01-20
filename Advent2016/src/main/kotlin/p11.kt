import java.io.File


val ALL_NAMES = mutableSetOf<String>()

/**
 * This was an AMAZING problem with tons of opportunity for optimization and pruning
 *
 * Lots of insights to be found. I used help for the critical optimization
 */
fun main() {
    var floorNum = 0
    val base = ChipState(1)
    val allVariables = mutableSetOf<ComputerItem>()
    File("inputs/input11.txt").forEachLine {
        floorNum++
        val split = it.split(" a ").map { it.trim() }.filter { it.isNotBlank() }
        split.forEach {
            if (it.contains("generator")) {
                val strGen = it.split(" ")[0]
                base.floorMap[floorNum]!!.add(ComputerItem(isGenerator = true, strGen))
                allVariables.add(ComputerItem(isGenerator = true, strGen))
                ALL_NAMES.add(strGen) // For easier implementation of 'rep' val
            }
            if (it.contains("microchip")) {
                val strChip = it.split(" ")[0].removeSuffix("-compatible")
                base.floorMap[floorNum]!!.add(ComputerItem(isGenerator = false, strChip))
                allVariables.add(ComputerItem(isGenerator = false, strChip))
            }
        }
    }


    val END = ChipState(4)
    END.floorMap[4]!!.addAll(allVariables)
    println(base)
    println(base.rep)

    var frontier = mutableSetOf(base)
    var counter = 0
    var seen = mutableSetOf<ChipState>()
    while (frontier.isNotEmpty()) {
        var newFrontier = mutableSetOf<ChipState>()
        frontier.forEach {
            if (seen.contains(it)) {
                return@forEach
            }
            if (it == END) {
                println(it)
                println(END)
                println(it.rep)
                println(END.rep)
                println("Found in ${counter}!!!!")
                return
            }
            seen.add(it)
            val neighbors = it.getNeighbors()//.filter {nei -> !seen.contains(nei)}
            //seen.addAll(neighbors)
            newFrontier.addAll(neighbors) // Should remove duplicates here
        }
        counter += 1
        frontier = newFrontier
        println("${counter}, ${frontier.size}, ${seen.size}")
    }
    println("DIDNT FIND!!!")
}

data class ChipState(
    var elevatorFloor: Int,
    val floorMap: MutableMap<Int, MutableSet<ComputerItem>> = mutableMapOf(
        1 to mutableSetOf(),
        2 to mutableSetOf(),
        3 to mutableSetOf(),
        4 to mutableSetOf(),
    )
) {
    override fun hashCode(): Int {
        return rep.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is ChipState) return rep == other.rep
        return false
    }

    val rep: String
        get(): String {
            val flattened: MutableList<String> = mutableListOf()

            // Old Logic
//            floorMap.forEach { (floor, value) -> // (key, value) is for kotlin, no paren for java
//                value.forEach { item ->
//                    flattened.add("$item:$floor")
//                }
//            }
//            flattened.add("elevator:" + elevatorFloor.toString())
//            return flattened.sorted().toString()

            // Critical Optimization
            ALL_NAMES.forEach { flattened.add(floorsOf(it).toString()) }
            return flattened.sorted().toString()  + "elevator" + elevatorFloor.toString()
        }

    fun floorsOf(name: String): List<Int> {
        val ret = mutableListOf<Int>()
        floorMap.forEach{
            val floor = it.key
            for (computerItem in it.value) {
                if (computerItem.name ==  name) {
                    ret.add(floor)
                }
            }
        }
        return ret.sorted()
    }

    fun getNeighbors(): Set<ChipState> {
        val ret = mutableSetOf<ChipState>()
        val floor = floorMap[elevatorFloor]!!
        // Implicitly, the floor should never be empty (invariant)
        for (a in floor) {
            for (b in floor) {
                val copyUp = getTrueCopy()
                copyUp.floorMap[elevatorFloor]!!.remove(a)
                copyUp.floorMap[elevatorFloor]!!.remove(b)
                val copyDown = copyUp.getTrueCopy()

                if (copyUp.floorMap.containsKey(elevatorFloor + 1)) {
                    copyUp.elevatorFloor += 1
                    copyUp.floorMap[copyUp.elevatorFloor]!!.add(a)
                    copyUp.floorMap[copyUp.elevatorFloor]!!.add(b)
                    ret.add(copyUp)
                }

                if (copyDown.floorMap.containsKey(elevatorFloor - 1)) {
                    copyDown.elevatorFloor -= 1
                    copyDown.floorMap[copyDown.elevatorFloor]!!.add(a)
                    copyDown.floorMap[copyDown.elevatorFloor]!!.add(b)
                    ret.add(copyDown)
                }
            }
        }

        return ret.filter { it.isLegal() }.toMutableSet()
    }

    private fun getTrueCopy(): ChipState {
        val copy = ChipState(elevatorFloor)
        for (entry in floorMap) {
            copy.floorMap[entry.key] = entry.value.toMutableSet()
        }
        return copy
    }

    private fun isLegal(): Boolean {
        // Keep global map of illegal hashes to avoid repeat work
        if (ILLEGALS.contains(this)) return false

        floorMap.values.forEach { floor ->
            val generators = mutableSetOf<String>()
            val chips = mutableSetOf<String>()
            floor.forEach { item ->
                if (item.isGenerator) {
                    generators.add(item.name)
                }
                else {
                    chips.add(item.name)
                }
            }
            val unpairedChips = chips - generators
            if (unpairedChips.isNotEmpty() && generators.isNotEmpty()) { // Radiation explosion
                ILLEGALS.add(this)
                return false
            }
        }
        return true
    }

    companion object {
        val ILLEGALS = mutableSetOf<ChipState>()
    }
}

data class ComputerItem(
    val isGenerator: Boolean,
    val name: String
)

// Input for part B
//The first floor contains a polonium generator, a thulium generator, a thulium-compatible microchip, a promethium generator, a ruthenium generator, a ruthenium-compatible microchip, a cobalt generator, and a cobalt-compatible microchip, a elerium-compatible microchip, a elerium generator, a dilithium generator, a dilithium-compatible microchip
//The second floor contains a polonium-compatible microchip and a promethium-compatible microchip.
//The third floor contains nothing relevant.
//The fourth floor contains nothing relevant.
