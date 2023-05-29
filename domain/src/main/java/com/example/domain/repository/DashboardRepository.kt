package com.example.domain.repository

import com.example.domain.model.dashboard.DashboardResponseModel
import com.example.domain.model.dashboard.ProfileResponseModel
import com.example.domain.utils.Resource

interface DashboardRepository {
    suspend fun postProfileApi(auth :String): Resource<ProfileResponseModel>
    suspend fun postDashboardApi(auth :String): Resource<DashboardResponseModel>

}