package com.ray.personnel.bindingadapter

import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter


@BindingAdapter("selectedValue")
fun bindSpinnerData(spinner: AppCompatSpinner, pos: Int?) {
    spinner.setSelection(pos?: 0, true)
}

@BindingAdapter("onItemSelected")
fun bindSpinnerData(spinner: AppCompatSpinner, listener: AdapterView.OnItemSelectedListener?) {
    spinner.onItemSelectedListener = listener
}