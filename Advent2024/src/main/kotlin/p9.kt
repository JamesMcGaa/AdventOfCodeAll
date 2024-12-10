@file:Suppress("unused")

import java.io.File
import java.util.*

const val FREE_SPACE = -1

fun main() {
    partA()
    partB()
}

/**
 * This also should be optimized with the Block strategy, keeping the two pointer system
 * and making a way to chain along the free blocks (linkedlist?) at the lower e
 */
fun partA() {
    val input = File("inputs/input9.txt").readLines()[0].map { it.toString().toInt() }
    val disk = mutableListOf<Int>()
    var currentInputIdx = 0
    var currentId = 0

    while (currentInputIdx in input.indices) {
        val filesize = input[currentInputIdx]
        val freeSpaceSize = input.getOrNull(currentInputIdx + 1)?.toInt() ?: 0
        repeat(filesize) {
            disk.add(currentId)
        }
        repeat(freeSpaceSize) {
            disk.add(FREE_SPACE)
        }

        currentId += 1
        currentInputIdx += 2
    }

    var lowestFreeSpace = disk.indexOf(FREE_SPACE)
    var highestNonFreeSpace: Int = disk.indices.last
    while (disk[highestNonFreeSpace] == FREE_SPACE) {
        highestNonFreeSpace--
    }

    while (lowestFreeSpace < highestNonFreeSpace) {
        Collections.swap(disk, lowestFreeSpace, highestNonFreeSpace)
        while (disk[highestNonFreeSpace] == FREE_SPACE) {
            highestNonFreeSpace--
        }
        while (disk[lowestFreeSpace] != FREE_SPACE) {
            lowestFreeSpace++
        }
    }

    val partA = disk.mapIndexed { position, id -> if (id != FREE_SPACE) position * id.toLong() else 0L }.sum()
    println("Part A: $partA")
}

class Block(
    var length: Int,
    val id: Int,
)


/**
 * An optimal way to do this would be a LinkedList with a HashMap with pointers to the
 * File elements for easy swaps and inserts here
 */
fun partB() {
    val input = File("inputs/input9.txt").readLines()[0].map { it.toString().toInt() }
    val disk = mutableListOf<Block>()
    var currentId = 0

    var isNonEmpty = true
    for (idx in input.indices) {
        val blocksize = input[idx]
        if (isNonEmpty) {
            disk.add(Block(blocksize, currentId))
            currentId++
        } else {
            disk.add(Block(blocksize, FREE_SPACE))
        }
        isNonEmpty = !isNonEmpty
    }

    idLoop@for (id in currentId - 1 downTo 0) {
        var blockIdx = -1
        for (idx in disk.indices.reversed()) {
            if (disk[idx].id == id) {
                blockIdx = idx
            }
        }
        assert(blockIdx != -1)

        searchForFree@ for (freeIdx in 0..<blockIdx) {
            val freeLen = disk[freeIdx].length
            val blockLen = disk[blockIdx].length
            if (disk[freeIdx].length >= blockLen && disk[freeIdx].id == FREE_SPACE) {
                if (freeLen == blockLen) {
                    Collections.swap(disk, blockIdx, freeIdx)
                } else {
                    Collections.swap(disk, blockIdx, freeIdx)
                    disk[blockIdx].length = blockLen // Empty space fills the size of the old block
                    disk.add(freeIdx+1, Block(freeLen - blockLen, FREE_SPACE)) // new free space
                }
                break@searchForFree
            }
        }
    }

    val flattenedDisk = mutableListOf<Int>()
    disk.forEach { block ->
        repeat(block.length) {
            flattenedDisk.add(block.id)
        }
    }
    val partB = flattenedDisk.mapIndexed { position, id -> if (id != FREE_SPACE) position * id.toLong() else 0L }.sum()
    println("Part B: $partB")
}

fun printDiskB(disk: List<Block>) {
    val flattenedDisk = mutableListOf<Int>()
    disk.forEach { block ->
        repeat(block.length) {
            flattenedDisk.add(block.id)
        }
    }
    println(flattenedDisk.joinToString("") { if (it == FREE_SPACE) "." else it.toString() })
}
