package com.ray.personnel.ui.favorite

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.like.LikeButton
import com.ray.personnel.R

class FavoriteCompanyHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
    val pane: ConstraintLayout = convertView.findViewById(R.id.company_list_item_pane)
    val imgThumb: ImageView = convertView.findViewById(R.id.company_list_item_thumb)
    val title: TextView = convertView.findViewById(R.id.company_list_item_title)
    val department: TextView = convertView.findViewById(R.id.company_list_item_department)
    val favorite: LikeButton = convertView.findViewById(R.id.favorite)
    val wrapper: View = convertView.findViewById(R.id.wrapper)

    init {
        val radius = 30f
        imgThumb.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(
                    0,
                    0,
                    view.width,
                    (view.height + radius).toInt(),
                    radius
                )
            }
        }
        imgThumb.clipToOutline = true
    }
}