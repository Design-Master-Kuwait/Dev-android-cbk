package com.example.data

import com.example.data.model.deshboard.DashboardDataResponseModel
import com.example.data.model.deshboard.ProfileDataResponseModel
import com.example.data.model.login.LoginDataRequestModel
import com.example.data.model.login.LoginDataResponseModel
import com.example.data.model.login.RegisterDataRequestModel
import com.example.data.model.login.RegisterDataResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth")
    suspend fun postAuthApi(@Body data: LoginDataRequestModel): Response<LoginDataResponseModel>

    @POST("register")
    suspend fun postRegisterApi(@Body data: RegisterDataRequestModel): Response<RegisterDataResponseModel>


    @GET("users/profile")
    suspend fun getUserProfileApi(@Header("Auth") auth: String): Response<ProfileDataResponseModel>

    @GET("dashboard")
    suspend fun getDashboardApi(@Header("Auth") auth: String): Response<DashboardDataResponseModel>


}