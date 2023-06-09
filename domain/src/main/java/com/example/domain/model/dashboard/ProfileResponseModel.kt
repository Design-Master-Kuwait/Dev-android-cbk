package com.example.domain.model.dashboard

/**ProfileResponseModel is model class for profile response data*/
data class ProfileResponseModel(
    var error: Boolean? = null,
    var data: Data? = Data(),
    var message: String? = null,
)

data class Data(
    var id: Int? = null,
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var role: String? = null,
)