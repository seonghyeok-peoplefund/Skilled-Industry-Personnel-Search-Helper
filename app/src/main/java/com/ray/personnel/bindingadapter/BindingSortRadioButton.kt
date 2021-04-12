package com.ray.personnel.bindingadapter

import androidx.databinding.BindingAdapter
import com.ray.personnel.ui.SortRadioGroup


@BindingAdapter("onClick")
fun onButtonClick(v: SortRadioGroup, listener: SortRadioGroup.SortRadioListener){
    v.setOnClickListener(listener)
}