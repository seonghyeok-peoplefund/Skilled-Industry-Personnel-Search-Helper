package com.ray.personnel.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.children

class SortRadioGroup: LinearLayout {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun setOffOtherRadio(v: SortRadioButton){
        for(btn in children){
            if(btn != v && btn is SortRadioButton){
                btn.isButtonChecked = false
                btn.refreshDrawableState()
                //invalidate()
            }
        }
    }
}