package com.ray.personnel.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatRadioButton


class SortRadioButton : AppCompatRadioButton {
    var isAscendant = false
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun toggle() {
        if (isChecked) {
            if (parent != null && parent is RadioGroup) {
                //(parent as RadioGroup).clearCheck()
                isAscendant = !isAscendant

            }
        } else {
            super.toggle()
        }
    }

    override fun getAccessibilityClassName(): CharSequence {
        return SortRadioButton::class.java.name
    }
}