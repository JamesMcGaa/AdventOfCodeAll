import java.io.File

fun main(args: Array<String>) {
    val board = mutableListOf<MutableList<Int>>()
    File("inputs/input8.txt").forEachLine { line ->
        board.add(line.split("").filter { ch -> ch.isNotBlank() }.map{ ch -> Integer.parseInt(ch)}.toMutableList())
    }
    var visibleCount = 0
    var maxScenicScore = 0
    val m = board.size
    val n = board[0].size

    for (r in 0 until m) {
        for (c in 0 until n) {
            val parallels = mutableListOf<MutableList<Int>>() // Should be sorted closest -> border elements
            val parallel = mutableListOf<Int>()


            for (rowVal in 0 until r) {
                parallel.add(board[rowVal][c])
            }
            parallel.reverse()
            parallels.add(ArrayList(parallel))
            parallel.clear()


            for (rowVal in r+1 until m) {
                parallel.add(board[rowVal][c])
            }
            parallels.add(ArrayList(parallel))
            parallel.clear()

            for (colVal in 0 until c) {
                parallel.add(board[r][colVal])
            }
            parallel.reverse()
            parallels.add(ArrayList(parallel))
            parallel.clear()


            for (colVal in c+1 until n) {
                parallel.add(board[r][colVal])
            }
            parallels.add(ArrayList(parallel))
            parallel.clear()

            run explicitAnnotationToSimulateBreak@{
                parallels.forEach { parallel ->
                    if (parallel.isEmpty() || board[r][c] > parallel.max()) {
                        visibleCount += 1
                        return@explicitAnnotationToSimulateBreak
                    }
                }
            }

            var scenicScore = 1
            parallels.forEach { parallel ->
                val copy = ArrayList(parallel)
                scenicScore *= incSubsequence(copy, board[r][c])
            }
            println("${r}, ${c}, ${scenicScore}")
            if (scenicScore > maxScenicScore) {
                maxScenicScore = scenicScore
            }


        }
    }

    println(visibleCount)
    println(maxScenicScore)
}

fun incSubsequence(nums: List<Int>, treeHeight: Int): Int {
    if (nums.isEmpty()) {
        return 1
    }

    var counter = 0

    nums.forEach {
        num ->
        counter += 1
        if (num >= treeHeight) {
           return counter
        }
    }

    return counter
}


