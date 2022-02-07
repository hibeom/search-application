package com.pinkcloud.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("v2/search/image")
    suspend fun getImages(
        @Query("query") query: String = "kakao"
    ): Response<ImageResponse>

    @GET("v2/search/vclip")
    suspend fun getVideos(
        @Query("query") query: String = "kakao"
    ): Response<VideoResponse>

    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val AUTHORIZATION_HEADER_NAME = "Authorization"
    }
}