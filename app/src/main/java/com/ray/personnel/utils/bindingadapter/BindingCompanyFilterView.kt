package com.ray.personnel.utils.bindingadapter

import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter


@BindingAdapter("selectedValue")
fun bindSpinnerData(spinner: AppCompatSpinner, pos: Int?) {
    spinner.setSelection(pos?: 0, true)
}

@BindingAdapter("onItemSelected")
fun bindSpinnerData(spinner: AppCompatSpinner, listener: AdapterView.OnItemSelectedListener?) {
    spinner.onItemSelectedListener = listener
}