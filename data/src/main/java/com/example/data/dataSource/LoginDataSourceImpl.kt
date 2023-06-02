package com.example.data.dataSource

import com.example.data.ApiService
import com.example.data.model.login.LoginDataRequestModel
import com.example.data.model.login.RegisterDataRequestModel
import com.example.domain.model.LoginResponseModel
import com.example.domain.model.RegisterResponseModel
import com.example.domain.repository.LoginRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

/*LoginDataSourceImpl is used to elaborate the data source of login screen
*/
class LoginDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
) : LoginRepository {
    override suspend fun postAuthApi(phone: String, pwd: String): Resource<LoginResponseModel> {
        return try {
            val data = LoginDataRequestModel(phone, pwd)
            val response = apiService.postAuthApi(data)
            if (response.isSuccessful) {
                val list = response.body()
                LoginResponseModel(list?.accessToken).let { result ->
                    Resource.Success(result)
                }
            } else {
                Resource.Error(response.message())
            }
        } catch (throwable: Exception) {
            Resource.Error(throwable.message.toString())
        }
    }

    override suspend fun postRegisterApi(
        name: String,
        phone: String,
        pwd: String,
        role: String,
    ): Resource<RegisterResponseModel> {
        return try {
            val data = RegisterDataRequestModel(name, phone, pwd, role)
            val response = apiService.postRegisterApi(data)
            if (response.isSuccessful) {
                val list = response.body()
                RegisterResponseModel(list?.success).let { result ->
                    Resource.Success(result)
                }
            } else {
                Resource.Error(response.message())
            }
        } catch (throwable: java.lang.Exception) {
            Resource.Error(throwable.message.toString())
        }
    }
}