package com.atharok.colorpicker

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText

class ColorPickerDialog: AlertDialog {
    private constructor(context: Context) : super(context)
    private constructor(context: Context, themeResId: Int) : super(context, themeResId)
    private constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    class Builder(private val context: Context, themeResId: Int? = null) {

        private val colorPickerDialog: ColorPickerDialog by lazy {
            themeResId?.let { ColorPickerDialog(context, it) } ?: run { ColorPickerDialog(context) }
        }

        private var title: String? = null
        private var positiveButtonText: String = context.getString(android.R.string.ok)
        private var positiveButtonOnClickListener: (color: Int, hexColorARGB: String) -> Unit = { _, _ -> }
        private var negativeButtonText: String = context.getString(android.R.string.cancel)
        private var negativeButtonOnClickListener: OnClickListener? = null

        private val view: View
        private val colorPicker: ColorPicker
        private val colorPreview: ImageView
        private val inputEditText: TextInputEditText

        init {
            view = View.inflate(context, R.layout.color_picker_dialog_layout, null)
            colorPicker = view.findViewById(R.id.color_picker_dialog_layout_color_picker)
            colorPreview = view.findViewById(R.id.color_picker_dialog_layout_color_preview)
            inputEditText = view.findViewById(R.id.color_picker_dialog_layout_text_input_edit_text)
            configureView()
        }

        private fun configureView() {
            // ---- Color Preview ----
            setDrawableColor(colorPreview.drawable, colorPicker.color)

            // ---- TextInputEditText ----
            inputEditText.setText(colorPicker.hexColorARGB)
            inputEditText.addTextChangedListener {
                if(inputEditText.isFocused) {
                    val value: String? = it?.toString()
                    colorPicker.setFromHexColorARGB(value)
                }
            }

            // ---- Color Picker ----
            colorPicker.addColorChangedListener { color: Int, hexColorARGB: String ->
                setDrawableColor(colorPreview.drawable, color)
                if(inputEditText.text?.toString() != hexColorARGB) {
                    val oldSelectionPosition = inputEditText.selectionStart
                    inputEditText.setText(hexColorARGB)
                    inputEditText.setSelection(oldSelectionPosition)
                }
            }
        }

        private fun setDrawableColor(drawable: Drawable, @ColorInt color: Int) {
            if(drawable is GradientDrawable) drawable.setColor(color) else drawable.setTint(color)
        }

        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setTitle(@StringRes stringRes: Int): Builder {
            this.title = context.getString(stringRes)
            return this
        }

        fun setDefaultColor(@ColorInt color: Int): Builder {
            colorPicker.setFromColorInt(color)
            return this
        }

        fun setDefaultColor(a: Int, r: Int, g: Int, b: Int): Builder {
            colorPicker.setFromColorARGB(a, r, g, b)
            return this
        }

        fun setDefaultColor(hexColorARGB: String?): Builder {
            colorPicker.setFromHexColorARGB(hexColorARGB)
            return this
        }

        fun setPositiveButton(
            text: String = context.getString(android.R.string.ok),
            listener: (color: Int, hexColorARGB: String) -> Unit = { _, _,-> }
        ): Builder {
            this.positiveButtonText = text
            this.positiveButtonOnClickListener = listener
            return this
        }

        fun setNegativeButton(
            text: String = context.getString(android.R.string.cancel),
            listener: OnClickListener? = null
        ): Builder {
            this.negativeButtonText = text
            this.negativeButtonOnClickListener = listener
            return this
        }

        fun create(): ColorPickerDialog {
            colorPickerDialog.setView(view)
            title?.let { colorPickerDialog.setTitle(it) }
            colorPickerDialog.setButton(BUTTON_POSITIVE, positiveButtonText) { _, _ ->
                positiveButtonOnClickListener(colorPicker.color, colorPicker.hexColorARGB)
            }
            colorPickerDialog.setButton(BUTTON_NEGATIVE, negativeButtonText, negativeButtonOnClickListener)
            colorPickerDialog.show()
            return colorPickerDialog
        }

        fun show(): ColorPickerDialog {
            val dialog: ColorPickerDialog = create()
            dialog.show()
            return dialog
        }
    }
}