package com.example.data.dataSource

import com.example.data.ApiService
import com.example.data.model.deshboard.DashboardDataResponse
import com.example.data.model.deshboard.ProfileData
import com.example.data.utils.toApiFailure
import com.example.domain.model.dashboard.DashboardData
import com.example.domain.model.dashboard.DashboardProfileData
import com.example.domain.model.dashboard.DashboardResponseModel
import com.example.domain.model.dashboard.Data
import com.example.domain.model.dashboard.ProfileResponseModel
import com.example.domain.repository.DashboardRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

class DashboardDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
) : DashboardRepository {
    override suspend fun postProfileApi(auth: String): Resource<ProfileResponseModel> {
        return try {
            val response = apiService.getUserProfileApi(auth)
            if (response.isSuccessful) {
                val list = response.body()
                list.let {
                    Resource.Success(
                        ProfileResponseModel(
                            list?.error,
                            Data(
                                list?.data?.id,
                                list?.data?.name,
                                list?.data?.phone,
                                list?.data?.email,
                                list?.data?.role
                            ),
                            list?.message
                        )
                    )
                }
            } else {
                Resource.Error(response.message())
            }
        } catch (throwable: Throwable) {
            throwable.toApiFailure().let { error ->
                Resource.Error(error.toString())
            }
        }
    }

    override suspend fun postDashboardApi(auth: String): Resource<DashboardResponseModel> {
        return try {
            val response = apiService.getDashboardApi(auth)
            if (response.isSuccessful) {
                val list = response.body()
                list.let {
                    Resource.Success(
                        DashboardResponseModel(
                            it?.error,
                            setListData(it?.data),
                            it?.message
                        )
                    )
                }
            } else {
                Resource.Error(response.message())
            }
        } catch (throwable: Throwable) {
            throwable.toApiFailure().let { error ->
                Resource.Error(error.toString())
            }
        }
    }

    private fun setListData(data: ArrayList<DashboardDataResponse>?): ArrayList<DashboardData> {
        val list = arrayListOf<DashboardData>()
        if (data != null) {
            for (item in data) {
                list.add(
                    DashboardData(
                        item.title,
                        item.type,
                        setDashboardProfileDataList(item.data)
                    )
                )
            }
        }
        return list
    }

    private fun setDashboardProfileDataList(data: ArrayList<ProfileData>): ArrayList<DashboardProfileData> {
        val list = arrayListOf<DashboardProfileData>()
        if (data != null) {
            for (item in data) {
                list.add(
                    DashboardProfileData(
                        item.totalQty,
                        item.totalPrice,
                        item.productName,
                        item.productImage,
                        item.unit,
                        item.increament
                    )
                )
            }
        }
        return list
    }
}