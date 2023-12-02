import java.io.File

fun readInput(name: String) = File("src/test/kotlin/${name.substring(0, 5).lowercase()}", "$name.txt")
fun readInputAsLines(name: String) = readInput(name).readLines()
fun readInputAsInts(name: String) = readInputAsLines(name).map { it.toInt() }