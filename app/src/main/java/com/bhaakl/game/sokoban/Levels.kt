package com.bhaakl.game.sokoban

import android.content.res.AssetManager
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class Levels {
    private var level = 0
    private val prefixFileName: String
    private val endFileName: String
    private var assetManager: AssetManager? = null

    constructor() {
        level = 5
        prefixFileName = "levels/level"
        endFileName = ".sok"
    }

    fun nextLevel(): Array<IntArray> {
        val desktop: Array<IntArray>
        when (level) {
            1 -> desktop = getFirstLevel()
            2 -> desktop = getSecondLevel()
            3 -> desktop = getThirdLevel()
            4, 5, 6 -> desktop = loadLevelFromFile(prefixFileName + level + endFileName)
            else -> {
                level = 1
                desktop = getFirstLevel()
            }
        }
        level = level + 1
        return desktop
    }

    private fun loadLevelFromFile(fileName: String): Array<IntArray> {
        var ins: InputStream? = null
        var array: CharArray?
        return try {
            setFilesDir(assetManager)
            ins = assetManager?.open(fileName)
            val size = ins?.available()?.plus(1) ?: 0
            array = CharArray(size)
            var unicode: Int
            var index = 0
            if (ins != null) {
                while (ins.read().also { unicode = it } != -1) {
                    val symbol = unicode.toChar()
                    if (symbol in '0'..'5') {
                        array[index] = symbol
                        index = index + 1
                    } else if (symbol == '\n') {
                        array[index] = 'A'
                        index = index + 1
                    }
                }
            }
            if (array[index - 1] != 'A' && array[index] != '\n') {
                array[index] = 'A'
                index = index + 1
            }
            val text = String(array, 0, index)
            convert(text)
        } catch (fne: FileNotFoundException) {
            fne.printStackTrace()
            return emptyArray()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
            return emptyArray()
        } finally {
            array = null
            try {
                ins?.close()
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
    }

    private fun getFirstLevel(): Array<IntArray> {
        val array = arrayOf(
            intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            intArrayOf(2, 0, 1, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 2, 2, 0, 0, 0, 0, 3, 4, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
        )

        return array
    }

    private fun getSecondLevel(): Array<IntArray> {
        val array = arrayOf(
            intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            intArrayOf(2, 0, 0, 4, 4, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 3, 3, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 1, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
        )
        return array
    }

    private fun getThirdLevel(): Array<IntArray> {
        val array = arrayOf(
            intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            intArrayOf(2, 2, 2, 0, 0, 0, 2, 0, 0, 2),
            intArrayOf(2, 0, 1, 0, 0, 0, 2, 0, 5, 5),
            intArrayOf(2, 2, 2, 0, 3, 4, 2, 0, 5, 5),
            intArrayOf(2, 4, 2, 2, 3, 0, 0, 0, 5, 5),
            intArrayOf(2, 0, 2, 0, 4, 0, 2, 2, 5, 5),
            intArrayOf(2, 3, 0, 0, 3, 3, 4, 2, 0, 2),
            intArrayOf(2, 0, 0, 0, 4, 0, 0, 2, 0, 2),
            intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
        )
        return array
    }

    private fun convert(text: String): Array<IntArray> {
        val array = try {
            isCorrectArray(text)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            emptyArray()
        }
        return array
    }

    private fun isCorrectArray(token: String): Array<IntArray> {
        var fl = false
        val setCount = mutableSetOf<Int>()
        var row = 0
        var column = 0
        var count = 0
        for (i in token.indices) {
            val symbol = token[i]
            if (symbol == 'A') {
                row = row + 1
                setCount.add(count)
                count = 0
            }
            if (symbol == token.last()) continue
            count = count + 1
        }
        var array: Array<IntArray> = emptyArray()
        if (setCount.isEmpty() || setCount.size > 1) {
            fl = false
        } else if (setCount.size == 1) {
            fl = true
            for (i in setCount)
                column = i
            array = Array(row) { IntArray(column) }
            var i = 0
            var j = 0
            for (symbol in token) {
                if (symbol == 'A') {
                    i = i + 1
                    j = 0
                } else {
                    array[i][j] = ("" + symbol).toInt()
                    j = j + 1
                }
            }
        }

        if (!fl && array.isEmpty()) {
            throw Exception("Parsing error(When loading from a file)!")
        }
        return array
    }

    fun getLevel(): Int {
        return level - 1
    }

    fun backToLevel() {
        level = level - 1
    }

    fun setFilesDir(assetManager: AssetManager?) {
        this.assetManager = assetManager
    }
}