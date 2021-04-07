package com.ray.personnel.utils.parser

import com.ray.personnel.company.News
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.Callable

class NaverParser {
    companion object Builder{
        private const val clientId = "5FXvyzH2ML2T8clAPX0c"
        private const val clientSecret = "GQNsUUbmfW"
        private const val NEWS_DEFAULT_ITEM_COUNT = 5
        fun build(content: String, itemCount: Int = NEWS_DEFAULT_ITEM_COUNT): Observable<ArrayList<News>> =
                Observable.fromCallable(parse(content, itemCount)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())

        private fun parse(keyword: String, itemCount: Int): Callable<ArrayList<News>> = Callable<ArrayList<News>>{
            val text = URLEncoder.encode(keyword, "UTF-8") //검색어";
            val apiURL = "https://openapi.naver.com/v1/search/news.json?query=$text&display=$itemCount&start=1&sort=sim"
            val jsonNews = JSONObject(Jsoup.connect(apiURL)
                    .header("X-Naver-Client-Id", clientId)
                    .header("X-Naver-Client-Secret", clientSecret)
                    .method(Connection.Method.GET).ignoreContentType(true).execute().body())
            //println(jsonNews.toString())
            val result = ArrayList<News>()
            for(i in 0 until itemCount){
                val curNewsJson = jsonNews.optJSONArray("items").optJSONObject(i)
                result.add(News(curNewsJson.optString("title"), curNewsJson.optString("description"), curNewsJson.optString("link")))
            }
            return@Callable result
        }
    }

    /*

    public static void search(MainActivity ctx, String keyword, String range) {
        new Thread() {
            public void run() {
                try {
                    String clientId = "FqaCw7XQj1pTpyrzg1uw";//애플리케이션 클라이언트 아이디값";
                    String clientSecret = "CUFNOCeC9q";//애플리케이션 클라이언트 시크릿값";
                    String text = URLEncoder.encode(keyword, "UTF-8"); //검색어";
                    String apiURL;
                    if(range.equals("뉴스")) apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + text + "&display=5&start=1&sort=date";
                    else if(range.equals("블로그")) apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text + "&display=5&start=1&sort=date";
                    else apiURL = "https://openapi.naver.com/v1/search/encyc.json?query="+text+"&display=5&start=1&sort=sim";
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if (responseCode == 200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    JSONObject json = new JSONObject(response.toString());
                    // send runnable object.
                    final Naver naver = new Naver(json);
                    ctx.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ctx.sendMessage(ChatAdapter.SEARCH, naver);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
     */
}