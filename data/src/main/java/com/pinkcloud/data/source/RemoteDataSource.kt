package com.pinkcloud.data.source

import com.pinkcloud.data.api.BaseApiResponse
import com.pinkcloud.data.api.SearchService
import com.pinkcloud.data.util.asDomainModel
import com.pinkcloud.domain.model.Image
import com.pinkcloud.domain.util.Result
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val searchService: SearchService
) : DataSource, BaseApiResponse() {
    override suspend fun getImages(query: String): Result<List<Image>> {
        return safeApiCall { searchService.getImages(query) }.asDomainModel()
    }
}