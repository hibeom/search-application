package com.pinkcloud.data.source

import com.pinkcloud.data.api.BaseApiResponse
import com.pinkcloud.data.api.SearchService
import com.pinkcloud.data.util.asDomainModel
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.util.Result
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val searchService: SearchService
) : DataSource, BaseApiResponse() {
    override suspend fun getImageThumbnails(query: String): Result<List<Thumbnail>> {
        return safeApiCall { searchService.getImages(query) }.asDomainModel()
    }

    override suspend fun getVideoThumbnails(query: String): Result<List<Thumbnail>> {
        return safeApiCall { searchService.getVideos(query) }.asDomainModel()
    }
}