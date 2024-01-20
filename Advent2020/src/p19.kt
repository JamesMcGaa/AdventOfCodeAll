import java.io.File

fun main() {
    File("src/input19.txt").forEachLine { line ->
        val idx = Integer.parseInt(line.split(":")[0].split(" ")[1].trim())
        val oreBotOreCost = Integer.parseInt(line.split(":")[1].split(".")[0].split(" ").takeLast(2)[0])
        val clayBotOreCost = Integer.parseInt(line.split(":")[1].split(".")[1].split(" ").takeLast(2)[0])

        val obsidianBotOreCost = Integer.parseInt(line.split(":")[1].split(".")[2].split(" ").takeLast(5)[0])
        val obsidianBotClayCost = Integer.parseInt(line.split(":")[1].split(".")[2].split(" ").takeLast(5)[3])

        val geodeBotOreCost = Integer.parseInt(line.split(":")[1].split(".")[3].split(" ").takeLast(5)[0])
        val geodeBotObsidianCost = Integer.parseInt(line.split(":")[1].split(".")[3].split(" ").takeLast(5)[3])
        Blueprint(
            idx,
            oreBotOreCost,
            clayBotOreCost,
            obsidianBotOreCost,
            obsidianBotClayCost,
            geodeBotOreCost,
            geodeBotObsidianCost
        )
    }
    var counter = 1
    for (blueprint in Blueprint.Companion.ALL_BLUEPRINTS) {
        println("------")
        println(blueprint)
        val result = blueprint.maxAfter(32)
        counter *= result
        println(result)
        println("------")
    }
    println(counter)
}

data class Blueprint(
    val idx: Int,
    val oreBotOreCost: Int,
    val clayBotOreCost: Int,
    val obsidianBotOreCost: Int,
    val obsidianBotClayCost: Int,
    val geodeBotOreCost: Int,
    val geodeBotObsidianCost: Int
) {
    companion object {
        val ALL_BLUEPRINTS = mutableListOf<Blueprint>()
    }

    init {
        ALL_BLUEPRINTS.add(this)
    }

    fun maxOre(): Int {
        return listOf(oreBotOreCost, obsidianBotOreCost, clayBotOreCost, geodeBotOreCost).max()!!
    }

    fun maxAfter(rounds: Int): Int {
        var candidates = hashSetOf(State(0,0,0,0,1,0,0,0, rounds, this))
        for (i in 1..rounds) {
            println("${i}, ${candidates.size}")
            val newCandidates = hashSetOf<State>()
            candidates.forEach {
                candidate ->
                newCandidates.addAll(candidate.getAdjacent())
            }
            candidates = HashSet(newCandidates.sortedBy { it.getScore() }) //.takeLast(1000000))
        }

        println(candidates.maxBy { candidate -> candidate.geodes })
        return candidates.maxBy { candidate -> candidate.geodes }!!.geodes
    }
}

data class State(
    val ores: Int,
    val clays: Int,
    val obsidians: Int,
    val geodes: Int,
    val oreBots: Int,
    val clayBots: Int,
    val obsidianBots: Int,
    val geodeBots: Int,
    val timeLeft: Int,
    val blueprint: Blueprint
) {
    fun getAdjacent(): MutableList<State> {
        var newOre = ores + oreBots
        var newClay = clays + clayBots
        var newObsidian = obsidians + obsidianBots
        var newGeodes = geodes + geodeBots

        val ret = mutableListOf<State>()
        val doNothingAction = this.copy(ores = newOre, clays = newClay, obsidians = newObsidian, geodes = newGeodes, timeLeft = timeLeft - 1)
        ret.add(doNothingAction)


        if (ores >= blueprint.oreBotOreCost && oreBots < blueprint.maxOre()) {
            ret.add(doNothingAction.copy(ores = newOre - blueprint.oreBotOreCost, oreBots = oreBots + 1))
        }
        if (ores >= blueprint.clayBotOreCost && clayBots < blueprint.obsidianBotClayCost) {
            ret.add(doNothingAction.copy(ores = newOre - blueprint.clayBotOreCost, clayBots = clayBots + 1))
        }
        if (ores >= blueprint.obsidianBotOreCost && clays >= blueprint.obsidianBotClayCost && obsidianBots < blueprint.geodeBotObsidianCost) {
            ret.add(doNothingAction.copy(
                ores = newOre - blueprint.obsidianBotOreCost,
                clays = newClay - blueprint.obsidianBotClayCost,
                obsidianBots = obsidianBots + 1
            ))
        }
        if (ores >= blueprint.geodeBotOreCost && obsidians >= blueprint.geodeBotObsidianCost) {
            ret.add(doNothingAction.copy(
                ores = newOre - blueprint.geodeBotOreCost,
                obsidians = newObsidian - blueprint.geodeBotObsidianCost,
                geodeBots = geodeBots + 1
            ))
        }

        return ret
    }

    fun getScore(): Int {
        return  (oreBots * timeLeft - ores ) * 1 +
                (clayBots * timeLeft - clays ) * 10 +
                (obsidianBots * timeLeft - obsidians) * 100 +
                (geodeBots * timeLeft + geodes ) * 100000
    }
}
