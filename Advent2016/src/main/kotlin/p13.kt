val END = Coord(31, 39)
val FAV_NUM = 1362
fun main() {
    val START = Coord(1, 1)
    var frontier = mutableSetOf(START)
    val seen = mutableSetOf(START)
    var counter = 0
    while (counter < 50) {
//    while (frontier.isNotEmpty()) {
        var newFrontier = mutableSetOf<Coord>()
        frontier.forEach {
//            if (it == END) {
//                println(counter)
//                return
//            }
            val neighbors = it.getNeighbors().filter { !seen.contains(it) && !it.isWall() }
            seen.addAll(neighbors)
            newFrontier.addAll(neighbors)
        }
        counter += 1
        frontier = newFrontier
    }
    println(seen.size)
}

fun Coord.isWall(): Boolean {
    if (this.x < 0 || this.y < 0) return true
    val adj = this.x * this.x + 3 * this.x + 2 * this.x * this.y + this.y + this.y * this.y
    val input = FAV_NUM
    return (adj + input).toString(2).count { it == '1' } % 2 == 1
}

fun Coord.getNeighbors(): Set<Coord> {
    return setOf(
        Coord(this.x + 1, this.y),
        Coord(this.x - 1, this.y),
        Coord(this.x, this.y - 1),
        Coord(this.x, this.y + 1),
    )
}