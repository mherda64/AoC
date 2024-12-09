package year24.day09

import DayTemplate
import io.kotest.matchers.shouldBe

class Day09 : DayTemplate() {

    init {
        "Should solve the test input" {
            firstPart(testInputPart1.first()) shouldBe 1928
            secondPart(testInputPart2.first()) shouldBe 2858
        }

        "Should solve the proper input" {
            println(firstPart(input.first()))
            println(secondPart(input.first()))
        }
    }

    private fun firstPart(input: String): Long {
        val diskMap = prepareDiskMap(input)
        val defragmented = defragment(diskMap)
        return defragmented
            .filter { it is Block.Data }
            .mapIndexed { index, block ->
                index * (block as Block.Data).id.toLong()
            }.sum()
    }

    private fun secondPart(input: String): Long {
        val diskMap = prepareDiskMap(input)
        val biggestId = diskMap.filter { it is Block.Data }.maxOf { (it as Block.Data).id }
        val defragmented = defragmentWholeFiles(diskMap, biggestId)
        return defragmented
            .mapIndexed { index, block ->
                when (block) {
                    is Block.Data -> index * block.id.toLong()
                    Block.FreeSpace -> null
                }
            }
            .filterNotNull()
            .sum()
    }

    sealed class Block {
        data class Data(val id: Int) : Block()
        object FreeSpace : Block()
    }

    private fun List<Block>.isDefragmented(): Boolean {
        var freeSpaceFound = false
        forEach {
            when (it) {
                is Block.Data -> if (freeSpaceFound) return false
                Block.FreeSpace -> freeSpaceFound = true
            }
        }
        return true
    }

    private fun List<Block>.findFreeBlockIndex(fileSize: Int): Int? {
        indices.forEach {
            if (it + fileSize <= size - 1 && subList(it, it + fileSize) == List(fileSize) { Block.FreeSpace })
                return it
        }

        return null
    }

    private tailrec fun defragmentWholeFiles(diskMap: List<Block>, idToMove: Int): List<Block> {
        if (idToMove < 0) {
            return diskMap
        }
        val fileSize = diskMap.count { it is Block.Data && it.id == idToMove }
        val fileIndex = diskMap.indexOfFirst { it is Block.Data && it.id == idToMove }
        val freeBlockIndex = diskMap.findFreeBlockIndex(fileSize)
        return if (freeBlockIndex != null && freeBlockIndex < fileIndex) {
            val sublist = diskMap.subList(0, freeBlockIndex) +
                    diskMap.subList(fileIndex, fileIndex + fileSize) +
                    diskMap.subList(freeBlockIndex + fileSize, fileIndex) +
                    diskMap.subList(freeBlockIndex, freeBlockIndex + fileSize)
            defragmentWholeFiles(
                if (fileIndex + fileSize > diskMap.size - 1)
                    sublist
                else
                    sublist + diskMap.subList(fileIndex + fileSize, diskMap.size),
                idToMove - 1
            )
        } else {
            defragmentWholeFiles(
                diskMap,
                idToMove - 1
            )
        }
    }

    private tailrec fun defragment(diskMap: List<Block>): List<Block> {
        if (diskMap.isDefragmented()) {
            return diskMap
        }
        val firstFreeBlockIndex = diskMap.indexOfFirst { it == Block.FreeSpace }
        val lastUnusedBlockIndex = diskMap.indexOfLast { it is Block.Data }

        return defragment(
            diskMap.subList(0, firstFreeBlockIndex) +
                    diskMap[lastUnusedBlockIndex] +
                    diskMap.subList(firstFreeBlockIndex + 1, lastUnusedBlockIndex) +
                    listOf(Block.FreeSpace) +
                    diskMap.subList(lastUnusedBlockIndex + 1, diskMap.size)
        )
    }

    private fun prepareDiskMap(input: String): List<Block> {
        var idCounter = 0

        return input.mapIndexed { index, ch ->
            if (index % 2 == 0) {
                val id = idCounter++
                (0..<ch.digitToInt()).map { Block.Data(id) }
            } else {
                (0..<ch.digitToInt()).map { Block.FreeSpace }
            }
        }.flatten()
    }
}