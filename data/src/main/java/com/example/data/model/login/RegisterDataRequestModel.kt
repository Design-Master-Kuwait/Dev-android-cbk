package com.example.data.model.login

import com.google.gson.annotations.SerializedName

/**RegisterDataRequestModel is data class for register request data*/
data class RegisterDataRequestModel(
    @SerializedName("name") var name: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("pwd") var pwd: String? = null,
    @SerializedName("role") var role: String? = null,
)