package com.example.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginDataResponseModel(
    @SerializedName("accessToken") val accessToken: String,
) {
}