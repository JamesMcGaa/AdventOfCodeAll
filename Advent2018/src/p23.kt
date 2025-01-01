import org.sosy_lab.java_smt.SolverContextFactory
import org.sosy_lab.java_smt.api.*
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions
import java.io.File
import java.math.BigInteger
import kotlin.math.absoluteValue

fun main() {
    val nanobots =
        File("inputs/input23.txt").readLines().map {
            val nums = Utils.extractLongListFromString(it, legalBreaks = setOf(' ', ',')).map { it.toLong() }
            Nanobot(MultiCoord(nums[0], nums[1], nums[2]), nums[3])
        }
    val largest = nanobots.maxByOrNull { it.r }!!
    val partA = nanobots.count { (largest.pos - it.pos).manhattanDist <= largest.r }
    println("Part A: $partA")

    /**
     * Inspired by other Z3 solutions like https://github.com/msullivan/advent-of-code/blob/master/2018/23b.py
     *
     * Uses https://github.com/sosy-lab/java-smt for optimization
     *
     * Install the maven dep through intellij, then download the 3 dylib and jar files directly
     * and add them to both java path and to the intellij libraries
     */
    val context: SolverContext = SolverContextFactory.createSolverContext(SolverContextFactory.Solvers.Z3)
    val imgr: IntegerFormulaManager = context.formulaManager.integerFormulaManager
    val bmgr: BooleanFormulaManager = context.formulaManager.booleanFormulaManager

    val x = imgr.makeVariable("x")
    val y = imgr.makeVariable("y")
    val z = imgr.makeVariable("z")

    fun abs(a: NumeralFormula.IntegerFormula) =
        bmgr.ifThenElse(imgr.greaterOrEquals(a, imgr.makeNumber(0)), a, imgr.multiply(a, imgr.makeNumber(-1)))

    fun nanobotInRange(nano: Nanobot): NumeralFormula.IntegerFormula {
        val xDelta = abs(imgr.subtract(imgr.makeNumber(nano.pos.x), x))
        val yDelta = abs(imgr.subtract(imgr.makeNumber(nano.pos.y), y))
        val zDelta = abs(imgr.subtract(imgr.makeNumber(nano.pos.z), z))
        val manhattan = imgr.add(imgr.add(xDelta, yDelta), zDelta)
        return bmgr.ifThenElse(
            imgr.lessOrEquals(manhattan, imgr.makeNumber(nano.r)),
            imgr.makeNumber(1),
            imgr.makeNumber(0)
        )
    }

    val originDistFormula = imgr.add(imgr.add(abs(x), abs(y)), abs(z))

    var numInRangeFormula = imgr.makeNumber(0)
    nanobots.forEach { nanobot ->
        numInRangeFormula = imgr.add(numInRangeFormula, nanobotInRange(nanobot))
    }

    val prover: OptimizationProverEnvironment = context.newOptimizationProverEnvironment(ProverOptions.GENERATE_MODELS)
    prover.maximize(numInRangeFormula)
    prover.minimize(originDistFormula)
    val optimizationStatus = prover.check()
    val partBModelValues =
        prover.model.asList().filter { it.name in setOf("x", "y", "z") }.sumOf { it.value as BigInteger }
    println("Part B: $partBModelValues, (Status: $optimizationStatus)")
}

data class Nanobot(
    var pos: MultiCoord,
    var r: Long
)

data class MultiCoord(
    var x: Long,
    var y: Long,
    var z: Long,
) {
    val manhattanDist = x.absoluteValue + y.absoluteValue + z.absoluteValue

    operator fun minus(other: MultiCoord): MultiCoord {
        return MultiCoord(x - other.x, y - other.y, z - other.z)
    }
}