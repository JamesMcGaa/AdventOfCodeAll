import java.io.File

fun main() {
  val cubes = hashSetOf<Triple<Int, Int, Int>>()
  File("input18.txt").forEachLine { line ->
    val ints = line.split(',').map { Integer.parseInt(it.trim()) }
    val triple = Triple(ints[0], ints[1], ints[2])
    cubes.add(triple)
  }

  println(calculateTotalSA(cubes))
  val xMin = cubes.minOf { cube -> cube.first }
  val xMax = cubes.maxOf { cube -> cube.first }
  val yMin = cubes.minOf { cube -> cube.second }
  val yMax = cubes.maxOf { cube -> cube.second }
  val zMin = cubes.minOf { cube -> cube.third }
  val zMax = cubes.maxOf { cube -> cube.third }
  println("${xMin}, ${xMax}, ${yMin}, ${yMax}, ${zMin}, ${zMax}, ")

  // Idea 1: BFS to find the connected components that cannot find an 'exit' then take the total
  // surface area of these pockets

  // Idea 2: Flood fill a corner of a cube that fully contains our cube + 1 in each dimension

  val exterior = hashSetOf<Triple<Int, Int, Int>>()
  val queue = mutableListOf(Triple(xMin - 1, yMin - 1, zMin - 1))
  while (queue.isNotEmpty()) {
    val cube = queue.removeLast()
    if (cube.first < xMin - 1 ||
            cube.second < yMin - 1 ||
            cube.third < zMin - 1 ||
            cube.first > xMax + 1 ||
            cube.second > yMax + 1 ||
            cube.third > zMax + 1 ||
            exterior.contains(cube) ||
            cubes.contains(cube)
    ) {
      continue
    }
    val adj =
        mutableListOf(
            Triple(cube.first - 1, cube.second, cube.third),
            Triple(cube.first + 1, cube.second, cube.third),
            Triple(cube.first, cube.second - 1, cube.third),
            Triple(cube.first, cube.second + 1, cube.third),
            Triple(cube.first, cube.second, cube.third - 1),
            Triple(cube.first, cube.second, cube.third + 1),
        )
    exterior.add(cube)
    for (a in adj) {
      queue.add(a)
    }
  }


  val interior = hashSetOf<Triple<Int, Int, Int>>()
  for (x in xMin..xMax) {
    for (y in yMin..yMax) {
      for (z in zMin..zMax) {
        val triple = Triple(x,y,z)
        if (!cubes.contains(triple) && !exterior.contains(triple)) {
          interior.add(triple)
        }
      }
    }
  }
    println(calculateTotalSA(interior))
    println(calculateTotalSA(cubes) - calculateTotalSA(interior))
}

fun calculateTotalSA(cubes: HashSet<Triple<Int, Int, Int>>): Int {
  val reconstructed = hashSetOf<Triple<Int, Int, Int>>()
  var counter = 0
  for (cube in cubes) {
    reconstructed.add(cube)
    counter += 6
    val others =
        mutableListOf(
            Triple(cube.first - 1, cube.second, cube.third),
            Triple(cube.first + 1, cube.second, cube.third),
            Triple(cube.first, cube.second - 1, cube.third),
            Triple(cube.first, cube.second + 1, cube.third),
            Triple(cube.first, cube.second, cube.third - 1),
            Triple(cube.first, cube.second, cube.third + 1),
        )
    for (other in others) {
      if (reconstructed.contains(other)) {
        counter -= 2
      }
    }
  }
  return counter
}
