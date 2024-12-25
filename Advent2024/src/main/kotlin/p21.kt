package main.kotlin

data class

fun main() {
    val numpad = Utils.readAsGrid<Char>("inputs/input21_numpad.txt")
    val dirpad = Utils.readAsGrid<Char>("inputs/input21_dirpad.txt")
    Utils.printGrid(numpad)
    Utils.printGrid(dirpad)
}