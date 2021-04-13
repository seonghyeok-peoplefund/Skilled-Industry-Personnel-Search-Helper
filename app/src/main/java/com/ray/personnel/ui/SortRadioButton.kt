package com.ray.personnel.ui

import android.R.attr.strokeColor
import android.R.attr.strokeWidth
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.ray.personnel.R
import com.ray.personnel.utils.ImageManager


class SortRadioButton : androidx.appcompat.widget.AppCompatImageButton, View.OnClickListener {
    var isAscendant = true
    var isButtonChecked = false
    private val CHECK by lazy{ intArrayOf(R.attr.isButtonChecked) }
    lateinit var up: Bitmap
    lateinit var down: Bitmap

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        isButtonChecked = context.obtainStyledAttributes(attrs, R.styleable.SortRadioButton).getBoolean(R.styleable.SortRadioButton_isButtonChecked, false)
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        isButtonChecked = context.obtainStyledAttributes(attrs, R.styleable.SortRadioButton).getBoolean(R.styleable.SortRadioButton_isButtonChecked, false)
        init()
    }

    fun init(){
        up = ImageManager.drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.ic_up2)!!)
        down = ImageManager.drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.ic_down2)!!)
    }
    override fun getAccessibilityClassName(): CharSequence {
        return SortRadioButton::class.java.name
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {

        if(isButtonChecked){
            if(isAscendant){
                val drawableState = super.onCreateDrawableState(extraSpace + R.styleable.SortRadioButton.size)
                mergeDrawableStates(drawableState, R.styleable.SortRadioButton)
                return drawableState
            } else{
                val drawableState = super.onCreateDrawableState(extraSpace + CHECK.size)
                mergeDrawableStates(drawableState, CHECK)
                return drawableState
            }
        }
        return super.onCreateDrawableState(extraSpace)
    }

    override fun onClick(p0: View?) {
        if(parent is SortRadioGroup && parent != null) {
            if (isButtonChecked) {
                isAscendant = !isAscendant
            } else {
                isButtonChecked = true
                isAscendant = true
                (parent as SortRadioGroup).setOffOtherRadio(this)
            }
            //refreshDrawableState()
            //invalidate()
            (parent as SortRadioGroup).isClicked(this)
        }
    }
    init{
        setOnClickListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        if(isButtonChecked){
            if(isAscendant) canvas?.drawBitmap(down, (width - down.width) - ImageManager.pxToDp(7, context), (height - down.height) / 2.toFloat(), null)
            else canvas?.drawBitmap(up, (width - up.width) - ImageManager.pxToDp(7, context), (height - up.height) / 2.toFloat(), null)
        }
        super.onDraw(canvas)
    }

}