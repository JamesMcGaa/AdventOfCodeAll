import java.io.File
import kotlin.math.max
import kotlin.math.min

data class ConversionRange(
    val dest: Long,
    val source: Long,
    val length: Long,
) {
    val sourceEnd = source + length - 1
    val sourceToDestOffset = dest - source
}

data class Mapping(
    val sourceType: String,
    val destType: String,
    val ranges: MutableList<ConversionRange>
) {
    fun convert(source: Long): Long {
        for (range in ranges) {
            if (source >= range.source && source <= range.source + range.length - 1) {
                return range.dest + (source - range.source)
            }
        }
        return source
    }
}

data class Range(
    var begin: Long,
    var end: Long,
) {
    fun isValid(): Boolean {
        return end > begin
    }
}

fun main() {

    // Process input
    val MAPPINGS = hashMapOf<String, Mapping>()
    var SEEDS = listOf<Long>()
    var RANGES = mutableListOf<Range>()
    var currentSource = ""
    File("inputs/input5.txt").forEachLine { line ->
        if (SEEDS.isEmpty()) {
            val seedVals = line.split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }
            SEEDS = seedVals
            seedVals.forEachIndexed { index, seedVal ->
                if (index % 2 == 0) {
                    RANGES.add(Range(seedVal, 0L))
                }
                else {
                    RANGES[RANGES.lastIndex].end = RANGES[RANGES.lastIndex].begin + seedVal - 1
                }
            }
        } else {
            if (line.contains("map")) {
                val dashed = line.split(" ")[0]
                val source = dashed.split("-")[0]
                val dest = dashed.split("-")[2]
                currentSource = source
                MAPPINGS[source] = Mapping(source, dest, mutableListOf())
            } else if (line.isNotBlank()) {
                val nums = line.split(" ").map { it.toLong() }
                MAPPINGS[currentSource]!!.ranges.add(
                    ConversionRange(
                        nums[0], nums[1], nums[2]
                    )
                )
            }
        }
    }

    // Part A
    var currentVals = SEEDS.toList()
    var currentType = "seed"
    while (currentType != "location") {
        val mapping = MAPPINGS[currentType]!!
        currentType = mapping.destType
        currentVals = currentVals.map { mapping.convert(it) }
    }
    println(currentVals.min())

    // Part B
    var currentRanges = RANGES.toMutableList()
    currentType = "seed"
    while (currentType != "location") {
        val mapping = MAPPINGS[currentType]!!
        currentType = mapping.destType
        var newRanges = mutableListOf<Range>()
        var rangesToProcess = currentRanges.toMutableList()
        while (rangesToProcess.isNotEmpty()) {
            val range = rangesToProcess.removeLast()
            var converted = false
            conversionLoop@for (conversionRange in mapping.ranges) {
                val result = splitRange(range, conversionRange)
                if (result.processed != null) {
                    converted = true
                    newRanges.add(result.processed)
                    result.leftRemain?.let {rangesToProcess.add(it)}
                    result.rightRemain?.let {rangesToProcess.add(it)}
                    break@conversionLoop
                }
            }
            if (!converted) {
                newRanges.add(range)
            }
        }
        currentRanges = newRanges
    }
    println(currentRanges.map {it.begin}.min())

}

data class Split(
    val leftRemain: Range?,
    val processed: Range?,
    val rightRemain: Range?,
)
fun splitRange(range: Range, conversionRange: ConversionRange): Split {
    var left: Range? = Range(range.begin, conversionRange.source)
    var right: Range? = Range(conversionRange.sourceEnd, range.end)
    var middle: Range? = Range(
        max(range.begin, conversionRange.source),
        min(range.end, conversionRange.sourceEnd)
    )
    if (!right!!.isValid()) {
        right = null
    }
    if (!left!!.isValid()) {
        left = null
    }
    if (!middle!!.isValid()) {
        middle = null
    }
    else {
        middle.begin += conversionRange.sourceToDestOffset
        middle.end += conversionRange.sourceToDestOffset
    }
    return Split(left, middle, right)
}
