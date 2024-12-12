import java.io.File
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

class Formula(
    val output: Pair<String, Long>,
    val inputs: List<Pair<String, Long>>
)

fun main() {
    val formulas = mutableMapOf<String, Formula>()
    val oreFormulas = mutableMapOf<String, Formula>()
    File("inputs/input14.txt").forEachLine {
        val inputStr = it.split("=>")[0].trim()
        val outputStr = it.split("=>")[1].trim()
        val outputAmount = outputStr.split(" ")[0].toLong()
        val outputChem = outputStr.split(" ")[1]
        val output = Pair(outputChem, outputAmount)
        val inputs = inputStr.split(",").map { pair ->
            val inputAmount = pair.trim().split(" ")[0].toLong()
            val inputChem = pair.trim().split(" ")[1]
            Pair(inputChem, inputAmount)
        }
        if (inputs.any { it.first == "ORE" }) {
            oreFormulas[output.first] = Formula(output, inputs)
        }
        formulas[output.first] = Formula(output, inputs)
    }
    val chemWallet = mutableMapOf("FUEL" to 1L)
    val partA = minOres(0, chemWallet, formulas, oreFormulas)
    println(partA)

    val TARGET = 1000000000000L
    var low = 1L
    var pointer = low
    var amount = partA
    while (amount < 1000000000000L) {
        pointer *= 2
        amount = minOres(0, mutableMapOf("FUEL" to pointer), formulas, oreFormulas)
    }
    var high = pointer
    while (high > low + 1) {
        val midpoint = (high + low) / 2
        if (minOres(0, mutableMapOf("FUEL" to midpoint), formulas, oreFormulas) < TARGET) {
            low = midpoint
        } else {
            high = midpoint
        }
    }
    println("$low, $high")
    println(minOres(0, mutableMapOf("FUEL" to low), formulas, oreFormulas))
    println(minOres(0, mutableMapOf("FUEL" to high), formulas, oreFormulas))
}

val seen = mutableSetOf<String>()
val INFINITY = Long.MAX_VALUE
var BEST_SO_FAR = INFINITY

fun minOres(
    initalOreCount: Long,
    chemWallet: MutableMap<String, Long>,
    formulas: Map<String, Formula>,
    oreFormulas: Map<String, Formula>
): Long {
    // Neither optimization is necessary
//    val strRep = chemWallet.toList().sortedBy { it.first }.toString()
//    if (strRep in seen || initalOreCount > BEST_SO_FAR) {
//        return INFINITY
//    } else {
//        seen.add(strRep)
//    }


    var oreCount = initalOreCount
    val toBeChecked = chemWallet.keys.toMutableSet()
    while (toBeChecked.isNotEmpty()) {
        val chem = toBeChecked.first()
        val amount = chemWallet[chem]!!
        toBeChecked.remove(chem)

        if (amount < 0) { // Surplus instead of a need to fill
            continue
        }

        if (chem == "ORE") {
            oreCount += amount
            chemWallet.remove(chem)
        } else {
            val formula = formulas[chem]!!
            val amountRequiredForReaction = formula.output.second
            val numReactions = ceil((amount / amountRequiredForReaction.toDouble())).toLong() // Theres exactly 1 way to get each output
            // Thus we round
//            if (numReactions == 0L) continue // Unneeded now
            chemWallet[chem] = chemWallet[chem]!! - numReactions * amountRequiredForReaction
            if (chemWallet[chem] == 0L) {
                chemWallet.remove(chem)
            }

            formulas[chem]!!.inputs.forEach { inputChem ->
                toBeChecked.add(inputChem.first)
                chemWallet[inputChem.first] =
                    chemWallet.getOrDefault(inputChem.first, 0) + inputChem.second * numReactions
            }
        }
    }
    return oreCount

//    if (chemWallet.values.any { it > 0 }) { // This no longer occurs due to rounding up
//        val recurs = mutableListOf<Long>()
//        val valuesToTry = chemWallet.keys.filter { chemWallet[it]!! > 0 } intersect oreFormulas.keys
//        valuesToTry.forEach { chemToBeStimulus ->
//            val formula = formulas[chemToBeStimulus]!!
//            val outputAmount = formula.output.second
//            recurs.add(
//                minOres(
//                    oreCount,
//                    chemWallet.toMutableMap().apply {
//                        formulas[chemToBeStimulus]!!.inputs.forEach { inputChem ->
//                            set(inputChem.first, getOrDefault(inputChem.first, 0) + inputChem.second)
//                        }
//                        set(chemToBeStimulus, get(chemToBeStimulus)!! - outputAmount) },
//                    formulas,
//                    oreFormulas
//                )
//            )
//
//        }
//        val ans =  recurs.minOrNull() ?: INFINITY
//        BEST_SO_FAR = min(ans, BEST_SO_FAR)
//        return ans
//    } else {
//        val ans =  oreCount
//        BEST_SO_FAR = min(ans, BEST_SO_FAR)
//        return ans
//    }
}
