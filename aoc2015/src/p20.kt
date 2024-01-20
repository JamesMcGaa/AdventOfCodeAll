fun main() {
    val target = 33100000L
//    var counter = 1
//    while (true) {
//        println(counter)
//        counter *= 2
//        var presents = 0L
//        for (i in 1..counter) {
//            if (counter % i == 0) {
//                presents += 10 * i
//            }
//        }
//        if (presents >= target) {
//            println("${counter}, ${presents}")
//            break
//        }
//    }

    val housesA = mutableListOf<Long>()
    val housesB = mutableListOf<Long>()
    for (i in 0..3000000) {
        housesA.add(0)
        housesB.add(0)
    }

    for (divisor in 1..3000000) {
        var multiple = 1
        while (multiple*divisor <= 3000000) {
            if (multiple <= 50) {
                housesB[multiple*divisor] += divisor * 11L
            }
            housesA[multiple*divisor] += divisor * 10L
            multiple += 1
        }
    }

    for (index in housesA.indices) {
        if (housesA[index] >= target) {
            println(index)
            break
        }
    }

    for (index in housesB.indices) {
        if (housesB[index] >= target) {
            println(index)
            break
        }
    }

}
//26000
//33100000
//45675864
//fun primes(n: Int) = (2..n).filter{ num -> (2 until num).none{ num % it == 0 } }

/**
 * Key insight here is going for each house for each divisor wastes a bunch of divisor checks
 *
 * We should do each divisor, then iterate only on its multiples
 *
 * We can set an arbitrary upper limit and hope our answer is low enough
 */