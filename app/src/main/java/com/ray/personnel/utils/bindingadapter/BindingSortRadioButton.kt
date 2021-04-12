package com.ray.personnel.utils.bindingadapter

import androidx.databinding.BindingAdapter
import com.ray.personnel.ui.SortRadioButton
import com.ray.personnel.ui.SortRadioGroup


@BindingAdapter("onClick")
fun onButtonClick(v: SortRadioGroup, listener: SortRadioGroup.SortRadioListener){
    v.setOnClickListener(listener)
}