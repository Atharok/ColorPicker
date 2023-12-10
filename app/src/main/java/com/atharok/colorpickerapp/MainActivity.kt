package com.atharok.colorpickerapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.atharok.colorpicker.ColorPicker
import com.atharok.colorpicker.ColorPickerDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private var colorPickerDialog: ColorPickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ---- Color Picker in XML layout ----

        val colorPreview: ImageView = findViewById(R.id.color_preview)
        val colorPicker: ColorPicker = findViewById(R.id.color_picker)
        val textInputEditText: TextInputEditText = findViewById(R.id.text_input_edit_text)

        colorPreview.setBackgroundColor(colorPicker.color)

        textInputEditText.setText(colorPicker.hexColorARGB)
        textInputEditText.addTextChangedListener {
            if(textInputEditText.isFocused) {
                val value: String? = it?.toString()
                colorPicker.setFromHexColorARGB(value)
            }
        }

        colorPicker.addColorChangedListener { color: Int, hexColorARGB: String ->
            colorPreview.setBackgroundColor(color)
            if(textInputEditText.text?.toString() != hexColorARGB) {
                val oldSelectionPosition = textInputEditText.selectionStart
                textInputEditText.setText(hexColorARGB)
                textInputEditText.setSelection(oldSelectionPosition)
            }
        }

        // ---- Color Picker Dialog ----

        val button: MaterialButton = findViewById(R.id.material_button)

        val builder = ColorPickerDialog.Builder(this)

        button.setOnClickListener {
            colorPickerDialog = builder.apply {
                setTitle(R.string.dialog_title)
                setDefaultColor(colorPicker.hexColorARGB)
                setPositiveButton { color: Int, hexColorARGB: String ->
                    colorPicker.setFromHexColorARGB(hexColorARGB)
                }
            }.show()
        }
    }

    override fun onPause() {
        super.onPause()
        colorPickerDialog?.dismiss()
    }
}
