import java.io.File
import kotlin.math.sign

const val BROADCASTER = "broadcaster"
const val BUTTON = "BUTTON"

fun main() {
    File("inputs/input20.txt").forEachLine { line ->
        val dests = line.split("->")[1].split(",").map { it.trim() }
        var name = line.split("->")[0].trim()
        when (name.first()) {
            '%' -> {
                val adjName = name.removePrefix("%")
                Module.ALL_MODULES[adjName] = FlipFlop(adjName, dests)
            }
            '&' -> {
                val adjName = name.removePrefix("&")
                Module.ALL_MODULES[adjName] = Conjunction(adjName, dests)
            }
            'b' -> Module.ALL_MODULES[BROADCASTER] = Broadcaster(BROADCASTER, dests)
        }

        for (conjunction in Module.ALL_MODULES.values) {
            if (conjunction is Conjunction) {
                for (input in Module.ALL_MODULES.values) {
                    if (input.dests.contains(conjunction.name)) {
                        conjunction.inputs[input.name] = SignalPolarity.LOW
                    }
                }
            }
        }
    }

    // Part A
//    for (i in 1..1000) {
//        Module.pushButton()
//    }
//    println(Signal.LOW_COUNT)
//    println(Signal.HIGH_COUNT)
//    println(Signal.LOW_COUNT * Signal.HIGH_COUNT)

    // Part B
    for(i in 0..100000) {
        Module.pushButton()
    }
    println(
        Module.PENULTIMATE_TO_SUCCEEDING_FREQ.map {
            val acceptables = it.value
            val key = it.key
            val ret = mutableListOf<Long>()
            for (i in 0 .. acceptables.lastIndex - 1) {
                ret.add(acceptables[i+1] - acceptables[i])
            }
            ret.toSet()
        }
    )
    // The LCM of these values is how we obtain the answer
}


enum class SignalPolarity {
    LOW, HIGH
}

data class Signal(
    val polarity: SignalPolarity,
    val from: String,
    val to: String
) {
    init {
        when (polarity) {
            SignalPolarity.LOW -> LOW_COUNT += 1
            SignalPolarity.HIGH -> HIGH_COUNT += 1
        }
    }

    companion object {
        var LOW_COUNT = 0L
        var HIGH_COUNT = 0L
    }
}

interface Module {
    val name: String
    val dests: List<String>
    fun processSignal(signal: Signal) {
//        if (mutableListOf("vg","kp","gc","tx").contains(name)) {
//            if (signal.polarity == SignalPolarity.LOW) {
//                println("Name ${name}, Button Count ${Module.BUTTON_COUNT}")
//            }
//        }
        if (mutableListOf("bq").contains(name)) {
            if (signal.polarity == SignalPolarity.HIGH) {
                println("Name ${signal.from}, Button Count ${Module.BUTTON_COUNT}")
                if (PENULTIMATE_TO_SUCCEEDING_FREQ[signal.from] == null){
                    PENULTIMATE_TO_SUCCEEDING_FREQ[signal.from] = mutableListOf()
                }
                PENULTIMATE_TO_SUCCEEDING_FREQ[signal.from]!!.add(BUTTON_COUNT)
            }
        }
    }

    companion object {
        val ALL_MODULES = mutableMapOf<String, Module>()
        val SIGNAL_QUEUE = ArrayDeque<Signal>()
        var BUTTON_COUNT = 0L
        val PENULTIMATE_TO_SUCCEEDING_FREQ = mutableMapOf<String, MutableList<Long>>()

        fun pushButton() {
            BUTTON_COUNT += 1
            SIGNAL_QUEUE.addLast(Signal(SignalPolarity.LOW, BUTTON, BROADCASTER))
            while (SIGNAL_QUEUE.isNotEmpty()) {
                val currentSignal = SIGNAL_QUEUE.removeFirst()
                ALL_MODULES[currentSignal.to]?.processSignal(currentSignal)
            }
        }
    }
}

data class Broadcaster(
    override val name: String,
    override val dests: List<String>
) : Module {
    override fun processSignal(signal: Signal) {
        super.processSignal(signal)
        dests.forEach { Module.SIGNAL_QUEUE.add(signal.copy(from = this.name, to = it)) }
    }
}

data class FlipFlop(
    override val name: String,
    override val dests: List<String>,
    var polarity: Boolean = false
) : Module {
    override fun processSignal(signal: Signal) {
        super.processSignal(signal)
        when (signal.polarity) {
            SignalPolarity.LOW -> {
                polarity = !polarity
                val newPolarity = if (polarity) SignalPolarity.HIGH else SignalPolarity.LOW
                dests.forEach {
                    Module.SIGNAL_QUEUE.add(
                        Signal(
                            from = this.name,
                            to = it,
                            polarity = newPolarity
                        )
                    )
                }
            }

            SignalPolarity.HIGH -> Unit
        }
    }

}

data class Conjunction(
    override val name: String,
    override val dests: List<String>,
    val inputs: MutableMap<String, SignalPolarity> = mutableMapOf()
) : Module {
    override fun processSignal(signal: Signal) {
        super.processSignal(signal)
        inputs[signal.from] = signal.polarity
        val newPolarity =
            if (inputs.values.all { it == SignalPolarity.HIGH }) SignalPolarity.LOW else SignalPolarity.HIGH
        dests.forEach {
            Module.SIGNAL_QUEUE.add(
                Signal(
                    from = this.name,
                    to = it,
                    polarity = newPolarity
                )
            )
        }
    }
}

/**
 * This problem part 1 was really enjoyable OOP. I used help to see the trick to part 2 is via inspecting the input
 *
 * Overall super enjoyable problem
 */