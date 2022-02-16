package com.pinkcloud.data.di

import com.pinkcloud.data.source.*
import com.pinkcloud.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    @Binds
    abstract fun bindPagingDataSource(basePagingDataSource: BasePagingDataSource): PagingDataSource

    @Binds
    @Singleton
    abstract fun bindSearchRepository(baseSearchRepository: BaseSearchRepository): SearchRepository
}