package com.pinkcloud.data.di

import com.pinkcloud.data.api.SearchService
import com.pinkcloud.data.api.SearchService.Companion.AUTHORIZATION_HEADER_NAME
import com.pinkcloud.data.api.SearchService.Companion.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    const val REST_API_KEY_BINDING = "REST_API_KEY"

    @Provides
    fun provideSearchService(@Named(REST_API_KEY_BINDING) restApiKey: String): SearchService {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader(AUTHORIZATION_HEADER_NAME, restApiKey)
                    .build()
                chain.proceed(request)
            }
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val moshiConverterFactory = MoshiConverterFactory.create(moshi)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(moshiConverterFactory)
            .build()
            .create(SearchService::class.java)
    }
}