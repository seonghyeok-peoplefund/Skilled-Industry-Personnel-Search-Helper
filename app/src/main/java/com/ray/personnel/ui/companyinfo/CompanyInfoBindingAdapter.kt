package com.ray.personnel.ui.companyinfo

import android.graphics.Bitmap
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout

@BindingAdapter("imageBitmap")
fun imageBitmap(v: AppCompatImageView, bitmap: Bitmap) {
    v.setImageBitmap(bitmap)
}

@BindingAdapter("colorOfTheme")
fun colorOfTheme(v: CollapsingToolbarLayout, color: Int) {
    v.setContentScrimColor(color)
    v.setStatusBarScrimColor(color)
}