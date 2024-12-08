import java.io.File


fun main() {
    File("inputs/input3.txt")
}

data class Line(
    val start: Pair<Int, Int>,
    val end: Pair<Int, Int>,
    val orientation: Orientation,
) {

    // Returns the manhattan dist (if any)
    fun intersection(other: Line): Int? {
        when {
            this.orientation == Orientation.VERTICAL && other.orientation == Orientation.VERTICAL -> {
                if (other.start.first in start.first..end.first || other.start.second in start.second..end.second) {

                }
            }

            this.orientation == Orientation.HORIZONTAL && other.orientation == Orientation.HORIZONTAL -> {}
            this.orientation == Orientation.VERTICAL && other.orientation == Orientation.HORIZONTAL -> {}
            this.orientation == Orientation.HORIZONTAL && other.orientation == Orientation.VERTICAL -> {}
        }
    }
}

enum class Orientation {
    VERTICAL, HORIZONTAL
}