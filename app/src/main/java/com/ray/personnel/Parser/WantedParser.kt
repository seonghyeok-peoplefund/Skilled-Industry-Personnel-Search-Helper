package com.ray.personnel.Parser

import android.net.Uri
import com.google.gson.Gson
import com.ray.personnel.Company.Company
import com.ray.personnel.Company.CompanyDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Jsoup
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.jvm.Throws

class WantedParser(var minCount: Int? = null) : Publisher<Int>{

    private val wanted_url: String
        get() = "https://www.wanted.co.kr/api/v4/jobs?country=kr&locations=all&years=-1&limit=$MAX_SEARCH_COUNT&offset=$itemCount&job_sort=job.latest_order&tag_type_id=677"
    private var progress = NO_PROGRESS
    private var jsonCompany: JSONObject? = null
    private var itemCount: Int = 0
    val output: ArrayList<Company> = ArrayList()
    @Throws(IOException::class)
    get(){
        if(progress == NO_PROGRESS) throw IOException("You Can't Access Before Search Has Been Finished")
        return field
    }

    /**
     * step 1 - init Parser, parse military information
     * step 2 - wanted search by MAX_SEARCH_COUNT
     * step 3 - if isMilitary, add company
     * return progress and repeat step 2 - step 3.
     */
    override fun subscribe(s: Subscriber<in Int>) {
        val time_currnet: Long = System.currentTimeMillis()
        when(progress){
            NO_PROGRESS -> {
                Parser.init()
                s.onNext(++progress)
            }
            PARSING_WANTED -> {
                jsonCompany = JSONObject(Jsoup.connect(wanted_url).ignoreContentType(true).execute().body())
                s.onNext(++progress)
            }
            CHECKING_MILITARY -> {
                var i = 0
                while (i < MAX_SEARCH_COUNT) {
                    if (jsonCompany!!.isNull("data")) break
                    if (jsonCompany!!.getJSONArray("data").isNull(i)) break
                    val name = jsonCompany!!.getJSONArray("data").getJSONObject(i).getJSONObject("company")["name"].toString().replace("\\([^\\)]*\\)".toRegex(), "")
                    Parser.getMilitaryCompany(name)?.also{ c ->
                        c.thumbURL = jsonCompany!!.getJSONArray("data").getJSONObject(i).getJSONObject("title_img")["origin"].toString()
                        c.department = jsonCompany!!.getJSONArray("data").getJSONObject(i).getString("position")
                        c.recruit_url = "https://www.wanted.co.kr/wd/"+jsonCompany!!.getJSONArray("data").getJSONObject(i).getString("id")
                        output.add(c)
                    }
                    i ++
                }
                itemCount += i
                if(isParsingFinished()) s.onNext(++progress)
                else s.onNext(--progress)
            }
            SEARCH_FINISHED -> {
                jsonCompany = null
                s.onNext(++progress)
                s.onComplete()
                println("wanted 개수 : $itemCount, military 개수 : "+Parser.sortedCompany.size+", 결과 : "+output.size)
            }
            else -> s.onError(IOException("어케한거여"))
        }
        if(progress <= SEARCH_FINISHED) {
            println("걸린 시간 : "+(System.currentTimeMillis() - time_currnet) / 1000.0+"초 걸림.")
            subscribe(s)
        }
    }

    private fun isParsingFinished(): Boolean = (minCount != null && output.size >= minCount!!) || (jsonCompany != null && jsonCompany!!.getJSONObject("links")["next"].toString() == "null")

    companion object {
        const val MAX_SEARCH_COUNT = 100
        const val MAX_PRESUMED_COUNT = 333
        const val NO_PROGRESS = 0
        const val PARSING_WANTED = NO_PROGRESS + 1
        const val CHECKING_MILITARY = PARSING_WANTED + 1
        const val SEARCH_FINISHED = CHECKING_MILITARY + 1
    }
}
/*
companion object{
    val link = "https://www.wanted.co.kr/api/v4/jobs?country=kr&locations=all&years=-1&limit=1&offset=0&job_sort=job.latest_order&tag_type_id=677"
    val companyList: ArrayList<String>
        get() {
            val img = json.getJsonArray("data").get(i).getJson("title_img").getObject("thumb").toString()
            val name = json.getJsonArray("data").get(i).getJson("company").getObject("name")
        }
    protected String uniToKsc(String uni) throws UnsupportedEncodingException{
        return new String (uni.getBytes("8859_1"),"KSC5601");
    }
    protected String kscToUni(String uni) throws UnsupportedEncodingException{
        return new String (uni.getBytes("KSC5601"),"8859_1");
    }
}
*/
/*
public static ArrayList<String> getCompanyList(int count) throws Exception {
Elements companies = Jsoup.connect("https://www.wanted.co.kr/wdlist/518/677?country=kr&job_sort=job.latest_order&years=-1&locations=all")
       .ignoreContentType(true).get().getElementsByAttributeValue("data-cy","job-card");
ArrayList<String> output = new ArrayList<String>();
System.out.println(companies.size());
Gson gson = new GsonBuilder().create();

for(Element c : companies){
   Company company = new Company("머니브레인","개발자");
   String url = Parser.getMilitaryInformation(company);
   if(url.length() == 0) {
       continue;
   }
   company.setMilitaryInformation(url);
   output.add(gson.toJson(company));
}
return output;
}*/
/*
fun getCompanyList(offset: Int = 0, minCount: Int? = null): ArrayList<String> {
    var i = offset
    var ii = 0
    while (minCount == null || output.size < minCount) {
        val json = JSONObject(Jsoup.connect("https://www.wanted.co.kr/api/v4/jobs?country=kr&locations=all&years=-1&limit=$MAX_SEARCH_COUNT&offset=$i&job_sort=job.latest_order&tag_type_id=677")
                .ignoreContentType(true).execute().body())
        ii = 0
        while (ii < MAX_SEARCH_COUNT) {
            if (json.isNull("data")) break
            if (json.getJSONArray("data").isNull(ii)) break
            val name = json.getJSONArray("data").getJSONObject(ii).getJSONObject("company")["name"].toString()
                    .replace("\\([^\\)]*\\)".toRegex(), "")
            val bool = !isMilitary(name)
            if (bool) {
                ii++
                continue
            }
            val img = json.getJSONArray("data").getJSONObject(ii).getJSONObject("title_img")["thumb"].toString()
            val c = Company(name)
            c.setThumb(img)
            val millitary = getMilitaryInformation(name)
            c.setMilitaryInformation(millitary)
            output.add(Gson().toJson(c))
            ii++
        }
        if (json.getJSONObject("links")["next"].toString() == "null") break
        i += MAX_SEARCH_COUNT
    }
    return output*/