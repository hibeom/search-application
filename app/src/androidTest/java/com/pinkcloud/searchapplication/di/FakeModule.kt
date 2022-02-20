package com.pinkcloud.searchapplication.di

import com.pinkcloud.data.api.SearchService
import com.pinkcloud.data.di.RemoteModule
import com.pinkcloud.data.fake.DocumentFactory
import com.pinkcloud.data.fake.FAKE_IMAGE_SIZE
import com.pinkcloud.data.fake.FAKE_VIDEO_SIZE
import com.pinkcloud.data.fake.FakeSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteModule::class]
)
@Module
object FakeModule {

    @Provides
    fun provideFakeSearchService(): SearchService {
        val documentFactory = DocumentFactory()
        val fakeImageDocuments = documentFactory.createImageDocuments(FAKE_IMAGE_SIZE)
        val fakeVideoDocuments = documentFactory.createVideoDocuments(FAKE_VIDEO_SIZE)
        return FakeSearchService(fakeImageDocuments, fakeVideoDocuments)
    }
}