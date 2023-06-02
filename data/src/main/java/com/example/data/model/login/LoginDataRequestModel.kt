package com.example.data.model.login

import com.google.gson.annotations.SerializedName

/*LoginDataRequestModel is data class for login request data*/
data class LoginDataRequestModel(
    @SerializedName("phone") val phone: String,
    @SerializedName("pwd") val pwd: String,
)