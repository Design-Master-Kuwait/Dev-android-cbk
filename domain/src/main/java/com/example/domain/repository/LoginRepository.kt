package com.example.domain.repository

import com.example.domain.model.LoginResponseModel
import com.example.domain.model.RegisterResponseModel
import com.example.domain.utils.Resource

/**LoginRepository is repository for auth and register api*/
interface LoginRepository {
    suspend fun postAuthApi(phone : String , pwd : String):Resource<LoginResponseModel>
    suspend fun postRegisterApi(name : String , phone : String , pwd: String , role:String ):Resource<RegisterResponseModel>

}