package com.pinkcloud.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("v2/search/image")
    suspend fun getImages(
        @Query("query") query: String = "kakao",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 80,
        @Query("sort") sort: String = "recency"
        ): ImageResponse

    @GET("v2/search/vclip")
    suspend fun getVideos(
        @Query("query") query: String = "kakao",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15,
        @Query("sort") sort: String = "recency"
        ): VideoResponse

    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val AUTHORIZATION_HEADER_NAME = "Authorization"
    }
}