package com.ray.personnel.Activity.CompanyActivity.CompanyInfo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ray.personnel.Company.Company
import com.ray.personnel.Company.News
import com.ray.personnel.R
import kotlin.collections.ArrayList

class CompanyInfoAdapter(private val mContext: Context, private val company: Company) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    /**
     * View가 보일때는 onBindViewHolder가 불러와짐
     * 그외에는 호출안됨
     */
    fun refresh(position: Int){

        notifyItemChanged(position)
    }

    fun filter(charText: String) {
        /*
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        items.clear()
        if (charText.length == 0) {
            items.addAll(arrayList)
        } else {
            for (recent in arrayList) {
                val name: String = recent.getAddress()
                if (name.toLowerCase().contains(charText)) {
                    items.add(recent)
                }
            }
        }
        notifyDataSetChanged()*/
        println("search $charText")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType){
        TITLE-> SubtitledHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_subtitled, parent, false))
        NEWS-> NewsHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_news, parent, false))
        else-> DefaultHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_default, parent, false))
    }

    override fun getItemViewType(i: Int) = when(i){
        TITLE -> TITLE
        NEWS -> NEWS
        else -> DEFAULT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(position){
            TITLE -> {
                (holder as SubtitledHolder).content.text = company.title
                (holder as SubtitledHolder).subcontent.text = company.department
            }
            POSITION -> {
                (holder as DefaultHolder).content.text = company.department
            }
            DESCRIPTION -> {
                (holder as DefaultHolder).content.text = company.description
            }
            RECRUIT -> {
                (holder as DefaultHolder).content.text = "NOTHING"+(position.toString())
            }
            NEWS -> {
                if(company.news == null){

                } else{
                    (holder as NewsHolder).setNews(company.news!!)
                }
            }
            else -> {
                (holder as DefaultHolder).content.text = "NOTHING"+(position.toString())
            }
        }
    }

    override fun getItemCount() = 100

    inner class SubtitledHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
        val subcontent: TextView = v.findViewById(R.id.subcontent)
    }

    inner class DefaultHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
    }

    inner class NewsHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: ArrayList<TextView> = arrayListOf(
                v.findViewById(R.id.news1_title),
                v.findViewById(R.id.news2_title),
                v.findViewById(R.id.news3_title),
                v.findViewById(R.id.news4_title),
                v.findViewById(R.id.news5_title))
        val contents: ArrayList<TextView> = arrayListOf(
                v.findViewById(R.id.news1_contents),
                v.findViewById(R.id.news2_contents),
                v.findViewById(R.id.news3_contents),
                v.findViewById(R.id.news4_contents),
                v.findViewById(R.id.news5_contents))
        val button: ArrayList<View> = arrayListOf(
                v.findViewById(R.id.layout1),
                v.findViewById(R.id.layout2),
                v.findViewById(R.id.layout3),
                v.findViewById(R.id.layout4),
                v.findViewById(R.id.layout5))

        fun setNews(n: ArrayList<News>){
            for(i in 0 until n.size){
                title[i].text = n[i].searchTitle
                contents[i].text = n[i].searchDescription
                button[i].setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(n[i].searchURL))
                    mContext.startActivity(intent)
                }
            }
        }
    }


    companion object{
        const val DEFAULT = -1
        const val TITLE = 0
        const val POSITION = TITLE + 1
        const val DESCRIPTION = POSITION + 1
        const val RECRUIT = DESCRIPTION + 1
        const val NEWS = RECRUIT + 1

        //const val NORMAL = 0
        //const val = NORMAL + 1
    }

}