import java.io.File

fun main() {
    val ips = File("inputs/input7.txt").readLines().map {
        val regulars = mutableListOf<String>()
        val hypernets = mutableListOf<String>()
        var current = ""

        for (ch in it) {
            if (ch == '[') {
                regulars.add(current)
                current = ""
            }
            else if (ch == ']') {
                hypernets.add(current)
                current = ""
            }
            else {
                current += ch
            }
        }

        if (!current.isBlank()) {
            regulars.add(current)
        }

        IPv7(
            regulars, hypernets
        )
    }
    println(ips.filter { it.isValid }.size)

    println(ips.filter { it.aba() }.size)
}

data class IPv7(
    val regularSequences: List<String>,
    val hypernetSequences: List<String>
) {
    val isValid = regularSequences.any { abba(it) } && !hypernetSequences.any { abba(it) }

    fun aba(): Boolean {
        for (a in 'a'..'z') {
            for (b in 'a'..'z') {
                if (a != b &&
                    regularSequences.any {it.contains(a.toString() + b.toString() + a.toString())} &&
                    hypernetSequences.any {it.contains(b.toString() + a.toString() + b.toString())}
                    ) {
                    return true
                }
            }
        }
        return false
    }
}

fun abba(input: String): Boolean {
    for (a in 'a'..'z') {
        for (b in 'a'..'z') {
            if (a != b && input.contains(a.toString() + b.toString() + b.toString() + a.toString())) {
                return true
            }
        }
    }
    return false
}