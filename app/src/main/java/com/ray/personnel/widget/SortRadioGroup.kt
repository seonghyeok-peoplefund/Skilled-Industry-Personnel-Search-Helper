package com.ray.personnel.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.children

class SortRadioGroup: LinearLayout {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    var listener: SortRadioListener? = null

    fun setOffOtherRadio(v: SortRadioButton){
        for(btn in children){
            if(btn != v && btn is SortRadioButton){
                btn.isButtonChecked = false
                btn.refreshDrawableState()
                //invalidate()
            }
        }
    }

    internal fun isClicked(v: SortRadioButton){
        val i = indexOfChild(v)
        if(i > -1) {
            listener?.onClick(i, v.isAscendant)
        }
    }
    fun setOnClickListener(listener: SortRadioListener){
        this.listener = listener
    }
    interface SortRadioListener{
        fun onClick(index: Int, isAscendant: Boolean)
    }
}