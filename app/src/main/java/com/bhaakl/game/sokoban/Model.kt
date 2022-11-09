package com.bhaakl.game.sokoban

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class Model {
    private val viewer: Viewer
    private var desktop: Array<IntArray>
    private val levels: Levels
    private var indexX = 0
    private var indexY = 0
    private var stateModel = true
    private var drawBluePrint: Boolean
    private var arrayOfIndexes: Array<IntArray>

    private var sDirection = ""

    constructor(viewer: Viewer) {
        this.viewer = viewer
        desktop = emptyArray()
        drawBluePrint = false
        levels = Levels()
        arrayOfIndexes = emptyArray()
        initialization()
    }

    private fun initialization() {
        levels.setFilesDir(viewer.assets)
        desktop = levels.nextLevel()
        if (desktop.isEmpty()) {
            stateModel = false
            return
        }
        var countOne = 0
        var countThree = 0
        var countFour = 0
        for (i in desktop.indices) {
            for (j in desktop[i].indices) {
                if (desktop[i][j] == 3) {
                    countThree = countThree + 1
                } else if (desktop[i][j] == 4) {
                    countFour = countFour + 1
                } else if (desktop[i][j] == 1) {
                    indexX = i
                    indexY = j
                    countOne = countOne + 1
                }
            }
        }

        if (countOne != 1 || (countThree != countFour) || countThree <= 0 || countFour <= 0) {
            stateModel = false
            return
        }

        arrayOfIndexes = Array(3) { IntArray(countFour) }
        var a = 0
        for (i in desktop.indices) {
            for (j in desktop.indices) {
                if (desktop[i][j] == 4) {
                    arrayOfIndexes[0][a] = i
                    arrayOfIndexes[1][a] = j
                    arrayOfIndexes[2][a] = 0
                    a = a + 1
                }
            }
        }
    }

    fun move(direction: String) {
        when (direction) {
            "Left" -> {
                moveLeft()
            }
            "Up" -> {
                moveUp()
            }
            "Right" -> {
                moveRight()
            }
            "Down" -> {
                moveDown()
            }
            else -> return
        }
        sDirection = direction
        check()
        viewer.update()
        won()
    }

    private fun won() {
        var won = true
        for (j in arrayOfIndexes[0].indices) {
            val x = arrayOfIndexes[0][j]
            val y = arrayOfIndexes[1][j]
            if (desktop[x][y] != -3) {
                won = false
                break
            }
        }
        if (won) {
            winMessage()
        }
    }

    private fun winMessage() {
        val inflater: LayoutInflater = LayoutInflater.from(viewer)
        val view: View = inflater.inflate(R.layout.feature_win, null)
        val titleView = TextView(viewer)
        titleView.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25F)
            text = resources.getText(R.string.level_completed)
            isAllCaps = true
            setTextColor(Color.GREEN)
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }
        val mBuilder = AlertDialog.Builder(viewer)
            .setCustomTitle(titleView)
            .setIcon(R.drawable.win)
            .setView(view)
            .setPositiveButton("Next Level", null)
            .setNegativeButton("play again", null)
            .setCancelable(false)
            .show()
        val mPosButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            setTextColor(Color.MAGENTA)
            setOnClickListener() {
                mBuilder.dismiss()
                initialization()
                viewer.update()
            }
        }
        val mNegButton = mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
            setTextColor(Color.RED)
            setOnClickListener() {
                mBuilder.dismiss()
                levels.backToLevel()
                initialization()
                viewer.update()
            }
        }
        val paramsLL = mPosButton.layoutParams as LinearLayout.LayoutParams
        paramsLL.weight = 10F
        mPosButton.layoutParams = paramsLL
        mNegButton.layoutParams = paramsLL
    }

    private fun check() {
        for (j in arrayOfIndexes[0].indices) {
            val x = arrayOfIndexes[0][j]
            val y = arrayOfIndexes[1][j]
            val enabled = arrayOfIndexes[2][j]
            if (desktop[x][y] == 0 && enabled == 0) {
                desktop[x][y] = 4
            } else if (desktop[x][y] == 0 && enabled == 1) {
                desktop[x][y] = -4
            }
        }
    }

    private fun moveLeft() {
        if (ifOutOfBoundsGrid(indexX, indexY - 1)) return
        if (desktop[indexX][indexY - 1] == 3 || desktop[indexX][indexY - 1] == -3) {
            if (desktop[indexX][indexY - 2] == 0) {
                desktop[indexX][indexY - 1] = 0
                desktop[indexX][indexY - 2] = 3
            } else if (desktop[indexX][indexY - 2] == 4 || desktop[indexX][indexY - 2] == -4) {
                desktop[indexX][indexY - 1] = 0
                desktop[indexX][indexY - 2] = -3
            }
        }
        if (desktop[indexX][indexY - 1] == 0 || desktop[indexX][indexY - 1] == 4
            || desktop[indexX][indexY - 1] == -4) {
            desktop[indexX][indexY] = 0
            indexY = indexY - 1
            desktop[indexX][indexY] = 1
        }
        if (desktop[indexX][indexY - 1] == -3 || desktop[indexX][indexY - 1] == 3) {
            for (k in arrayOfIndexes[0].indices) {
                if (arrayOfIndexes[0][k] == indexX && arrayOfIndexes[1][k] == indexY) {
                    arrayOfIndexes[2][k] = 1
                }
            }
        }
        if (desktop[indexX][indexY] == 1 && desktop[indexX][indexY - 1] == -3) {
            var isnotgoal = true
            for (k in arrayOfIndexes[0].indices) {
                if (arrayOfIndexes[0][k] == indexX && arrayOfIndexes[1][k] == indexY - 1)
                    isnotgoal = false
            }
            if (isnotgoal) desktop[indexX][indexY - 1] = 3
        }
    }

    private fun moveUp() {
        if (ifOutOfBoundsGrid(indexX - 1, indexY)) return
        if (desktop[indexX - 1][indexY] == 3 || desktop[indexX - 1][indexY] == -3) {
            if (desktop[indexX - 2][indexY] == 0) {
                desktop[indexX - 1][indexY] = 0
                desktop[indexX - 2][indexY] = 3
            } else if (desktop[indexX - 2][indexY] == 4 || desktop[indexX - 2][indexY] == -4) {
                desktop[indexX - 1][indexY] = 0
                desktop[indexX - 2][indexY] = -3
            }
        }
        if (desktop[indexX - 1][indexY] == 0 || desktop[indexX - 1][indexY] == 4
            || desktop[indexX - 1][indexY] == -4) {
            desktop[indexX][indexY] = 0
            indexX = indexX - 1
            desktop[indexX][indexY] = 1
        }
        if (desktop[indexX - 1][indexY] == -3 || desktop[indexX - 1][indexY] == 3) {
            for (k in arrayOfIndexes[0].indices) {
                if (arrayOfIndexes[0][k] == indexX && arrayOfIndexes[1][k] == indexY) {
                    arrayOfIndexes[2][k] = 1
                }
            }
        }
        if (desktop[indexX][indexY] == 1 && desktop[indexX - 1][indexY] == -3) {
            var isnotgoal = true
            for (k in arrayOfIndexes[0].indices) {
                if (arrayOfIndexes[0][k] == indexX - 1 && arrayOfIndexes[1][k] == indexY)
                    isnotgoal = false
            }
            if (isnotgoal) desktop[indexX - 1][indexY] = 3
        }
    }

    private fun moveRight() {
        if (ifOutOfBoundsGrid(indexX, indexY + 1)) return
        if (desktop[indexX][indexY + 1] == 3 || desktop[indexX][indexY + 1] == -3) {
            if (desktop[indexX][indexY + 2] == 0) {
                desktop[indexX][indexY + 1] = 0
                desktop[indexX][indexY + 2] = 3
            } else if (desktop[indexX][indexY + 2] == 4 || desktop[indexX][indexY + 2] == -4) {
                desktop[indexX][indexY + 1] = 0
                desktop[indexX][indexY + 2] = -3
            }
        }
        if (desktop[indexX][indexY + 1] == 0 || desktop[indexX][indexY + 1] == 4
            || desktop[indexX][indexY + 1] == -4) {
            desktop[indexX][indexY] = 0
            indexY = indexY + 1
            desktop[indexX][indexY] = 1
        }
        if (desktop[indexX][indexY + 1] == -3 || desktop[indexX][indexY + 1] == 3) {
            for (k in arrayOfIndexes[0].indices) {
                if (arrayOfIndexes[0][k] == indexX && arrayOfIndexes[1][k] == indexY) {
                    arrayOfIndexes[2][k] = 1
                }
            }
        }
        if (desktop[indexX][indexY] == 1 && desktop[indexX][indexY + 1] == -3) {
            var isnotgoal = true
            for (k in arrayOfIndexes[0].indices) {
                if (arrayOfIndexes[0][k] == indexX && arrayOfIndexes[1][k] == indexY + 1)
                    isnotgoal = false
            }
            if (isnotgoal) desktop[indexX][indexY + 1] = 3
        }
    }

    private fun moveDown() {
        if (ifOutOfBoundsGrid(indexX + 1, indexY)) return
        if (desktop[indexX + 1][indexY] == 3 || desktop[indexX + 1][indexY] == -3) {
            if (desktop[indexX + 2][indexY] == 0) {
                desktop[indexX + 1][indexY] = 0
                desktop[indexX + 2][indexY] = 3
            } else if (desktop[indexX + 2][indexY] == 4 || desktop[indexX + 2][indexY] == -4) {
                desktop[indexX + 1][indexY] = 0
                desktop[indexX + 2][indexY] = -3
            }
        }
        if (desktop[indexX + 1][indexY] == 0 || desktop[indexX + 1][indexY] == 4
            || desktop[indexX + 1][indexY] == -4) {
            desktop[indexX][indexY] = 0
            indexX = indexX + 1
            desktop[indexX][indexY] = 1
        }
        if (desktop[indexX + 1][indexY] == -3 || desktop[indexX + 1][indexY] == 3) {
            for (k in arrayOfIndexes[0].indices) {
                if (arrayOfIndexes[0][k] == indexX && arrayOfIndexes[1][k] == indexY) {
                    arrayOfIndexes[2][k] = 1
                }
            }
        }
        if (desktop[indexX][indexY] == 1 && desktop[indexX + 1][indexY] == -3) {
            var isnotgoal = true
            for (k in arrayOfIndexes[0].indices) {
                if (arrayOfIndexes[0][k] == indexX + 1 && arrayOfIndexes[1][k] == indexY)
                    isnotgoal = false
            }
            if (isnotgoal) desktop[indexX + 1][indexY] = 3
        }
    }

    private fun ifOutOfBoundsGrid(x: Int, y: Int): Boolean {
        var fl = false
        if (x < 0) {
            fl = true
        } else if (x > desktop.size) {
            fl = true
        } else if (y < 0) {
            fl = true
        } else if (y > desktop[0].size) {
            fl = true
        }
        return fl
    }

    fun getStateUri(): String {
        return sDirection
    }

    fun getDesktop(): Array<IntArray> {
        return desktop
    }

    fun getViewer(): Context {
        return viewer
    }

    fun getStateModel(): Boolean {
        return stateModel
    }

    fun getCurrentLevel(): Int {
        return levels.getLevel()
    }

    fun getDrawBluePrint(): Boolean {
        return drawBluePrint
    }
}