import java.io.File
import kotlin.system.exitProcess

fun main() {

    File("input/input7.txt").forEachLine { line ->
        WireSet.addWire(line)
    }
    for (i in 0..WireSet.wires.size) {
        WireSet.wires.values.forEach{
            it.maybeConvertToLiteral()
        }
    }
    val finalAResult = WireSet.wires["a"]!!.value
    println(finalAResult)

    WireSet.wires.clear()
    File("input/input7.txt").forEachLine { line ->
        WireSet.addWire(line)
    }
    WireSet.wires["b"] = Wire(OpType.LITERAL,null, finalAResult, null, WireSet)
    for (i in 0..WireSet.wires.size) {
        WireSet.wires.values.forEach{
            it.maybeConvertToLiteral()
        }
    }
    println(WireSet.wires["a"]!!.value)

}

object WireSet {
    val wires: HashMap<String, Wire> = hashMapOf()

    fun addWire(inp: String) {
        val identifier = inp.split("->")[1].trim()
        val tokens = inp.split("->")[0].trim()
        val splitTokens = tokens.split(" ")
        if (tokens.contains("AND")) {
            wires[identifier] = Wire(OpType.AND, Pair(splitTokens[0], splitTokens[2]), null, null, this)
        } else if (tokens.contains("OR")) {
            wires[identifier] = Wire(OpType.OR, Pair(splitTokens[0], splitTokens[2]), null, null, this)
        } else if (tokens.contains("LSHIFT")) {
            wires[identifier] = Wire(OpType.LSHIFT, Pair(splitTokens[0], splitTokens[2]), null, null, this)
        } else if (tokens.contains("RSHIFT")) {
            wires[identifier] = Wire(OpType.RSHIFT, Pair(splitTokens[0], splitTokens[2]), null, null, this)
        } else if (tokens.contains("NOT")) {
            wires[identifier] = Wire(OpType.NOT,null, null, splitTokens[1], this)
        } else {
            wires[identifier] = Wire(OpType.DIRECT_DEP,null, null, splitTokens[0], this)
        }
    }

    fun getWire(identifier: String): Wire {
        val maybeWire = wires[identifier]
//        println(identifier)
        if (maybeWire == null) {
            return Wire(OpType.LITERAL, null, identifier.toInt(), null, this)
        } else {
            return maybeWire
        }
    }
}

class Wire(
    var type: OpType,
    var dependencies: Pair<String, String>?,
    var value: Int? = null,
    var directDep: String?,
    val wireSet: WireSet
) {
    fun maybeConvertToLiteral() {
        if (type == OpType.DIRECT_DEP // Convert this to a literal if its direct dep is literal
            && directDep != null
            && wireSet.getWire(directDep!!).type == OpType.LITERAL
        ) {
            value = wireSet.getWire(directDep!!).value
            directDep = null
            dependencies = null
            type = OpType.LITERAL
        }

        if (type == OpType.NOT // Convert this to a literal if its direct dep is literal
            && directDep != null
            && wireSet.getWire(directDep!!).type == OpType.LITERAL
        ) {
            value = wireSet.getWire(directDep!!).value!!.inv()
            directDep = null
            dependencies = null
            type = OpType.LITERAL
        }

        if (dependencies != null
            && wireSet.getWire(dependencies!!.first).type == OpType.LITERAL
            && wireSet.getWire(dependencies!!.second).type == OpType.LITERAL
        ) {
            val a = wireSet.getWire(dependencies!!.first).value!!
            val b = wireSet.getWire(dependencies!!.second).value!!
            value = when (type) {
                OpType.NOT -> throw Exception()
                OpType.DIRECT_DEP -> throw Exception()
                OpType.LITERAL -> throw Exception()
                OpType.AND -> a and b
                OpType.OR -> a or b
                OpType.LSHIFT -> a shl b
                OpType.RSHIFT -> a shr b
            }
            directDep = null
            dependencies = null
            type = OpType.LITERAL
        }
    }
}

enum class OpType {
    LITERAL, AND, OR, LSHIFT, RSHIFT, DIRECT_DEP, NOT
}

fun isNumber(s: String): Boolean {
    return try {
        s.toInt()
        true
    } catch (ex: NumberFormatException) {
        false
    }
}