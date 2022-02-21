package com.pinkcloud.searchapplication.ui.search

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.pinkcloud.searchapplication.R
import com.pinkcloud.searchapplication.util.hideKeyboard

class HideKeyboardConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var inputRect = Rect()

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val textInput = findViewById<TextInputEditText>(R.id.search_text_input)
            textInput?.run {
                this.getGlobalVisibleRect(inputRect)
                if (!inputRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) hideKeyboard(context, this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}