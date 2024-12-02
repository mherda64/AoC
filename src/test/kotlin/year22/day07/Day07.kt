package year22.day07

import year22.readInputAsLines

const val dayNumber = "07"

data class Dir(
    val name: String,
    var size: Int,
    val dirs: MutableList<Dir>,
    val parentDir: Dir?
) {
    override fun toString(): String {
        return "Dir(name='$name', size=$size, dirs=${dirs.map { it.name }})"
    }
}

fun getDirSize(dir: Dir): Int {
    return dir.size + dir.dirs.sumOf { getDirSize(it) }
}

fun getDirsSmallerThan(dir: Dir, smallerThan: Int): List<Dir> {
    if (getDirSize(dir) < smallerThan)
        return listOf(dir)
    else return dir.dirs.map { getDirsSmallerThan(it, smallerThan) }.flatten()
}

fun getAllDirsInHierarchy(dir: Dir): List<Dir> {
    return listOf(dir) + dir.dirs.map { getAllDirsInHierarchy(it) }.flatten()
}

fun main() {

    fun common(input: List<String>): Dir {
        val first = Dir("/", 0, mutableListOf(), null)
        var currentDir = first
        for (line in input) {
            when {
                line.matches("\\$ cd (.+)".toRegex()) -> {
                    val (dirName) = "\\$ cd (.+)".toRegex().find(line)!!.destructured
                    if (dirName == "..") {
                        currentDir = currentDir.parentDir!!
                    } else if (currentDir.dirs.any { it.name == dirName }) {
                        currentDir = currentDir.dirs.find { it.name == dirName }!!
                    }
                }

                line.matches("dir (.+)".toRegex()) -> {
                    val (dirName) = "dir (.+)".toRegex().find(line)!!.destructured
                    if (currentDir.dirs.none { it.name == dirName }) {
                        val newDir = Dir(dirName, 0, mutableListOf(), currentDir)
                        currentDir.dirs.add(newDir)
                    }
                }

                else -> {
                    val (size) = "(\\d+) .+".toRegex().find(line)!!.destructured
                    currentDir.size += size.toInt()
                }
            }
        }
        return first
    }

    fun part1(input: List<String>): Int {
        val first = common(input)
        val dirs = getDirsSmallerThan(first, 100000)
        val allDirsInHierarchy = dirs.map { getAllDirsInHierarchy(it) }.flatten()
        return allDirsInHierarchy.sumOf { getDirSize(it) }
    }

    fun part2(input: List<String>): Int {
        val first = common(input)
        val usedSpace = getDirSize(first)
        val dirs = getAllDirsInHierarchy(first)
        return dirs.map { getDirSize(it) }
            .filter { 70000000 - usedSpace + it >= 30000000  }
            .minOf { it }
    }


    fun getInput(filename: String): List<String> =
        readInputAsLines(filename)
            .filterIndexed { index, line -> index != 0 && !line.matches("\\$ ls".toRegex()) }

    val testInput = getInput("Day${dayNumber}_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = getInput("Day${dayNumber}")
    println(part1(input))
    println(part2(input))
}