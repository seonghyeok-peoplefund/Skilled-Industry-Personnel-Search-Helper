package com.ray.personnel.utils.parser

import com.ray.personnel.company.Company
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

object MilitaryParser {

    val link = Jsoup.connect("https://work.mma.go.kr/caisBYIS/search/byjjecgeomsaek.do?eopjong_gbcd=1&pageUnit=10000&pageIndex=1").timeout(20000).ignoreContentType(true).maxBodySize(0).get().body()

    val sortedCompany = link.getElementsByClass("title t-alignLt pl20px").mapIndexed { index, element ->
        Company(element.child(0).text().replace("\\([^\\)]*\\)".toRegex(), "").replace("주식회사", "").trim(), element.child(0).attr("href"))
    }



    @JvmStatic
    @Throws(IOException::class)
    fun getMilitaryCompany(name: String): Company? {
        val i = Collections.binarySearch(sortedCompany, Company(name))
        if(i > -1) return sortedCompany[i]
        return null;
    }
    fun init(){
        Collections.sort(sortedCompany)
    }
}