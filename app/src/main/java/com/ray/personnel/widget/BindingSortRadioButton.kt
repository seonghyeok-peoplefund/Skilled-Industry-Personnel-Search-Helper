package com.ray.personnel.widget

import androidx.databinding.BindingAdapter


@BindingAdapter("onClick")
fun onButtonClick(v: SortRadioGroup, listener: SortRadioGroup.SortRadioListener){
    v.setOnClickListener(listener)
}