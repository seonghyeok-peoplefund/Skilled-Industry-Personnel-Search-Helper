package com.ray.personnel.widget

import android.content.Context
import android.widget.LinearLayout
import androidx.core.view.children

class SortRadioGroup(context: Context) : LinearLayout(context) {
    var listener: ((Int, Boolean) -> Unit)? = null

    fun setOffOtherRadio(v: SortRadioButton) {
        for (btn in children) {
            if (btn != v && btn is SortRadioButton) {
                btn.isButtonChecked = false
                btn.refreshDrawableState()
            }
        }
    }

    fun isClicked(v: SortRadioButton) {
        val i = indexOfChild(v)
        if (i > -1) {
            listener?.let { it(i, v.isAscendant) }
        }
    }
}