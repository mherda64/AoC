fun <T> List<T>.dropIndex(index: Int): List<T> =
    slice(0 until index) + slice((index + 1) until size)