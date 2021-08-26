package com.ray.personnel.ui.favorite

import android.graphics.Outline
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.like.LikeButton
import com.like.OnLikeListener
import com.ray.personnel.data.Company
import com.ray.personnel.R
import com.ray.personnel.domain.database.CompanyDatabaseMethods

class FavoriteListAdapter(var companies: List<Company>) : RecyclerView.Adapter<FavoriteListAdapter.SimpleCompanyHolder>() {
    var onItemClickListener: ((Company) -> Unit)? = null
    var onLikeListener: ((Company, Boolean) -> Unit)? = null
    var isLogined = false

    override fun getItemCount() = companies.size

    override fun getItemViewType(i: Int) = if (!isLogined || companies[i].scale > 0) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleCompanyHolder {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_company_list, parent, false)
        val v = SimpleCompanyHolder(convertView)
        if (viewType == 1) {
            v.wrapper.setBackgroundColor(0x79ff0000)
        } else {
            v.wrapper.setBackgroundColor(0x00000000)
        }
        return v
    }

    override fun onBindViewHolder(holder: SimpleCompanyHolder, position: Int) {
        val company = companies[position]
        Glide.with(holder.imgThumb.context)
            .load(Uri.parse(company.thumbURL))
            .thumbnail(0.5f)
            .into(holder.imgThumb)
        holder.title.text = company.title
        holder.department.text = company.department
        holder.pane.setOnClickListener { onItemClickListener?.let { it(company) } }
        if (company.isLiked) holder.favorite.isLiked = true
        holder.favorite.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                onLikeListener?.let { it(company, true) }
            }

            override fun unLiked(likeButton: LikeButton) {
                onLikeListener?.let { it(company, false) }
            }
        })
    }

    class SimpleCompanyHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
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
}