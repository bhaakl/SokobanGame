package com.bhaakl.game.sokoban

import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class Controller : View.OnTouchListener {
    private val model: Model
    private var x1: Float?
    private var y1: Float?

    constructor(viewer: Viewer) {
        model = Model(viewer)
        x1 = null
        y1 = null
    }

    fun getModel(): Model {
        return model
    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        if (!model.getStateModel())
            return false
        var x2: Float? = null
        var y2: Float? = null
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    y1 = event.y
                }
                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    y2 = event.y
                }
            }
        }
        if (x2 != null && y2 != null)
            move(x2, y2)
        return true
    }

    private fun move(x2: Float, y2: Float) {
        var direction = ""
        val deltaX = x1!! - x2
        val deltaY = y1!! - y2
        direction = if (abs(deltaX) > abs(deltaY)) {
            if (deltaX < 0) "Right" else "Left"
        } else if (deltaY < 0) "Down" else "Up"
        if (direction.isEmpty()) return
        model.move(direction)
    }

}