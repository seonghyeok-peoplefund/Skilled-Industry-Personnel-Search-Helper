package com.ray.personnel.domain.parser

import com.ray.personnel.data.News
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.net.URLEncoder
import java.util.concurrent.Callable

//네이버 검색 API 인터넷 예제 가져와 위에서 가져와 사용하던 RX, Jsoup 결합시킴.
object NaverParser {
    private const val clientId = "5FXvyzH2ML2T8clAPX0c" //TODO("다른곳으로 보내야함")

    private const val clientSecret = "GQNsUUbmfW"

    private const val NEWS_DEFAULT_ITEM_COUNT = 5

    private val errorList = News.getErrorList(NEWS_DEFAULT_ITEM_COUNT)

    fun getNaverNews(
        content: String,
        itemCount: Int = NEWS_DEFAULT_ITEM_COUNT,
        onSuccess: Consumer<in List<News>>,
        onError: Consumer<in Throwable>
    ): Disposable {
        return Single.fromCallable(parse(content, itemCount))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onSuccess, onError)
    }

    private fun parse(
        keyword: String,
        itemCount: Int
    ): Callable<List<News>> {
        return Callable<List<News>> {
            val text = URLEncoder.encode(keyword, "UTF-8")
            val apiURL = "https://openapi.naver.com/v1/search/news.json?query=$text&display=$itemCount&start=1&sort=sim"
            val jsonNews = runCatching {
                Jsoup.connect(apiURL)
                    .header("X-Naver-Client-Id", clientId)
                    .header("X-Naver-Client-Secret", clientSecret)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute()
                    .body()
            }.mapCatching {
                JSONObject(it).optJSONArray("items")
            }.getOrElse {
                return@Callable errorList
            }
            // } else if { 처럼 한 줄로 붙여야 할 것 같음.
            val result = mutableListOf<News>()
            for (i in 0 until itemCount) {
                val curNewsJson = jsonNews.optJSONObject(i)
                result.add(
                    News(
                        curNewsJson.optString("title"),
                        curNewsJson.optString("description"),
                        curNewsJson.optString("link")
                    )
                )
            }
            return@Callable result
        }
    }
}