package com.example.data.model.deshboard

import com.google.gson.annotations.SerializedName

/**DashboardDataResponseModel is data clas for dashboard api*/
data class DashboardDataResponseModel(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("data") var data: ArrayList<DashboardDataResponse> = arrayListOf(),
    @SerializedName("message") var message: String? = null,
)

data class ProfileData(
    @SerializedName("total_qty") var totalQty: Int? = null,
    @SerializedName("total_price") var totalPrice: Int? = null,
    @SerializedName("product_name") var productName: String? = null,
    @SerializedName("product_image") var productImage: String? = null,
    @SerializedName("unit") var unit: String? = null,
    @SerializedName("increament") var increament: Int? = null,
)

data class DashboardDataResponse(
    @SerializedName("title") var title: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("data") var data: ArrayList<ProfileData> = arrayListOf(),
)