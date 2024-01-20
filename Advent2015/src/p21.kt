import java.io.File
import java.lang.Integer.max
import kotlin.math.min

fun main() {
    val lines = File("input/input21.txt").readLines()
    val weapons = lines.subList(0, 5).map(::lineToEquip)
    val armors = lines.subList(6, 12).map(::lineToEquip)
    val rings = lines.subList(13, 20).map(::lineToEquip)

    val boss = Player(104, 8, 1)

    var cheapestSoFar = 7777777
    var mostExpensiveSoFar = -1
    for (weapon in weapons) {
        for (armor in armors) {
            for (ring1 in rings) {
                for (ring2 in rings) {
                    val allItems = setOf(ring1, ring2).toList() + weapon + armor
                    val overall = allItems.reduce { sum, element -> sum + element }

                    // A
                    if (Player(100, overall.damage, overall.armor).winsFight(boss)) {
                        cheapestSoFar = min(cheapestSoFar, overall.cost)
                    }

                    // B
                    if (!Player(100, overall.damage, overall.armor).winsFight(boss)) {
                        mostExpensiveSoFar = max(mostExpensiveSoFar, overall.cost)
                    }
                }
            }
        }
    }

    println(cheapestSoFar)
    println(mostExpensiveSoFar)
}

fun lineToEquip(line: String): Equipment {
    val p = line.split(" ").filter { it.isNotBlank() }
    return Equipment(p[0], p[1].toInt(), p[2].toInt(), p[3].toInt())
}

data class Equipment(
    val name: String,
    val cost: Int,
    val damage: Int,
    val armor: Int
) {
    operator fun plus(other: Equipment): Equipment {
        return Equipment(name + other.name, cost + other.cost, damage + other.damage, armor + other.armor)
    }
}

data class Player(
    val hp: Int,
    val damage: Int,
    val armor: Int,
) {
    // Other goes second, calling object goes first
    fun winsFight(other: Player): Boolean {
        var currentHpSelf = hp
        var currentHpOther = other.hp
        while (true) {
            currentHpOther -= max(1, damage-other.armor)
            if (currentHpOther <= 0) return true

            currentHpSelf -= max(1, other.damage-armor)
            if (currentHpSelf <= 0) return false
        }
    }
}