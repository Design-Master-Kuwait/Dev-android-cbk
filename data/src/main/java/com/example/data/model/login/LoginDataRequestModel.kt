package com.example.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginDataRequestModel(
    @SerializedName("phone") val phone: String,
    @SerializedName("pwd") val pwd: String,
) {
}