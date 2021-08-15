package com.ray.personnel.ui.mainpage.filter.list

import android.content.Context
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
import com.ray.personnel.domain.database.CompanyDatabase
import com.ray.personnel.R
import com.ray.personnel.Constants
import com.ray.personnel.domain.preference.PreferenceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CompanyListAdapter(private val mContext: Context, var companies: List<Company>) : RecyclerView.Adapter<CompanyListAdapter.SimpleCompanyHolder>() {
    var onItemClickListener: OnItemClickListener? = null
    var isLogined = PreferenceManager.getString(mContext, Constants.TOKEN)?.isNotEmpty()?:false

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
        val convertView = LayoutInflater.from(mContext).inflate(R.layout.item_company_list, parent, false)//NO
        val v = SimpleCompanyHolder(convertView)
        if(viewType == 1) v.wrapper.setBackgroundColor(0x79ff0000)
        else v.wrapper.setBackgroundColor(0x00000000)
        return v
    }

    override fun onBindViewHolder(holder: SimpleCompanyHolder, position: Int) {
        val company = companies[position]
        Glide.with(mContext)//no
                .load(Uri.parse(company.thumbURL))
                .thumbnail(0.5f)
                .into(holder.img_thumb)
        holder.title.text = company.title
        holder.department.text = company.department
        holder.pane.setOnClickListener { v -> onItemClickListener?.onItemClick(v, company) }
        if(company.isLiked) holder.favorite.isLiked = true
        holder.favorite.setOnLikeListener(object: OnLikeListener{
            override fun liked(likeButton: LikeButton) {
                company.isLiked = true
                CompanyDatabase.getInstance(mContext).companyDao().update(company)//no
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }
            override fun unLiked(likeButton: LikeButton) {
                company.isLiked = false
                CompanyDatabase.getInstance(mContext)//chain
                    .companyDao()
                    .update(company)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }
        })
    }

    override fun getItemViewType(i: Int) = if(!isLogined || companies[i].scale > 0) 0; else 1
    override fun getItemCount() = companies.size

    //override fun getItemViewType(i: Int) = if(companies[i].isMilitary) 0 else 1

    inner class SimpleCompanyHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        val pane: ConstraintLayout
        val img_thumb: ImageView
        val title: TextView
        val department: TextView
        val favorite: LikeButton
        val wrapper: View

        init {
            pane = convertView.findViewById(R.id.company_list_item_pane)
            img_thumb = convertView.findViewById(R.id.company_list_item_thumb)
            title = convertView.findViewById(R.id.company_list_item_title)
            department = convertView.findViewById(R.id.company_list_item_department)
            wrapper = convertView.findViewById(R.id.wrapper)
            favorite = convertView.findViewById(R.id.favorite)
            val radius = 30f
            img_thumb.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(0, 0, view!!.width, (view.height + radius).toInt(), radius)
                }
            }
            img_thumb.clipToOutline = true



        }
    }


}