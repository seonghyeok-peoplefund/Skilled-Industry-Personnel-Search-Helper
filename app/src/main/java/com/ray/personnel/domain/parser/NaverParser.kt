package com.ray.personnel.domain.parser

import android.util.Log
import com.ray.personnel.data.News
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.Callable

object NaverParser {
    private const val TAG = "NaverParser"
    private const val clientId = "5FXvyzH2ML2T8clAPX0c" //TODO("다른곳으로 보내야함")
    private const val clientSecret = "GQNsUUbmfW"
    private const val NEWS_DEFAULT_ITEM_COUNT = 5
    private val nothing = listOf(
        News(),
        News(),
        News(),
        News(),
        News()
    )

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
            //적절한 방법이 없지만 옳은 방법
            val jsonNews: JSONArray = JSONObject(
                try {
                    Jsoup.connect(apiURL)
                        .header("X-Naver-Client-Id", clientId)
                        .header("X-Naver-Client-Secret", clientSecret)
                        .method(Connection.Method.GET)
                        .ignoreContentType(true)
                        .execute()
                        .body()
                } catch (e: IOException) {
                    Log.e(TAG, "통신 결과가 올바르지 못합니다.", e)
                    return@Callable nothing
                }
            )
                .optJSONArray("items")
                ?: return@Callable nothing
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