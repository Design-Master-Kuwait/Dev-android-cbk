package com.example.domain.model.dashboard

data class DashboardResponseModel(
    var error: Boolean? = null,
    var data: ArrayList<DashboardData> = arrayListOf(),
    var message: String? = null,
)

data class DashboardProfileData(
    var totalQty: Int? = null,
    var totalPrice: Int? = null,
    var productName: String? = null,
    var productImage: String? = null,
    var unit: String? = null,
    var increament: Int? = null,
)


data class DashboardData(
    var title: String? = null,
    var type: String? = null,
    var data: ArrayList<DashboardProfileData> = arrayListOf(),
)