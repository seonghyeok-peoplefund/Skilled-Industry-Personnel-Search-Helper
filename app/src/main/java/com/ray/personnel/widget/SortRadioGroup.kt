package com.ray.personnel.widget

import android.content.Context
import android.widget.LinearLayout
import androidx.core.view.children

// 버튼들을 감싸는 그룹이 있어야 할 것 같아 LinearLayout을 상속함.
// 이후 SortRadioButton에서 가져온 내용들을 토대로 내용을 추가함.
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