package com.atharok.colorpicker

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import kotlin.math.roundToInt

class CheckedPatternDrawable(private val cornerRadius: Float): Drawable() {

    private val clipPath = Path()
    private val paint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val lightColor = Color.parseColor("#ffd1d1d1")
    private val darkColor = Color.parseColor("#ffbdbdbd")

    override fun draw(canvas: Canvas) {
        clipPath.reset()
        clipPath.addRoundRect(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat(), cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(clipPath)

        val width = bounds.width()
        val height = bounds.height()
        val unit: Int = maxOf((height / 3f).roundToInt(), 1)

        for (i in 0 until width step unit) {
            for (j in 0 until height step unit) {
                paint.color = if(((i + j) / unit.toFloat()).roundToInt() % 2 == 0) lightColor else darkColor
                val posX = i.toFloat()
                val posY = j.toFloat()
                canvas.drawRect(posX, posY, posX + unit, posY + unit, paint)
            }
        }
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("PixelFormat.OPAQUE", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int = PixelFormat.OPAQUE
}