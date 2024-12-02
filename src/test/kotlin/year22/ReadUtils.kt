package year22

import java.io.File

fun readInput(name: String) = File("src/test/kotlin/year22/${name.substring(0, 5).lowercase()}", "$name.txt")
fun readInputAsLines(name: String) = readInput(name).readLines()