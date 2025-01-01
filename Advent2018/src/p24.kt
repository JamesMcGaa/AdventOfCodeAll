import java.io.File
import java.util.Scanner

val input = File("inputs/input24.txt").readLines()

fun main() {
    println("Part A: ${run24(0).second}")

    /**
     * Sometimes binary search is better manually, credits to Reddit for that dumb trick https://www.reddit.com/r/adventofcode/comments/a91ysq/comment/ecfyy4y/
     */
    val scanner = Scanner(System.`in`)
    while (true) {
        print("Input: ")
        val num = scanner.nextInt()
        println("Output: ${run24(num)}")
    }
}

fun immuneGrouping(a: Int, b: Int, isImmune: Boolean, boost: Int = 0): List<ImmuneUnit> {
    return input.subList(a, b).mapIndexed { idx, it ->
        val split = it.split(" ")
        val units = split[0].toInt()
        val hp = split[4].toInt()
        val damage = split[split.size - 6].toInt()
        var weaknesses = mutableSetOf<DamageType>()
        var immunities = mutableSetOf<DamageType>()
        if (it.contains("(")) {
            val substr = it.substring(it.indexOf("(") + 1, it.indexOf(")"))
            val groups = substr.split("; ")
            groups.forEach {
                if (it.contains("immune to ")) {
                    val tokens = it.removePrefix("immune to ").split(", ")
                    immunities.addAll(tokens.map {
                        DamageType.fromStr(it)
                    })
                } else if (it.contains("weak to ")) {
                    val tokens = it.removePrefix("weak to ").split(", ")
                    weaknesses.addAll(tokens.map {
                        DamageType.fromStr(it)
                    })
                } else {
                    throw Exception()
                }
            }
        }
        val damageType = DamageType.fromStr(split[split.size - 5])
        val initiative = split[split.size - 1].toInt()
        ImmuneUnit(hp, units, damageType, damage + boost, initiative, weaknesses, immunities, isImmune, idx)
    }
}

fun run24(boost: Int): Pair<Boolean, Int> {
    var immuneUnits = immuneGrouping(1, 11, true, boost) // Set to your inputs linebreaks
    var infectionUnits = immuneGrouping(13, 23, false)

    val highToLowEPThenHighToLowInitiative = compareByDescending<ImmuneUnit> { it.effectivePower }
        .thenByDescending { it.initiative }

    while (immuneUnits.isNotEmpty() && infectionUnits.isNotEmpty()) {
        val targetMap = mutableMapOf<ImmuneUnit, ImmuneUnit>()

        listOf(Pair(immuneUnits, infectionUnits), Pair(infectionUnits, immuneUnits)).forEach {
            val attackingGroup = it.first
            val untargetedDefendingGroup = it.second.toMutableSet()
            attackingGroup.sortedWith(highToLowEPThenHighToLowInitiative).forEach { attacker ->
                // Go after weak, then normals according to EP then initiative
                val target =
                    untargetedDefendingGroup
                        .filter { untargeted -> attacker.damageType in untargeted.weaknesses }
                        .sortedWith(highToLowEPThenHighToLowInitiative).firstOrNull() ?: untargetedDefendingGroup
                        .filter { untargeted -> attacker.damageType !in untargeted.immunities }
                        .sortedWith(highToLowEPThenHighToLowInitiative).firstOrNull()
                if (target != null) {
                    untargetedDefendingGroup.remove(target)
                    targetMap[attacker] = target
                }
            }
        }
        var losses = false // Sometimes we get stuck with 2 stragglers that cannot deal whole damage to each other
        (immuneUnits + infectionUnits).sortedByDescending { it.initiative }.forEach { attacking ->
            if (attacking.units <= 0) return@forEach
            val defender = targetMap[attacking] ?: return@forEach
            val multiplier = if (attacking.damageType in defender.weaknesses) 2 else 1
            val unitLoss = (attacking.units * attacking.damage * multiplier) / defender.hp
            if (unitLoss > 0) {
                losses = true
            }
            defender.units -= unitLoss
        }
        if (!losses) {
            return Pair(false, -1) // infinite loop
        }
        immuneUnits = immuneUnits.filter { it.units > 0 }
        infectionUnits = infectionUnits.filter { it.units > 0 }
    }

    return Pair(immuneUnits.isNotEmpty(), (immuneUnits + infectionUnits).sumOf { it.units })
}

enum class DamageType {
    BLUDGEONING, RADIATION, SLASHING, COLD, FIRE;

    companion object {
        fun fromStr(str: String): DamageType {
            return when (str) {
                "bludgeoning" -> BLUDGEONING
                "slashing" -> SLASHING
                "radiation" -> RADIATION
                "fire" -> FIRE
                "cold" -> COLD
                else -> throw Exception()
            }
        }
    }
}

class ImmuneUnit(
    val hp: Int,
    var units: Int,
    val damageType: DamageType,
    val damage: Int,
    val initiative: Int,
    val weaknesses: Set<DamageType> = setOf(),
    val immunities: Set<DamageType> = setOf(),
    val isImmune: Boolean,
    val idx: Int,
) {
    val effectivePower
        get() = units * damage

    override fun toString(): String {
        return name
    }

    val name
        get() = "${if (isImmune) "Immune System" else "Infection"} Group ${idx + 1}"
}