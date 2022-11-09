package com.bhaakl.game.sokoban

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.view.View
import android.widget.ImageView

class CanvasSokoban : View {
    private val model: Model
    private val paint: Paint
    private val textPaint: Paint
    private val rect: Rect
    private val uriWin: Uri
    private var uriHero: Uri
    private val uriWall: Uri
    private var uriBox: Uri
    private val uriBoxGround: Uri
    private val uriBoxGroundGoal: Uri
    private val uriGoal: Uri
    private var uriGoalBox: Uri
    private val uriError: Uri
    private val uriHeroRight: Uri
    private val uriHeroLeft: Uri
    private val uriHeroUp: Uri
    private val uriHeroDown: Uri
    private val uriHeroRightGoal: Uri
    private val uriHeroLeftGoal: Uri
    private val uriHeroUPGoal: Uri
    private val uriHeroDownGoal: Uri

    constructor(viewer: Viewer, model: Model) : super(viewer) {
        this.model = model
        setBackgroundColor(Color.BLACK)
        paint = Paint()
        textPaint = Paint().apply {
            color = Color.MAGENTA
            isAntiAlias = true
        }
        rect = Rect()
        // base icons
        uriHero = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.hero}")
        uriWall = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.wall}")
        uriBox = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.box}")
        uriBox = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.box}")
        uriGoal = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.box_goal}")
        uriGoalBox = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.box_goal_v2}")
        uriBoxGround = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.ground}")
        uriBoxGroundGoal =
            Uri.parse("android.resource://${viewer.packageName}/${R.drawable.ground_goal}")
        uriWin = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.win}")
        uriError = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.error}")
        // direct icons
        uriHeroRight =
            Uri.parse("android.resource://${viewer.packageName}/${R.drawable.hero_right}")
        uriHeroLeft = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.hero_left}")
        uriHeroUp = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.hero_up}")
        uriHeroDown = Uri.parse("android.resource://${viewer.packageName}/${R.drawable.hero_down}")
        uriHeroRightGoal =
            Uri.parse("android.resource://${viewer.packageName}/${R.drawable.hero_right_goal}")
        uriHeroLeftGoal =
            Uri.parse("android.resource://${viewer.packageName}/${R.drawable.hero_left_goal}")
        uriHeroUPGoal =
            Uri.parse("android.resource://${viewer.packageName}/${R.drawable.hero_up_goal}")
        uriHeroDownGoal =
            Uri.parse("android.resource://${viewer.packageName}/${R.drawable.hero_down_goal}")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawARGB(30, 102, 204, 255)
        if (model.getStateModel()) {
            val desktop = model.getDesktop()
            drawDesktop(canvas, desktop)
        } else {
            drawErrorGame(canvas)
        }
    }

    private fun drawErrorGame(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        val imageError = ImageView(model.getViewer())
        imageError.setImageURI(uriError)
        val imageHeight = imageError.drawable.intrinsicHeight
        val imageWidth = imageError.drawable.intrinsicWidth
        imageError.drawable.bounds.set(
            (width / 2) - imageWidth, (height / 2) - imageHeight, (width / 2) + imageWidth,
            (height / 2) + imageHeight
        )
        imageError.scaleType = ImageView.ScaleType.FIT_END
        imageError.drawable.draw(canvas)
        textPaint.textSize = resources.displayMetrics.scaledDensity * 37
        val xOffset = textPaint.measureText("INITIALIZATION ERROR!") * 0.5f // ширина текста
        val textX = (width / 2) - xOffset
        val yOffset = textPaint.fontMetrics.ascent * -0.4f // высота
        val textY = (height / 2) - yOffset
        canvas.drawText(
            "INITIALIZATION ERROR!",
            textX,
            textY,
            textPaint
        )
    }

    private fun drawDesktop(canvas: Canvas, desktop: Array<IntArray>) {
        if (model.getDrawBluePrint()) {
            drawBluePrintContentGame(canvas, desktop)
        } else {
            drawLevelMsg(canvas)
            drawContentGame(canvas, desktop)
        }
    }

    private fun drawContentGame(canvas: Canvas, desktop: Array<IntArray>) {
        //width, height = (widthContentGame / 10)
        val subWidth = if (width < height) 0 else (width - height) - ((width - (width - height)) / 10)
        val widthContentGame = width - subWidth
        val heightContentGame = 9 * widthContentGame / 10
        val x = (width - widthContentGame) / 2
        val textLvlHeight = if (width < height) textPaint.fontMetrics.ascent * -0.4f else 0
        val y = (height - heightContentGame) / 2 - textLvlHeight.toInt()
        val boxWidth = widthContentGame / 10 + x
        var boxHeight = y + widthContentGame / 10
        rect.left = x
        rect.top = y
        rect.right = boxWidth
        rect.bottom = boxHeight
        paint.strokeWidth = 8F

        for (i in desktop.indices) {
            for (j in desktop[i].indices) {
                if (desktop[i][j] == 1) {
                    val imageGamer = ImageView(model.getViewer())
                    imageGamer.setImageURI(setUrisStateHero(model.getStateUri()))
                    imageGamer.drawable.bounds.set(rect)
                    imageGamer.drawable.draw(canvas)
                } else if (desktop[i][j] == 2) {
                    val imageWall = ImageView(model.getViewer())
                    imageWall.setImageURI(uriWall)
                    imageWall.drawable.bounds.set(rect)
                    imageWall.drawable.draw(canvas)
                } else if (desktop[i][j] == 3) {
                    val imageBox = ImageView(model.getViewer())
                    imageBox.setImageURI(uriBox)
                    imageBox.drawable.bounds.set(rect)
                    imageBox.drawable.draw(canvas)
                } else if (desktop[i][j] == -3) {
                    val imageBox = ImageView(model.getViewer())
                    imageBox.setImageURI(uriGoalBox)
                    imageBox.drawable.bounds.set(rect)
                    imageBox.drawable.draw(canvas)
                } else if (desktop[i][j] == 4) {
                    val imageGoal = ImageView(model.getViewer())
                    imageGoal.setImageURI(uriGoal)
                    imageGoal.drawable.bounds.set(rect)
                    imageGoal.drawable.draw(canvas)
                } else if (desktop[i][j] == -4) {
                    val imageGoal = ImageView(model.getViewer())
                    imageGoal.setImageURI(uriBoxGroundGoal)
                    imageGoal.drawable.bounds.set(rect)
                    imageGoal.drawable.draw(canvas)
                } else if (desktop[i][j] == 0) {
                    val imageGround = ImageView(model.getViewer())
                    imageGround.setImageURI(uriBoxGround)
                    imageGround.drawable.bounds.set(rect)
                    imageGround.drawable.draw(canvas)
                }
                rect.offset(boxWidth - x, 0) // horizontally offset
            }
            // vertically offset
            rect.offsetTo(x, boxHeight)
            boxHeight = boxHeight + widthContentGame / 10
        }

    }

    private fun setUrisStateHero(s: String): Uri {
        return when (s) {
            "Left" -> uriHeroLeft
            "Right" -> uriHeroRight
            "Up" -> uriHeroUp
            "Down" -> uriHeroDown
            else -> uriHero
        }
    }

    private fun drawLevelMsg(canvas: Canvas) {
        textPaint.textSize = resources.displayMetrics.scaledDensity * 40
        val xOffset =
            textPaint.measureText("Level: ${model.getCurrentLevel()}") * 0.5f // ширина текста
        val textX = (width / 2) - xOffset
        if (width > height) {
            canvas.drawText(
                "Level: ${model.getCurrentLevel()}",
                10F,
                height - 10F,
                textPaint
            )
        } else {
            canvas.drawText(
                "Level: ${model.getCurrentLevel()}",
                textX,
                height.toFloat() - 10,
                textPaint
            )
        }
    }

    private fun drawBluePrintContentGame(canvas: Canvas, desktop: Array<IntArray>) {
        val subWidth = if (width < height) 1 else width - height - 100
        val widthContentGame = width - subWidth
        val heightContentGame = 9 * widthContentGame / 10
        val x = (width - widthContentGame) / 2
        val textLvlHeight = if (width < height) textPaint.fontMetrics.ascent * -0.4f else 0
        val y = (height - heightContentGame) / 2 - textLvlHeight.toInt()
        val boxWidth = widthContentGame / 10 + x
        var boxHeight = y + widthContentGame / 10
        rect.left = x
        rect.top = y
        rect.right = boxWidth
        rect.bottom = boxHeight
        paint.strokeWidth = 8F

        for (i in desktop.indices) {
            for (j in desktop[i].indices) {
                if (desktop[i][j] == 1) {
                    paint.color = Color.RED
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(rect, paint)
                    paint.style = Paint.Style.STROKE
                    paint.color = Color.BLACK
                    canvas.drawRect(rect, paint)
                } else if (desktop[i][j] == 2) {
                    paint.color = Color.MAGENTA
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(rect, paint)
                    paint.style = Paint.Style.STROKE
                    paint.color = Color.BLACK
                    canvas.drawRect(rect, paint)
                } else if (desktop[i][j] == 3) {
                    paint.color = Color.BLUE
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(rect, paint)
                    paint.style = Paint.Style.STROKE
                    paint.color = Color.BLACK
                    canvas.drawRect(rect, paint)
                } else if (desktop[i][j] == 4) {
                    paint.color = Color.GREEN
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(rect, paint)
                    paint.style = Paint.Style.STROKE
                    paint.color = Color.BLACK
                    canvas.drawRect(rect, paint)
                } else if (desktop[i][j] == 0) {
                    paint.color = Color.rgb(80, 102, 204)
                    paint.style = Paint.Style.FILL
                    canvas.drawRect(rect, paint)
                }
                rect.offset(boxWidth - x, 0) // horizontally offset
            }

            // vertically offset
            rect.offsetTo(x, boxHeight)
            boxHeight = boxHeight + widthContentGame / 10
        }
    }
}