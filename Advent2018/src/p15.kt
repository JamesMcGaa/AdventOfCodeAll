import Utils.Coord
import Utils.Direction
import java.util.Scanner

fun main() {
    println("Part A: ${run15Battle(3, false).second}")

    // Taking inspiration from p24, just manually binary search here since its all we need in this problem
    val scanner = Scanner(System.`in`)
    while (true) {
        print("Input: ")
        val num = scanner.nextInt()
        println("Output: ${run15Battle(num, true)}")
    }
}

fun run15Battle(elfAttack: Int, returnOnElfDeath: Boolean, shouldPrintBattle: Boolean = false): Pair<Boolean, Int> {
    val readingOrder = compareBy<ArmyUnit> { it.pos.x }.thenBy { it.pos.y }
    val lowestHpThenReading = compareBy<ArmyUnit> { it.hp }.then(readingOrder)
    val grid = Utils.readAsGrid("inputs/input15.txt", null) { it }
    var goblins = grid.filterValues { it == 'G' }.map { ArmyUnit(it.key, true) }.toMutableSet()
    var elves = grid.filterValues { it == 'E' }.map { ArmyUnit(it.key, false, attack = elfAttack) }.toMutableSet()
    val walls = grid.filterValues { it == '#' }.keys

    fun printBattle() {
        if (!shouldPrintBattle) return
        val newGrid = mutableMapOf<Coord, Char>()
        elves.forEach { newGrid[it.pos] = 'E' }
        goblins.forEach { newGrid[it.pos] = 'G' }
        walls.forEach { newGrid[it] = '#' }
        Utils.printGrid(newGrid)
    }

    printBattle()
    var iterationCount = 0
    while (goblins.isNotEmpty() && elves.isNotEmpty()) {
        val total = (goblins + elves).sortedWith(readingOrder)
        total.forEach { unit ->
            var changed = false

            if (unit.hp <= 0) return@forEach
            val enemies = if (unit.isGoblin) elves else goblins

            // Move if not adjacent
            var newPos: Coord? = null
            if ((unit.pos.manhattanNeighbors intersect enemies.map { it.pos }
                    .toSet()).isEmpty()) { // Remember to map the enemies set, to avoid Set<Any>
                val blocked = (walls + goblins.map { it.pos }.toSet() + elves.map { it.pos }.toSet())
                val start = unit.pos
                val enemyAdj = enemies.map { it.pos.manhattanNeighbors }.flatten().toSet() - blocked
                var directionFill = mutableMapOf(
                    start.down to mutableSetOf(Direction.DOWN),
                    start.up to mutableSetOf(Direction.UP),
                    start.right to mutableSetOf(Direction.RIGHT),
                    start.left to mutableSetOf(Direction.LEFT),
                )
                var frontier = mutableSetOf<Coord>()
                frontier.addAll(start.manhattanNeighbors)
                val seen = mutableSetOf<Coord>()
                while (frontier.isNotEmpty()) {
                    val newFrontier = mutableSetOf<Coord>()
                    val newSeen = mutableSetOf<Coord>()
                    frontier.forEach { frontierNode ->
                        if (frontierNode !in blocked && frontierNode !in seen && frontierNode in grid.keys) {
                            newSeen.add(frontierNode)
                            for (neighbor in frontierNode.manhattanNeighbors) {
                                if (neighbor !in blocked && neighbor !in seen && neighbor in grid.keys) {
                                    newFrontier.add(neighbor)
                                    directionFill.getOrPut(neighbor) { mutableSetOf() }
                                        .apply { addAll(directionFill[frontierNode]!!) }
                                }
                            }
                        }
                    }
                    frontier = newFrontier
                    seen.addAll(newSeen)
                    val adj = enemyAdj intersect seen
                    if (adj.isNotEmpty()) {
                        val target = adj.sortedWith(compareBy<Coord> { it.x }.thenBy { it.y }).first()
                        val directions = directionFill[target]!!
                        newPos = if (Direction.UP in directions) {
                            unit.pos.up
                        } else if (Direction.LEFT in directions) {
                            unit.pos.left
                        } else if (Direction.RIGHT in directions) {
                            unit.pos.right
                        } else if (Direction.DOWN in directions) {
                            unit.pos.down
                        } else {
                            throw Exception()
                        }
                        break
                    }
                }
            }
            if (newPos != null) {
                changed = true
                unit.pos = newPos
            }

            // Attack
            var potentialTarget =
                enemies.filter { it.pos in unit.pos.manhattanNeighbors }
                    .sortedWith(lowestHpThenReading)
                    .firstOrNull()
            potentialTarget?.hp -= unit.attack
            if (potentialTarget != null && potentialTarget.hp <= 0) {
                changed = true
                enemies.remove(potentialTarget)
                if (!potentialTarget.isGoblin && returnOnElfDeath) {
                    return Pair(false, -1)
                }
                if (enemies.isEmpty()) {
                    val survivorHpCount = goblins.sumOf { it.hp } + elves.sumOf { it.hp }
                    val outcome = survivorHpCount * iterationCount
                    return Pair(potentialTarget.isGoblin, outcome)
                }
            }
            if (changed) {
                printBattle()
            }
        }
        iterationCount++
    }
    throw Exception()
}


class ArmyUnit(
    var pos: Coord,
    val isGoblin: Boolean,
    var hp: Int = 200,
    var attack: Int = 3
)