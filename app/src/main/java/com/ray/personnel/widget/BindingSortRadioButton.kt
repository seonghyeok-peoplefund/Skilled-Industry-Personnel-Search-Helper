package com.ray.personnel.widget

import androidx.databinding.BindingAdapter

// BindingAdapter 예제 그대로 가져와 사용함
@BindingAdapter("onClick")
fun onButtonClick(v: SortRadioGroup, listener: ((Int, Boolean) -> Unit)?) {
    v.listener = listener
}