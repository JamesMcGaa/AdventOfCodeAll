import java.io.File


fun main() {
    val piles = mutableMapOf<String, MutableList<Int>>()
    val lines = ArrayDeque(File("inputs/input10.txt").readLines().sorted().reversed())
    while(lines.isNotEmpty()) {
        val line = lines.removeLast()
        val split = line.split(" ")
        val op = split[0]
        when (op) {
            "value" -> {
                val value = split[1].toInt()
                val receiverBot = split[4] + split[5]
                piles.getOrPut(receiverBot, { mutableListOf() }).add(value)
            }
            "bot" -> {
                val givingBot = split[0] + split[1]
                if (piles[givingBot]?.size != 2) {
                    lines.addFirst(line)
                    continue
                }
                val low = piles[givingBot]!!.min()
                val high = piles[givingBot]!!.max()
                piles[givingBot]!!.removeLast()
                piles[givingBot]!!.removeLast()

                val lowDest = split[5] + split[6]
                val highDest = split[10] + split[11]
                if (low == 17 && high == 61) {
                    println(split[1])
                }
                piles.getOrPut(lowDest, { mutableListOf() }).add(low)
                piles.getOrPut(highDest, { mutableListOf() }).add(high)
            }
        }
    }
    println(piles["output0"]!!.removeLast() * piles["output1"]!!.removeLast() * piles["output2"]!!.removeLast())
}
