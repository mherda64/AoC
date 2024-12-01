import io.kotest.core.spec.style.FreeSpec
import java.io.File

open class DayTemplate(
    private val dayNo: String,
    secondTestPartTheSame: Boolean = true
) : FreeSpec() {
    protected val testInputPart1 = asLines("test_part1")
    protected val testInputPart2 = asLines("test_part${if (secondTestPartTheSame) "1" else "2"}")
    protected val input = asLines("input")

    private fun readInput(name: String) = File("src/test/kotlin/year24/day$dayNo", "$name.txt")
    private fun asLines(name: String) = readInput(name).readLines()
}