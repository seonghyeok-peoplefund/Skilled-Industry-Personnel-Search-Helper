package com.ray.personnel.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val searchTitle: String = "정보를 불러오는데 실패함.",
    val searchDescription: String = "인터넷 연결을 확인해주세요.",
    val searchURL: String = "https://www.naver.com"
) : Parcelable