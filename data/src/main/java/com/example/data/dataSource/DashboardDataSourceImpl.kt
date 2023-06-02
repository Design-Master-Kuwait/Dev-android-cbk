package com.example.data.dataSource

import android.util.Log
import com.example.data.ApiService
import com.example.data.model.deshboard.DashboardDataResponse
import com.example.data.model.deshboard.ProfileData
import com.example.domain.model.dashboard.DashboardData
import com.example.domain.model.dashboard.DashboardProfileData
import com.example.domain.model.dashboard.DashboardResponseModel
import com.example.domain.model.dashboard.Data
import com.example.domain.model.dashboard.ProfileResponseModel
import com.example.domain.repository.DashboardRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

/**DashboardDataSourceImpl is data source class for dashboard*/
class DashboardDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
) : DashboardRepository {
    override suspend fun postProfileApi(): Resource<ProfileResponseModel> {
        return try {
            val response = apiService.getUserProfileApi()
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
                Log.e(TAG, "postProfileApi: ${response.message()}")
                Resource.Error(response.message())
            }
        } catch (throwable: Exception) {
            Log.e(TAG, "postProfileApi: ${throwable.message}")
            Resource.Error(throwable.toString())
        }
    }

    override suspend fun postDashboardApi(): Resource<DashboardResponseModel> {
        return try {
            val response = apiService.getDashboardApi()
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
                Log.e(TAG, "postDashboardApi: $response.message()")
                Resource.Error(response.message())
            }
        } catch (throwable: java.lang.Exception) {
            Log.e(TAG, "postDashboardApi: ${throwable.message}")
            Resource.Error(throwable.toString())
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

    companion object {
        val TAG: String? = DashboardDataSourceImpl::class.java.simpleName
    }
}