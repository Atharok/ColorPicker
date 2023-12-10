package com.atharok.colorpicker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.annotation.ColorInt
import kotlin.math.roundToInt

class ColorPicker: FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    private val redSeekBar: SeekBar
    private val greenSeekBar: SeekBar
    private val blueSeekBar: SeekBar
    private val alphaSeekBar: SeekBar

    private val redThumb = GradientDrawable()
    private val greenThumb = GradientDrawable()
    private val blueThumb = GradientDrawable()
    private val alphaThumb = GradientDrawable()

    var color: Int = Color.BLACK
        private set

    val hexColorARGB: String get() = String.format("#%02X%02X%02X%02X", alphaSeekBar.progress, redSeekBar.progress, greenSeekBar.progress, blueSeekBar.progress)

    private var colorListener: (color: Int, colorHex: String) -> Unit = { _, _ -> }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.color_picker_layout, this, true)

        redSeekBar = view.findViewById(R.id.red_seek_bar)
        greenSeekBar = view.findViewById(R.id.green_seek_bar)
        blueSeekBar = view.findViewById(R.id.blue_seek_bar)
        alphaSeekBar = view.findViewById(R.id.alpha_seek_bar)

        redSeekBar.setOnSeekBarChangeListener(SeekBarChangeListener())
        greenSeekBar.setOnSeekBarChangeListener(SeekBarChangeListener())
        blueSeekBar.setOnSeekBarChangeListener(SeekBarChangeListener())
        alphaSeekBar.setOnSeekBarChangeListener(SeekBarChangeListener())

        updateColor()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updateThumbDrawable(redSeekBar, redThumb)
        updateThumbDrawable(greenSeekBar, greenThumb)
        updateThumbDrawable(blueSeekBar, blueThumb)
        updateThumbDrawable(alphaSeekBar, alphaThumb)
        updateGradientDrawables()
    }

    private fun updateThumbDrawable(seekBar: SeekBar, thumbDrawable: GradientDrawable) {
        val seekBarHeightPx = seekBar.height
        val thumbStrokePx = (seekBarHeightPx * (6.5f / 100f)).roundToInt()
        val progress = seekBar.progress
        seekBar.progress = 0

        thumbDrawable.apply {
            shape = GradientDrawable.OVAL
            setSize(seekBarHeightPx, seekBarHeightPx)
            setColor(Color.TRANSPARENT)
            setStroke(thumbStrokePx, Color.WHITE)
        }

        seekBar.thumb = thumbDrawable
        seekBar.thumbOffset = 0
        seekBar.progress = progress
    }

    private inner class SeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            updateColor()
            updateGradientDrawables()
        }
        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    fun addColorChangedListener(listener: (color: Int, hexColorARGB: String) -> Unit) {
        colorListener = listener
    }

    private fun updateColor() {
        val red: Int = redSeekBar.progress
        val green: Int = greenSeekBar.progress
        val blue: Int = blueSeekBar.progress
        val alpha: Int = alphaSeekBar.progress
        color = Color.argb(alpha, red, green, blue)
        colorListener(color, hexColorARGB)
    }

    private fun updateGradientDrawables() {
        redSeekBar.progressDrawable = createRedGradientDrawable()
        greenSeekBar.progressDrawable = createGreenGradientDrawable()
        blueSeekBar.progressDrawable = createBlueGradientDrawable()
        alphaSeekBar.progressDrawable = createAlphaGradientDrawable()
    }

    private fun createRedGradientDrawable(): GradientDrawable {
        val green: Int = greenSeekBar.progress
        val blue: Int = blueSeekBar.progress
        val startColor = Color.rgb(0, green, blue)
        val endColor = Color.rgb(255, green, blue)
        return createGradientDrawable(startColor, endColor, redSeekBar.height/2f)
    }

    private fun createGreenGradientDrawable(): GradientDrawable {
        val red: Int = redSeekBar.progress
        val blue: Int = blueSeekBar.progress
        val startColor = Color.rgb(red, 0, blue)
        val endColor = Color.rgb(red, 255, blue)
        return createGradientDrawable(startColor, endColor, greenSeekBar.height/2f)
    }

    private fun createBlueGradientDrawable(): GradientDrawable {
        val red: Int = redSeekBar.progress
        val green: Int = greenSeekBar.progress
        val startColor = Color.rgb(red, green, 0)
        val endColor = Color.rgb(red, green, 255)
        return createGradientDrawable(startColor, endColor, blueSeekBar.height/2f)
    }

    private fun createAlphaGradientDrawable(): Drawable {
        val red: Int = redSeekBar.progress
        val green: Int = greenSeekBar.progress
        val blue: Int = blueSeekBar.progress
        val startColor = Color.TRANSPARENT
        val endColor = Color.rgb(red, green, blue)
        val cornerRadius = alphaSeekBar.height / 2f
        val checkedPatternDrawable = CheckedPatternDrawable(cornerRadius)
        val gradientDrawable = createGradientDrawable(startColor, endColor, cornerRadius)
        return LayerDrawable(arrayOf(checkedPatternDrawable, gradientDrawable))
    }

    private fun createGradientDrawable(
        @ColorInt startColor: Int,
        @ColorInt endColor: Int,
        cornerRadius: Float
    ): GradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(startColor, endColor)).apply {
        this.shape = GradientDrawable.RECTANGLE
        this.cornerRadius = cornerRadius
        this.gradientType = GradientDrawable.LINEAR_GRADIENT
    }

    // ----

    fun setFromColorARGB(a: Int, r: Int, g: Int, b: Int) {
        alphaSeekBar.progress = a.coerceIn(0, 255)
        redSeekBar.progress = r.coerceIn(0, 255)
        greenSeekBar.progress = g.coerceIn(0, 255)
        blueSeekBar.progress = b.coerceIn(0, 255)
    }

    fun setFromColorInt(@ColorInt color: Int) = setFromColorARGB(
        a = Color.alpha(color),
        r = Color.red(color),
        g = Color.green(color),
        b = Color.blue(color)
    )

    /**
     * @param hexColorARGB Format: #FF000000
     */
    fun setFromHexColorARGB(hexColorARGB: String?) {
        if(!hexColorARGB.isNullOrBlank() && hexColorARGB.length == 9 && hexColorARGB[0] == '#') {
            try {
                setFromColorARGB(
                    a = Integer.parseInt(hexColorARGB.substring(1, 3), 16),
                    r = Integer.parseInt(hexColorARGB.substring(3, 5), 16),
                    g = Integer.parseInt(hexColorARGB.substring(5, 7), 16),
                    b = Integer.parseInt(hexColorARGB.substring(7, 9), 16)
                )
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }
}