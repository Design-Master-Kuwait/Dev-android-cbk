package com.example.data.model.login

import com.google.gson.annotations.SerializedName

/*RegisterDataResponseModel is model class for registration response data*/
data class RegisterDataResponseModel(
    @SerializedName("success") val success: String,
)