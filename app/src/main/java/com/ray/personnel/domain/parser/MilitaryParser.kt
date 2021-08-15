package com.ray.personnel.domain.parser

import com.ray.personnel.data.Company
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

object MilitaryParser {


    lateinit var sortedCompany: List<Company>



    @JvmStatic
    @Throws(IOException::class)
    fun getMilitaryCompany(name: String): Company? {
        val i = Collections.binarySearch(sortedCompany, Company(name))
        if(i > -1) return sortedCompany[i]
        return null;
    }

    @Throws(IOException::class)
    fun init(){
        val link = Jsoup.connect("https://work.mma.go.kr/caisBYIS/search/byjjecgeomsaek.do?eopjong_gbcd=1&pageUnit=10000&pageIndex=1").timeout(20000).ignoreContentType(true).maxBodySize(0).get().body()
        sortedCompany = link.getElementsByClass("title t-alignLt pl20px").mapIndexed { index, element ->
            Company(element.child(0).text().replace("\\([^\\)]*\\)".toRegex(), "").replace("주식회사", "").trim(), element.child(0).attr("href"))
        }
        Collections.sort(sortedCompany)
    }
}