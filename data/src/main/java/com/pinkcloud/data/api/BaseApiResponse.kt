package com.pinkcloud.data.api

import retrofit2.Response
import com.pinkcloud.domain.util.Result

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                return Result.Success(body)
            }
            return error("${response.code()} ${response.message()}")
        } catch (exception: Exception) {
            return error(exception.message ?: exception.toString())
        }
    }

    private fun <T> error(message: String): Result<T> {
        return Result.Error("Network Request Error: $message")
    }
}