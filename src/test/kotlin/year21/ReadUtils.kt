package year21

import java.io.File

fun readInput(name: String) = File("src/test/kotlin/year21/${name.substring(0, 5).lowercase()}", "$name.txt").readLines()
fun readInputAsInts(name: String) = readInput(name).map { it.toInt() }