import java.io.File
import kotlin.Exception
import kotlin.math.absoluteValue

fun main() {
    val beacons = mutableListOf<Pair<Int, Int>>()
    val sensors = mutableListOf<Triple<Int, Int, Int>>()
    val covered: HashSet<Pair<Int,Int>> = hashSetOf()
    val plusOneFrontier: HashMap<Pair<Int,Int>, Int> = hashMapOf()

    val LIMIT = 4000000
    val lines = File("inputs/input15.txt").forEachLine {
        line ->
        val split = line.split(":")
        val sensorInput = split[0]
        val beaconInput = split[1]

        val xBeacon = beaconInput.split(",")[0]
        val xBeaconStart = xBeacon.indexOf("=") + 1
        val xBeaconVal = Integer.parseInt(xBeacon.substring(xBeaconStart))

        val yBeacon = beaconInput.split(",")[1]
        val yBeaconVal = Integer.parseInt(yBeacon.substring(3))
        val beacon = Pair(xBeaconVal, yBeaconVal)
//        beacons.add(beacon)


        val xSensor = sensorInput.split(",")[0]
        val xStart = xSensor.indexOf("=") + 1
        val xSensorVal = Integer.parseInt(xSensor.substring(xStart))

        val ySensor = sensorInput.split(",")[1]
        val ySensorVal = Integer.parseInt(ySensor.substring(3))

        val beaconDist = (xSensorVal-xBeaconVal).absoluteValue + (ySensorVal-yBeaconVal).absoluteValue
        val yDist = (ySensorVal - 2000000).absoluteValue
        val leftoverBudgetForX = beaconDist - yDist
        val sensor = Triple(xSensorVal, ySensorVal, beaconDist)

        sensors.add(sensor)



        // A, mark covered points
//        if (leftoverBudgetForX >= 0) {
//            for (i in xSensorVal - leftoverBudgetForX .. xSensorVal + leftoverBudgetForX) {
//                val potentialPair = Pair(i,2000000)
//                if (potentialPair != beacon && Triple(potentialPair.first, potentialPair.second, beaconDist) != sensor && !covered.contains(potentialPair)) {
//                    covered.add(potentialPair)
//                }
//            }
//        }

        // B
        val radiusPlusOne = beaconDist + 1
        for (i in xSensorVal - radiusPlusOne .. xSensorVal + radiusPlusOne) {
            if (i in 0 .. LIMIT) {
                val pairOne = Pair(i, ySensorVal + (xSensorVal - i + radiusPlusOne))
                val pairTwo = Pair(i, ySensorVal - (xSensorVal - i + radiusPlusOne))
                if (pairOne.first in 0 .. LIMIT && pairOne.second in 0 .. LIMIT) {
                    plusOneFrontier[pairOne] = 1 + (plusOneFrontier[pairOne] ?: 0)
                }
                if (pairTwo.first in 0 .. LIMIT && pairTwo.second in 0 .. LIMIT) {
                    plusOneFrontier[pairTwo] = 1 + (plusOneFrontier[pairTwo] ?: 0)
                }
            }
        }
        println(plusOneFrontier.size)


    }
    println(plusOneFrontier.filterValues { value -> value >= 2 }.size)
    plusOneFrontier.filterValues { value -> value >= 2 }.forEach { pair, freq ->
        var good = true
        sensors.forEach {
            sensor ->
            val dist = (sensor.first - pair.first).absoluteValue + (sensor.second - pair.second).absoluteValue
            if (dist <= sensor.third) {
                good = false
            }
        }
        if (good) {
            println("FOUND")
            println(pair.first.toLong() * LIMIT.toLong() + pair.second.toLong())
        }
    }
    println(covered.size)

}

 
