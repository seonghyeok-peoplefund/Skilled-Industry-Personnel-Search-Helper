package com.ray.personnel.ui.favorite

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.like.LikeButton
import com.like.OnLikeListener
import com.ray.personnel.R
import com.ray.personnel.data.Company

class FavoriteListAdapter(var companies: List<Company>) : RecyclerView.Adapter<FavoriteCompanyHolder>() {
    var onItemClickListener: ((Company) -> Unit)? = null

    var onLikeListener: ((Company, Boolean) -> Unit)? = null

    var isLogined = false

    override fun getItemCount() = companies.size

    override fun getItemViewType(i: Int) = if (!isLogined || companies[i].employees > 0) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCompanyHolder {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_company_list, parent, false)
        val v = FavoriteCompanyHolder(convertView)
        if (viewType == 1) {
            v.wrapper.setBackgroundColor(0x79ff0000)
        } else {
            v.wrapper.setBackgroundColor(0x00000000)
        }
        return v
    }

    override fun onBindViewHolder(holder: FavoriteCompanyHolder, position: Int) {
        val company = companies[position]
        Glide.with(holder.imgThumb.context)
            .load(Uri.parse(company.thumbUrl))
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
}