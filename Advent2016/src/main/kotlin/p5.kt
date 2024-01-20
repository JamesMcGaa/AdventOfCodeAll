import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    var counter = 0
    val input = "reyedfim"
    var password = ""
    while (password.length != 8) {
        val hash = md5(input + counter.toString())
        if (hash.substring(0,5) == "00000") {
            password += hash[5]
        }
        counter += 1
    }
    println(password)

    counter = -1
    val passwordB = MutableList<Char?>(8, { null })
    while (passwordB.filterNotNull().size != 8) {
        counter += 1
        val hash = md5(input + counter.toString())
        if (hash.substring(0,5) == "00000") {
            if (!hash[5].isDigit()) continue
            val pos = hash[5].toString().toInt()
            val ch = hash[6]
            if ((0..7).contains(pos) && passwordB[pos] == null) {
                passwordB[pos] = ch
            }
        }
    }
    println(passwordB.joinToString(""))
}


fun md5(input:String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}
