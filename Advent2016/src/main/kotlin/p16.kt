/**
 * Apparently you can checksumify this in one go with iterative XNOR using some
 * math / thought
 */
fun main() {
    val INPUT = "10001001100000001"
//    val DISK_FILL_SIZE = 272
    val DISK_FILL_SIZE = 35651584
    var a = INPUT
    while (a.length < DISK_FILL_SIZE) {
        var b = a
        b = b.reversed()
        b = b.map { if (it == '1') '0' else '1' }.joinToString("")
        a = a + "0" + b
    }
    var disk = a.substring(0, DISK_FILL_SIZE)
    while(disk.length % 2 == 0) {
        disk = toChecksum(disk)
    }
    println(disk)
}

fun toChecksum(inp: String): String {
    var res = mutableListOf<Char>()
    // !!! It is super important to use a list rather than string concat which is N^2
    for (i in 0..inp.length / 2 - 1) {
        res.add(if (inp[2 * i] == inp[2 * i + 1]) '1' else '0')
    }
    return res.joinToString("")
}