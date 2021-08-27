package com.ray.personnel.ui.companyinfo

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ray.personnel.R
import com.ray.personnel.data.Company
import com.ray.personnel.data.Location
import com.ray.personnel.data.News

class CompanyInfoAdapter(var company: Company) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //TODO("홀더 새로 파일 만들어서 거기 넣을것. 근데 그전에 이거 잘못하고있는데... 전부 바꾸기엔 시간 너무 걸려. 다른 작업 먼저 진행하도록 함.")
    var onTitleClickListener: ((Company) -> Unit)? = null

    var onLocationClickListener: ((Location) -> Unit)? = null

    var onScaleClickListener: ((Company) -> Unit)? = null

    var onSalaryClickListener: ((Company) -> Unit)? = null

    var onNewsClickListener: ((News) -> Unit)? = null

    override fun getItemCount() = FINAL

    override fun getItemViewType(i: Int): Int {
        return when (i) {
            TITLE -> TITLE
            MAIN_TASKS -> MAIN_TASKS
            REQUIREMENTS -> REQUIREMENTS
            PREFERRED -> PREFERRED
            NEWS -> NEWS
            NEWS_INTRO -> NEWS_INTRO
            LOCATION -> LOCATION
            SCALE -> SCALE
            SALARY -> SALARY
            else -> DEFAULT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TITLE -> SubtitledHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_info_subtitled, parent, false))
        NEWS -> NewsHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_info_news, parent, false))
        MAIN_TASKS, REQUIREMENTS, PREFERRED -> {
            ListHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_info_list, parent, false))
        }
        LOCATION -> LocationHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_info_location, parent, false))
        SCALE -> ScaleHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_info_scale, parent, false))
        SALARY -> SalaryHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_info_salary, parent, false))
        NEWS_INTRO -> NewsIntroHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_info_news_intro, parent, false))
        else -> DefaultHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_info_default, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            TITLE -> {
                holder as SubtitledHolder
                holder.content.text = company.title
                holder.subcontent.text = company.department
                holder.wrapper.setOnClickListener { onTitleClickListener?.let { it(company) } }
            }
            INTRO -> {
                (holder as DefaultHolder).content.text = company.intro
            }
            MAIN_TASKS -> {
                (holder as ListHolder).content.text = company.mainTasks
            }
            REQUIREMENTS -> {
                (holder as ListHolder).content.text = company.requirements
            }
            PREFERRED -> {
                (holder as ListHolder).content.text = company.preferred
            }
            LOCATION -> {
                (holder as LocationHolder).content.text = "거리 : ${company.distance} M"
                holder.subcontent.text = company.location?.fullLocation
                holder.wrapper.setOnClickListener { onLocationClickListener?.let { it(company.location!!) } }
            }
            SCALE -> {
                (holder as ScaleHolder).content.text = "사원 : ${company.employees} 명"
                holder.subcontent.text = "${company.employeesLatestDate.substring(0, 2)}년 ${company.employeesLatestDate.substring(2, 4)}월 기준"
                holder.third.text = "현역복무 : ${company.employeesActivePersonnel}명"
                holder.fourth.text = "보충복무 : ${company.employeesReservePersonnel}명"
                holder.wrapper.setOnClickListener { v -> onScaleClickListener?.let { it(company) } }
            }
            SALARY -> {
                (holder as SalaryHolder).content.text = "루키 : ${company.salaryRookey} 만원"
                holder.subcontent.text = "일반 : ${company.salaryNormal} 만원"
                holder.wrapper.setOnClickListener { onSalaryClickListener?.let { it(company) } }
            }
            NEWS_INTRO -> {
                (holder as NewsIntroHolder).content.text = "\"${company.title}\"의 최근 정보"
            }
            NEWS -> {
                if (company.news != null) {
                    holder as NewsHolder
                    for (i in company.news!!.indices) {
                        holder.title[i].text = Html.fromHtml(company.news!![i].title)
                        holder.contents[i].text = Html.fromHtml(company.news!![i].description)
                        holder.button[i].setOnClickListener { onNewsClickListener?.let { it(company.news!![i]) } }
                    }
                }
            }
            else -> {
                (holder as DefaultHolder).content.text = "NOTHING${(position.toString())}"
            }
        }
    }

    fun refresh(position: Int) {
        notifyItemChanged(position)
    }

    class SubtitledHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
        val subcontent: TextView = v.findViewById(R.id.subcontent)
        val wrapper: ConstraintLayout = v.findViewById(R.id.wrapper)
    }

    class DefaultHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
    }

    class ListHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.content)
    }

    class NewsHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: List<TextView> = listOf(
            v.findViewById(R.id.news_title)
        )
        val contents: List<TextView> = listOf(
            v.findViewById(R.id.news_content)
        )
        val button: List<View> = listOf(
            v.findViewById(R.id.news_container)
        )
    }

    class LocationHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.location_content)
        val subcontent: TextView = v.findViewById(R.id.location_subcontent)
        val wrapper: ConstraintLayout = v.findViewById(R.id.wrapper)
    }

    class SalaryHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.salary_content)
        val subcontent: TextView = v.findViewById(R.id.salary_subcontent)
        val wrapper: ConstraintLayout = v.findViewById(R.id.wrapper)
    }

    class ScaleHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.scale_content)
        val subcontent: TextView = v.findViewById(R.id.scale_subcontent)
        val third: TextView = v.findViewById(R.id.personnel_subcontent)
        val fourth: TextView = v.findViewById(R.id.personnel_content)
        val wrapper: ConstraintLayout = v.findViewById(R.id.wrapper)
    }

    class NewsIntroHolder(v: View) : RecyclerView.ViewHolder(v) {
        val content: TextView = v.findViewById(R.id.news_intro_content)
    }

    companion object {
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
    }
}