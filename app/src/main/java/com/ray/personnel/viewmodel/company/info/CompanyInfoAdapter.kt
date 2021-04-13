package com.ray.personnel.viewmodel.company.info

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ray.personnel.company.Company
import com.ray.personnel.company.News
import com.ray.personnel.R
import com.ray.personnel.utils.Constants
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType){
        TITLE -> SubtitledHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_subtitled, parent, false))
        NEWS -> NewsHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_news, parent, false))
        MAIN_TASKS, REQUIREMENTS, PREFERRED -> {
            ListHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_list, parent, false))
        }
        LOCATION -> LocationHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_location, parent, false))
        SCALE -> ScaleHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_scale, parent, false))
        SALARY -> SalaryHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_salary, parent, false))
        NEWS_INTRO -> NewsIntroHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_news_intro, parent, false))
        else-> {
            DefaultHolder(LayoutInflater.from(mContext).inflate(R.layout.company_info_item_default, parent, false))
        }
    }

    override fun getItemViewType(i: Int) = when(i){
        TITLE -> TITLE
        MAIN_TASKS -> MAIN_TASKS
        REQUIREMENTS -> REQUIREMENTS
        PREFERRED -> PREFERRED
        NEWS -> NEWS
        NEWS_INTRO -> NEWS_INTRO
        LOCATION -> LOCATION
        SCALE-> SCALE
        SALARY-> SALARY

        else -> {
            DEFAULT
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(position){
            TITLE -> {
                (holder as SubtitledHolder).content.text = company.title
                (holder as SubtitledHolder).subcontent.text = company.department
                holder.wrapper.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WANTED_RECRUIT + company.job_id))
                    mContext.startActivity(intent)
                }
            }
            INTRO -> {
                (holder as DefaultHolder).content.text = company.intro
            }
            MAIN_TASKS -> {
                (holder as ListHolder).content.text = company.main_tasks
            }
            REQUIREMENTS -> {
                (holder as ListHolder).content.text = company.requirements
            }
            PREFERRED -> {
                (holder as ListHolder).content.text = company.preferred
            }
            LOCATION -> {
                (holder as LocationHolder).content.text = "거리 : "+company.distance.toString()+" M"
                holder.subcontent.text = company.location?.full_location
                holder.wrapper.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/@"+company.location!!.geo_location.latitude+","+company.location!!.geo_location.longitude+",20z"))
                    mContext.startActivity(intent)
                }
            }
            SCALE -> {
                (holder as ScaleHolder).content.text = "사원 : "+company.scale+" 명"
                holder.subcontent.text = company.scale_date.substring(0,2)+"년 "+company.scale_date.substring(2,4)+"월 기준"
                holder.third.text = "현역복무 : "+company.scale_normal
                holder.fourth.text = "보충복무 : "+company.scale_fourth
                holder.wrapper.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MILITARY_SEARCH + company.military_url))
                    mContext.startActivity(intent)
                }
            }
            SALARY -> {
                (holder as SalaryHolder).content.text = "루키 : "+company.salary_rookey + " 만원"
                holder.subcontent.text = "일반 : "+company.salary_normal+" 만원"
                holder.wrapper.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WANTED_INTRO + company.company_id))
                    mContext.startActivity(intent)
                }
            }
            NEWS_INTRO -> {
                (holder as NewsIntroHolder).content.text = "\""+company.title+"\"의 최근 정보"
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

    override fun getItemCount() = FINAL

    inner class SubtitledHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
        val subcontent: TextView = v.findViewById(R.id.subcontent)
        val wrapper: ConstraintLayout = v.findViewById(R.id.wrapper)
    }

    inner class DefaultHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
    }

    inner class ListHolder(v: View) : RecyclerView.ViewHolder(v) {
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
                title[i].text = Html.fromHtml(n[i].searchTitle)
                contents[i].text = Html.fromHtml(n[i].searchDescription)
                button[i].setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(n[i].searchURL))
                    mContext.startActivity(intent)
                }
            }
        }
    }

    inner class LocationHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
        val subcontent: TextView = v.findViewById(R.id.subcontent)
        val wrapper: ConstraintLayout = v.findViewById(R.id.wrapper)
    }
    inner class SalaryHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
        val subcontent: TextView = v.findViewById(R.id.subcontent)
        val wrapper: ConstraintLayout = v.findViewById(R.id.wrapper)
    }

    inner class ScaleHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
        val subcontent: TextView = v.findViewById(R.id.subcontent)
        val third: TextView = v.findViewById(R.id.third)
        val fourth: TextView = v.findViewById(R.id.fourth)
        val wrapper: ConstraintLayout = v.findViewById(R.id.wrapper)
    }
    inner class NewsIntroHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
    }


    companion object{
        const val DEFAULT = -1
        const val TITLE = 0
        const val SALARY = TITLE + 1
        const val INTRO = SALARY + 1
        const val MAIN_TASKS = INTRO + 1
        const val REQUIREMENTS = MAIN_TASKS + 1
        const val PREFERRED = REQUIREMENTS + 1
        const val LOCATION = PREFERRED + 1
        const val SCALE = LOCATION + 1
        const val NEWS_INTRO = SCALE + 1
        const val NEWS = NEWS_INTRO + 1
        const val FINAL = NEWS + 1

        //const val BENEFITS = PREFERRED + 1
        //const val NORMAL = 0
        //const val = NORMAL + 1
    }

}