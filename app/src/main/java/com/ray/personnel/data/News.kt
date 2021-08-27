package com.ray.personnel.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val title: String,
    val description: String,
    val url: String
) : Parcelable {
    companion object {
        private val error = News(
            "정보를 불러오는데 실패함.",
            "인터넷 연결을 확인해주세요.",
            ""
        )

        fun getErrorList(size: Int): List<News> {
            return mutableListOf<News>().apply {
                for (i in 0 until size) add(error)
            }
        }
    }
}