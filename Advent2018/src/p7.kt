import java.io.File
import java.util.SortedSet

fun deepcopyDeps(deps: MutableMap<Char, SortedSet<Char>>): MutableMap<Char, SortedSet<Char>> {
    val new = mutableMapOf<Char, SortedSet<Char>>()
    deps.forEach { k, v -> new[k] = v.toSortedSet() }
    return new
}

fun main() {
    val depFor = mutableMapOf<Char, SortedSet<Char>>() // Dep to target
    val depsOriginal = mutableMapOf<Char, SortedSet<Char>>() // Target to its deps
    val allNodes = mutableSetOf<Char>()
    val noDepsCandidatesForRemovalOriginal = sortedSetOf<Char>()
    File("inputs/input7.txt").forEachLine {
        val neededDep = it[5]
        val toStart = it[36]
        allNodes.add(neededDep)
        allNodes.add(toStart)
        depFor.getOrPut(neededDep) { sortedSetOf<Char>() }.apply { add(toStart) }
        depsOriginal.getOrPut(toStart) { sortedSetOf<Char>() }.apply { add(neededDep) }
    }
    noDepsCandidatesForRemovalOriginal.addAll(allNodes - depsOriginal.keys)
    // Some will not be referenced as needing a dep, those are our starting set

    val partA = mutableListOf<Char>()
    val depsA = deepcopyDeps(depsOriginal)
    val noDepsCandidatesForRemovalA = noDepsCandidatesForRemovalOriginal.toSortedSet()
    while (noDepsCandidatesForRemovalA.isNotEmpty()) {
        val first = noDepsCandidatesForRemovalA.first()
        noDepsCandidatesForRemovalA.remove(first)
        partA.add(first)
        depFor[first]?.forEach { potentialTarget ->
            depsA[potentialTarget]!!.remove(first)
            if (depsA[potentialTarget]!!.isEmpty()) {
                noDepsCandidatesForRemovalA.add(potentialTarget)
            }
        }
    }
    assert(partA.size == allNodes.size)
    println("Part A: ${partA.joinToString("")}")


    val partB = mutableListOf<Char>()
    val depsB = deepcopyDeps(depsOriginal)
    val noDepsCandidatesForRemovalB = noDepsCandidatesForRemovalOriginal.toMutableList()
    var workingQueue = mutableSetOf<Pair<Char, Int>>()
    var second = 0
    val numElves = 5
    val baseTimeTaken = 60
    while (noDepsCandidatesForRemovalB.isNotEmpty() || workingQueue.isNotEmpty()) {
        // Perform work
        var done = workingQueue.filter { it.second == 1 }.map { it.first }
        partB.addAll(done)
        workingQueue = workingQueue.filter { it.second != 1 }.map { it.copy(second = it.second - 1) }.toMutableSet()

        // See if new nodes have freed up after completion events
        done.forEach { doneNode ->
            depFor[doneNode]?.forEach { potentialTarget ->
                depsB[potentialTarget]?.remove(doneNode)
                if (depsB[potentialTarget]?.isEmpty() == true) {
                    noDepsCandidatesForRemovalB.add(potentialTarget)
                    depsB.remove(potentialTarget) // Prevent adding
                }
            }
        }

        // Fill workers if possible
        repeat(numElves - workingQueue.size) {
            if (noDepsCandidatesForRemovalB.isNotEmpty()) {
                val first = noDepsCandidatesForRemovalB.first()
                noDepsCandidatesForRemovalB.remove(first)
                workingQueue.add(Pair(first, baseTimeTaken + 1 + (first - 'A')))
            }
        }
        second++
    }

    println(partB.joinToString(""))
    println("Part B: ${second - 1}")
}