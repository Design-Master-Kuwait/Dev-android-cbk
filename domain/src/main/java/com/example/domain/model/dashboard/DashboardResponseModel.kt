package com.example.domain.model.dashboard

/**DashboardResponseModel is model class for dashboard response*/
data class DashboardResponseModel(
    var error: Boolean? = null,
    var data: ArrayList<DashboardData> = arrayListOf(),
    var message: String? = null,
) {
    override fun toString(): String {
        return "DashboardResponseModel(error=$error, data=$data, message=$message)"
    }
}

data class DashboardProfileData(
    var totalQty: Int? = null,
    var totalPrice: Int? = null,
    var productName: String? = null,
    var productImage: String? = null,
    var unit: String? = null,
    var increament: Int? = null,
) {
    override fun toString(): String {
        return "DashboardProfileData(totalQty=$totalQty, totalPrice=$totalPrice, productName=$productName, productImage=$productImage, unit=$unit, increament=$increament)"
    }
}

data class DashboardData(
    var title: String? = null,
    var type: String? = null,
    var data: ArrayList<DashboardProfileData> = arrayListOf(),
) {
    override fun toString(): String {
        return "DashboardData(title=$title, type=$type, data=$data)"
    }
}