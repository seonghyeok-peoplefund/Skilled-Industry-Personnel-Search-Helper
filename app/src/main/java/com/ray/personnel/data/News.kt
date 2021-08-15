package com.ray.personnel.data

class News(val searchTitle: String, val searchDescription: String, val searchURL: String){

    companion object{
        fun nothingBuilder(): News{
            return News("정보를 불러오는데 실패함.", "인터넷 연결을 확인해주세요.", "https://www.naver.com")
        }
    }

}