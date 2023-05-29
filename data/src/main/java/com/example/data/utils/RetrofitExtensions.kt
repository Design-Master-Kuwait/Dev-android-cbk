package com.example.data.utils

import com.example.domain.utils.AppError
import org.json.JSONObject
import retrofit2.Response


fun Throwable.toApiFailure(): AppError {
    return AppError.ApiFailure(this)
}

fun <T> Response<T>.toApiError(): AppError {
    val statusCode = code()
    val errorMessage = getErrorMessage(errorBody()?.string())
    return AppError.ApiError(statusCode, errorMessage)

}

private fun getErrorMessage(errorJson: String?): String {
    if (errorJson == null || errorJson.toString().isEmpty()) {
        return ""
    }

    return try {
        val errorJsonObject = JSONObject(errorJson)
        errorJsonObject.getString("message")
    } catch (exception: Exception) {
        ""
    }
}