package com.ray.personnel.widget

import androidx.databinding.BindingAdapter

@BindingAdapter("onClick")
fun onButtonClick(v: SortRadioGroup, listener: ((Int, Boolean) -> Unit)?) {
    v.listener = listener
}