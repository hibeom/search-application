package com.pinkcloud.searchapplication.di

import com.pinkcloud.data.di.RemoteModule.REST_API_KEY_BINDING
import com.pinkcloud.searchapplication.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Named(REST_API_KEY_BINDING)
    fun provideRestApiKey() = "KakaoAK ${BuildConfig.REST_API_KEY}"
}