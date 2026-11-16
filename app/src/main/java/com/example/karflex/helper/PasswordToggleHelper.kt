package com.example.karflex.helper

import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton
import com.example.karflex.R

class PasswordToggleHelper(
    private val editText: EditText,
    private val toggleButton: ImageButton
) {
    private var isPasswordVisible = false

    fun setup() {
        toggleButton.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

                toggleButton.setImageResource(R.drawable.ic_visibility)
                toggleButton.contentDescription = "Hide password"
            } else {
                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD

                toggleButton.setImageResource(R.drawable.ic_visibility_off)
                toggleButton.contentDescription = "Show password"
            }

            editText.setSelection(editText.text?.length ?: 0)
        }
    }
}
