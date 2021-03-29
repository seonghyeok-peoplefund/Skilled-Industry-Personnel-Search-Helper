package com.ray.personnel.Activity.CompanyActivity.CompanyList

import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.like.LikeButton
import com.like.OnLikeListener
import com.ray.personnel.Company.Company
import com.ray.personnel.R
import java.util.*

class CompanyListAdapter(private val mContext: Context, private val companies: ArrayList<Company>) : RecyclerView.Adapter<CompanyListAdapter.SimpleCompanyHolder>() {
    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, company: Company)
    }

    fun setOnItemClickListener(listener: (View, Company) -> Unit) {

        onItemClickListener = object: OnItemClickListener {
            override fun onItemClick(view: View, company: Company) {
                listener(view, company)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleCompanyHolder {
        val convertView = LayoutInflater.from(mContext).inflate(R.layout.company_list_item, parent, false)
        return SimpleCompanyHolder(convertView)
    }

    override fun onBindViewHolder(holder: SimpleCompanyHolder, position: Int) {
        val company = companies[position]
        Glide.with(mContext)
                .load(company.thumbURL)
                .thumbnail(0.5f)
                .into(holder.img_thumb)
        holder.title.text = company.title
        holder.department.text = company.department
        holder.pane.setOnClickListener { v -> onItemClickListener?.onItemClick(v, company) }
        holder.favorite.setOnLikeListener(object: OnLikeListener{
            override fun liked(likeButton: LikeButton) {
            }
            override fun unLiked(likeButton: LikeButton) {
            }
        });
    }

    override fun getItemCount() = companies.size

    //override fun getItemViewType(i: Int) = if(companies[i].isMilitary) 0 else 1

    inner class SimpleCompanyHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        val pane: ConstraintLayout
        val img_thumb: ImageView
        val title: TextView
        val department: TextView
        val favorite: LikeButton

        init {
            pane = convertView.findViewById(R.id.company_list_item_pane)
            img_thumb = convertView.findViewById(R.id.company_list_item_thumb)
            title = convertView.findViewById(R.id.company_list_item_title)
            department = convertView.findViewById(R.id.company_list_item_department)
            favorite = convertView.findViewById(R.id.favorite)
            //img_thumb.setBackgroundResource(R.drawable.company_info_item_rounded)
            val radius = 30f
            img_thumb.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(0, 0, view!!.width, (view.height + radius).toInt(), radius)
                }
            }
            img_thumb.clipToOutline = true



        }
    }

    companion object{
        //const val NORMAL = 0
        //const val = NORMAL + 1
    }

}