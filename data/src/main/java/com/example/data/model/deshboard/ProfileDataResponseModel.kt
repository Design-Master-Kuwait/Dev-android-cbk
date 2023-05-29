package com.example.data.model.deshboard

import com.google.gson.annotations.SerializedName

data class ProfileDataResponseModel(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("data") var data: ProfileDataResponse? = ProfileDataResponse(),
    @SerializedName("message") var message: String? = null,
)

data class ProfileDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("role") var role: String? = null,
)