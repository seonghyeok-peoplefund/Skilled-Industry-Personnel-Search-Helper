package com.ray.personnel.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.ray.personnel.R
import com.ray.personnel.domain.ImageManager

class SortRadioButton(context: Context, attrs: AttributeSet? = null) :
    AppCompatImageButton(context),
    View.OnClickListener {

    private val CHECK by lazy { intArrayOf(R.attr.isButtonChecked) }
    private var upBitmap = ImageManager.drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.ic_up2)!!)
    private var downBitmap = ImageManager.drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.ic_down2)!!)
    var isAscendant = true
    var isButtonChecked = false

    init {
        if (attrs != null) {
            isButtonChecked = context
                .obtainStyledAttributes(attrs, R.styleable.SortRadioButton)
                .getBoolean(R.styleable.SortRadioButton_isButtonChecked, false)
        }
        setOnClickListener(this)
    }

    override fun getAccessibilityClassName(): CharSequence = SortRadioButton::class.java.name

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        return if (isButtonChecked) {
            if (isAscendant) {
                val drawableState = super.onCreateDrawableState(extraSpace + R.styleable.SortRadioButton.size)
                mergeDrawableStates(drawableState, R.styleable.SortRadioButton)
                drawableState
            } else {
                val drawableState = super.onCreateDrawableState(extraSpace + CHECK.size)
                mergeDrawableStates(drawableState, CHECK)
                drawableState
            }
        } else super.onCreateDrawableState(extraSpace)
    }

    override fun onClick(p0: View) {
        if (parent is SortRadioGroup && parent != null) {
            if (isButtonChecked) {
                isAscendant = !isAscendant
            } else {
                isButtonChecked = true
                isAscendant = true
                (parent as SortRadioGroup).setOffOtherRadio(this)
            }
            (parent as SortRadioGroup).isClicked(this)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isButtonChecked) {
            if (isAscendant) {
                canvas.drawBitmap(
                    downBitmap,
                    (width - downBitmap.width) - ImageManager.pxToDp(7, context),
                    (height - downBitmap.height) / 2.toFloat(),
                    null
                )
            } else {
                canvas.drawBitmap(
                    upBitmap,
                    (width - upBitmap.width) - ImageManager.pxToDp(7, context),
                    (height - upBitmap.height) / 2.toFloat(), null
                )
            }
        }
        super.onDraw(canvas)
    }
}