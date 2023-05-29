package com.example.domain.utils

sealed class AppError {
    data class ApiError(val statusCode: Int, val message: String) : AppError()
    data class ApiFailure(val throwable: Throwable) : AppError()
}