package com.bhaakl.game.sokoban

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class Viewer : AppCompatActivity {
    private var model: Model?
    private var canvas: CanvasSokoban?
    private var controller: Controller?
    private var mp: MediaPlayer? = null

    constructor() {
        controller = null
        model = null
        canvas = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = Controller(this)
        model = controller!!.getModel()
        canvas = CanvasSokoban(this, model!!)
        setContentView(canvas)
        canvas!!.setOnTouchListener(controller)
        mp?.isLooping = true
        runMusic()
    }

    fun update() {
        canvas?.invalidate()
    }

    private fun runMusic() {
        mp = MediaPlayer.create(this, R.raw.sokoban_perfect_misterio)
        mp?.start()
    }

    private fun releaseMP() {
        if (mp != null) {
            try {
                mp?.release()
                mp = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMP()
    }
}