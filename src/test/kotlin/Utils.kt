fun <T> List<T>.dropIndex(index: Int): List<T> =
    slice(0 until index) + slice((index + 1) until size)

class Map<T>(
    val grid: List<List<T>>
) {
    fun size(): Pair<Int, Int> = Pair(grid[0].size, grid.size)

    fun get(x: Int, y: Int): T? =
        grid.getOrNull(y)?.getOrNull(x)
}

fun <T>List<List<T>>.transpose(): List<List<T>> {
    return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
}