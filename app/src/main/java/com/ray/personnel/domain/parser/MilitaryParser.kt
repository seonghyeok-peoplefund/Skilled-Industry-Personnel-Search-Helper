package com.ray.personnel.domain.parser

import com.ray.personnel.data.Company
import org.jsoup.Jsoup
import java.io.IOException
import java.util.Collections
import kotlin.jvm.Throws

object MilitaryParser {
    private var sortedCompany: List<Company>? = null

    @JvmStatic
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