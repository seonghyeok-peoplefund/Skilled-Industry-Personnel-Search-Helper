package com.ray.personnel.domain.parser

import com.ray.personnel.data.Company
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

//어렸을 때 Jsoup 이용한 기억을 더듬어 비교하며 인터넷에 있는 내용 가져옴.
//Regex도 필요할 때마다 검색해서 가져옴
object MilitaryParser {
    private var sortedCompany: List<Company>? = null

    @Throws(IOException::class)
    fun getMilitaryCompany(name: String): Company? {
        if (sortedCompany == null) return null
        val i = Collections.binarySearch(sortedCompany, Company(name))
        return if (i > -1) sortedCompany!![i] else null
    }

    @Throws(IOException::class)
    fun getMilitaryInformationByJson() {
        val link = Jsoup.connect("https://work.mma.go.kr/caisBYIS/search/byjjecgeomsaek.do?eopjong_gbcd=1&pageUnit=10000&pageIndex=1")
            .timeout(20000)
            .ignoreContentType(true)
            .maxBodySize(0)
            .get()
            .body()
        sortedCompany = link.getElementsByClass("title t-alignLt pl20px")
            .map { element ->
                Company(
                    title = element.child(0)
                        .text()
                        .replace("\\([^\\)]*\\)".toRegex(), "")
                        .replace("주식회사", "")
                        .trim(),
                    militaryUrl = element.child(0)
                        .attr("href")
                )
            }
            .sorted()
    }
}