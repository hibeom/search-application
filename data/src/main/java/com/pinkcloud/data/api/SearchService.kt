package com.pinkcloud.data.api

import com.pinkcloud.data.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("v2/search/image")
    suspend fun getImages(
        @Query("query") search: String = "kakao"
    ): Response<ImageResponse>

    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val AUTHORIZATION_HEADER_NAME = "Authorization"
    }
}