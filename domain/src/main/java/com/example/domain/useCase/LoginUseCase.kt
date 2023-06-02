package com.example.domain.useCase

import com.example.domain.model.LoginResponseModel
import com.example.domain.model.RegisterResponseModel
import com.example.domain.repository.LoginRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

/**LoginUseCase is used to create use case login*/
class LoginUseCase @Inject constructor(
    private val repository: LoginRepository,
) {

    suspend fun postAuthApi(phone: String, pwd: String): Resource<LoginResponseModel> {
        return repository.postAuthApi(phone, pwd)
    }

    suspend fun postRegisterApi(
        name: String,
        phone: String,
        pwd: String,
        role: String,
    ): Resource<RegisterResponseModel> {
        return repository.postRegisterApi(name, phone, pwd, role)
    }
}