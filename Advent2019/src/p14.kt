@file:Suppress("LocalVariableName")

import java.io.File
import kotlin.math.ceil

class Formula(
    val output: Pair<String, Long>,
    val inputs: List<Pair<String, Long>>
)

fun main() {
    val FUEL = "FUEL"
    val TARGET = 1000000000000L

    val formulas = mutableMapOf<String, Formula>()
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
        formulas[output.first] = Formula(output, inputs)
    }

    fun innerMinOres(fuelCount: Long): Long{
        return minOres(mutableMapOf(FUEL to fuelCount), formulas)
    }

    val partA = innerMinOres(1L)
    println("Part A: $partA ores minimum")

    var low = 1L
    var pointer = low
    var amount = partA
    while (amount < TARGET) {
        pointer *= 2
        amount = innerMinOres(pointer)
    }
    var high = pointer
    while (high > low + 1) {
        val midpoint = (high + low) / 2
        if (innerMinOres(midpoint) < TARGET) {
            low = midpoint
        } else {
            high = midpoint
        }
    }
    println("Part B: low $low (answer) fuel to ${innerMinOres(low)} ores VS high $high fuel to ${innerMinOres(low)} ores")
}

fun minOres(
    chemWallet: MutableMap<String, Long>,
    formulas: Map<String, Formula>,
): Long {
    var oreCount = 0L
    val toBeChecked = chemWallet.keys.toMutableSet()
    while (toBeChecked.isNotEmpty()) {
        val chem = toBeChecked.first()
        val amount = chemWallet[chem]!!
        toBeChecked.remove(chem)

        if (amount <= 0L) { // "Surplus" instead of a "need" to fill
            continue
        }

        if (chem == "ORE") {
            oreCount += amount
            chemWallet.remove(chem)
        } else {
            val formula = formulas[chem]!!
            val amountRequiredForReaction = formula.output.second
            // There is exactly 1 way to get each output (when inspecting the input), thus we round up
            val numReactions = ceil((amount / amountRequiredForReaction.toDouble())).toLong()

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
}
