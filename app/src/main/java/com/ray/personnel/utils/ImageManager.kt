package com.ray.personnel.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas

import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable
import android.util.DisplayMetrics







class ImageManager{
    companion object{
        fun drawableToBitmap(drawable: Drawable): Bitmap {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        //dp를 px로 변환 (dp를 입력받아 px을 리턴)
        fun dpToPx(dp: Int, context: Context) = dpToPx(dp.toFloat(), context)
        fun dpToPx(dp: Float, context: Context): Float {
            val resources: Resources = context.resources
            val metrics: DisplayMetrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        //px을 dp로 변환 (px을 입력받아 dp를 리턴)
        fun pxToDp(px: Int, context: Context) = pxToDp(px.toFloat(), context)
        fun pxToDp(px: Float, context: Context): Float {
            val resources: Resources = context.resources
            val metrics: DisplayMetrics = resources.displayMetrics
            return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }
}