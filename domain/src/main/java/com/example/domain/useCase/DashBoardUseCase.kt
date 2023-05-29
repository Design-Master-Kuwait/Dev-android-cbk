package com.example.domain.useCase

import com.example.domain.model.dashboard.DashboardResponseModel
import com.example.domain.model.dashboard.ProfileResponseModel
import com.example.domain.repository.DashboardRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

class DashBoardUseCase @Inject constructor(
    private val repository: DashboardRepository,
) {

    suspend fun getProfileApi(auth: String): Resource<ProfileResponseModel> {
        return repository.postProfileApi(auth)
    }

    suspend fun getDashBoardApi(
        auth: String,
    ): Resource<DashboardResponseModel> {
        return repository.postDashboardApi(auth)
    }


}