package com.example.data.model.login

import com.google.gson.annotations.SerializedName

data class RegisterDataResponseModel(
    @SerializedName("success") val success: String,
) {
}