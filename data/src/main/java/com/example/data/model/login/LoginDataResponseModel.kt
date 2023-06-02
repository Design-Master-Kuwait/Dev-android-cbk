package com.example.data.model.login

import com.google.gson.annotations.SerializedName

/**LoginDataResponseModel is data class for login response data*/
data class LoginDataResponseModel(
    @SerializedName("accessToken") val accessToken: String,
)