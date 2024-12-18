@file:Suppress("PropertyName")

import java.io.File
import Utils.Coord

var hasPrintedPartA = false

fun main() {
    val input =
        File("inputs/input23.txt").readLines()[0].split(",").mapIndexed { idx, va -> idx.toLong() to va.toLong() }
            .toMap()
            .toMutableMap()

    var computers = mutableListOf<IntcodeP23>()
    val NAT = Coord(0, 0)
    for (i in 0..49) {
        computers.add(IntcodeP23(input.toMutableMap(), computers, NAT).apply {
            inputBuffer.add(i.toLong())
        })
    }

    var natPrev = -1
    var prevState = ""
    while (computers.isNotEmpty()) {
        var newComputers = mutableListOf<IntcodeP23>()
        forLoop@ for (computer in computers) {
            computer.seen.clear()
            val res = computer.executeIntcode()
            if (res == Long.MAX_VALUE) { // Suspended
                newComputers.add(computer)
                continue@forLoop
            }
        }
        val newState = newComputers.map {it.toStr()}.toString()
        if (newState == prevState) {
            println("NAT: $NAT")
            if (NAT.y == natPrev) {
                println("Part B: ${NAT.y}")
                return
            } else {
                natPrev = NAT.y
            }
            computers[0].inputBuffer.add(NAT.x.toLong())
            computers[0].inputBuffer.add(NAT.y.toLong())
        }

        prevState = newState
        computers = newComputers
    }
}

class IntcodeP23(instructions: MutableMap<Long, Long>, val allComputers: MutableList<IntcodeP23>, val NAT: Coord) : IntcodeP15Base(instructions) {
    var inputBuffer = mutableListOf<Long>()
    var inputCtr = 0
    var outputBuffer = mutableListOf<Long>()
    var seen = mutableSetOf<String>()

    fun toStr(): String {
        return "$inputBuffer:$inputCtr:$executionPointer:$instructions"
    }

    fun receivePacket(x: Long, y: Long) {
        inputBuffer.add(x)
        inputBuffer.add(y)
        seen.clear()
    }

    override fun preExecuteHook(): Boolean {
        super.preExecuteHook()
        if (toStr() in seen) {
            return false
        } else {
            seen.add(toStr())
            return true
        }
    }

    override fun getInput(): Long {
        if (inputCtr in inputBuffer.indices) {
            val ret = inputBuffer[inputCtr++]
            if (inputCtr == 2) {
                inputCtr = 0
                inputBuffer.removeFirst()
                inputBuffer.removeFirst()
            }
            return ret
        } else {
            return -1L
        }
    }

    override fun outputHook() {
        super.outputHook()
        outputBuffer.add(lastOutput)
        if (outputBuffer.size == 3) {
            println(outputBuffer)
            if (outputBuffer[0].toInt() == 255) {
                if (!hasPrintedPartA) {
                    println("Part A: ${outputBuffer[2].toInt()}")
                    hasPrintedPartA = true
                }
                NAT.x = outputBuffer[1].toInt()
                NAT.y = outputBuffer[2].toInt()
            }
            else {
                allComputers[outputBuffer[0].toInt()].receivePacket(outputBuffer[1], outputBuffer[2])
            }
            outputBuffer.clear()
        }
    }

    override fun resetHook() {
        super.resetHook()
        inputCtr = 0
        inputBuffer.clear()
        outputBuffer.clear()
    }
}