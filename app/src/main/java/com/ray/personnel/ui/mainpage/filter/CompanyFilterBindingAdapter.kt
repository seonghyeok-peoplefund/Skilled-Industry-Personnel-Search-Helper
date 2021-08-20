package com.ray.personnel.ui.mainpage.filter

import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter

@BindingAdapter("selectedValue")
fun selectedValue(spinner: AppCompatSpinner, pos: Int?) {
    spinner.setSelection(pos ?: 0, true)
}

@BindingAdapter("onItemSelected")
fun onItemSelected(spinner: AppCompatSpinner, listener: AdapterView.OnItemSelectedListener?) {
    spinner.onItemSelectedListener = listener
}