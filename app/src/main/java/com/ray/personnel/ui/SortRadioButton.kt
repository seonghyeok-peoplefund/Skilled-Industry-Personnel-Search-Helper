package com.ray.personnel.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.ray.personnel.R


class SortRadioButton : androidx.appcompat.widget.AppCompatImageButton, View.OnClickListener {
    val n = num++
    var isAscendant = true
    var isButtonChecked = false
    private var listener: SortRadioListener? = null
    private val CHECK by lazy{ intArrayOf(R.attr.isButtonChecked) }

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        isButtonChecked = context.obtainStyledAttributes(attrs, R.styleable.SortRadioButton).getBoolean(R.styleable.SortRadioButton_isButtonChecked, false)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        isButtonChecked = context.obtainStyledAttributes(attrs, R.styleable.SortRadioButton).getBoolean(R.styleable.SortRadioButton_isButtonChecked, false)
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

    fun setOnClickListener(listener: SortRadioListener){
        this.listener = listener
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
            listener?.onClick(this, isAscendant)
        }
    }

    init{
        setOnClickListener(this)
    }
    companion object{
        var num = 0
    }
    interface SortRadioListener{
        fun onClick(v: View, isAscendant: Boolean)
    }

}