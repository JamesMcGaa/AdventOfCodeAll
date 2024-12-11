@file:Suppress("LocalVariableName")

import java.io.File

fun main() {
    val input = File("inputs/input8.txt").readLines()[0]
    val coordToLayers = mutableMapOf<Coord, MutableList<Int>>()
    val layersToCoord = mutableMapOf<Int, MutableMap<Coord, Int>>()
    val ROW_SIZE = 25
    val COL_SIZE = 6
    val LAYER_SIZE = ROW_SIZE * COL_SIZE
    input.forEachIndexed { index, ch ->
        val unlayered = index % LAYER_SIZE
        val layer = index / LAYER_SIZE
        val row = unlayered / ROW_SIZE
        val col = unlayered % ROW_SIZE
        if (!layersToCoord.contains(layer)) {
            layersToCoord[layer] = mutableMapOf()
        }
        layersToCoord[layer]!![Coord(row, col)] = ch.toString().toInt()
        coordToLayers[Coord(row, col)] =
            coordToLayers.getOrDefault(Coord(row, col), mutableListOf()).apply { add(ch.toString().toInt()) }
    }

    val layer = layersToCoord.values.minBy { layer -> layer.values.count { pixel -> pixel == 0 } }
    val ones = layer.values.count {pixel -> pixel == 1}
    val twos = layer.values.count {pixel -> pixel == 2}
    println("Part A: ${ones * twos}")

    println("-----------------Part B-----------------")
    val firstVisibleGrid = coordToLayers.mapValues {entry -> entry.value.first { it != 2 }}
    for (i in 0 until COL_SIZE) {
        var row = ""
        for (j in 0 until ROW_SIZE) {
            var decoded = firstVisibleGrid[Coord(i,j)].toString()
            if (decoded == "0") { // For ease of visibility
                decoded = " "
            }
            row += decoded
        }
        println(row)
    }
}