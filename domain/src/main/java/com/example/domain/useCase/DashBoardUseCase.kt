package com.example.domain.useCase

import com.example.domain.model.dashboard.DashboardResponseModel
import com.example.domain.model.dashboard.ProfileResponseModel
import com.example.domain.repository.DashboardRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

/**DashBoardUseCase is used to create use case file for dashboard*/
class DashBoardUseCase @Inject constructor(
    private val repository: DashboardRepository,
) {

    suspend fun getProfileApi(): Resource<ProfileResponseModel> {
        return repository.postProfileApi()
    }

    suspend fun getDashBoardApi(): Resource<DashboardResponseModel> {
        return repository.postDashboardApi()
    }


}