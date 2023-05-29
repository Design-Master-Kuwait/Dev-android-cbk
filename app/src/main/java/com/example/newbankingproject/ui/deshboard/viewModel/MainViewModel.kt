package com.example.newbankingproject.ui.deshboard.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.dashboard.DashboardResponseModel
import com.example.domain.model.dashboard.ProfileResponseModel
import com.example.domain.useCase.DashBoardUseCase
import com.example.domain.utils.Resource
import com.example.newbankingproject.R
import com.example.newbankingproject.util.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val dashBoardUseCase: DashBoardUseCase,
    @ApplicationContext var context: Context?,
) : ViewModel() {

    private val profileData = MutableLiveData<Resource<ProfileResponseModel>>()
    val _profileData: LiveData<Resource<ProfileResponseModel>>
        get() = profileData

    private val dashBoardData = MutableLiveData<Resource<DashboardResponseModel>>()
    val _dashBoardData: LiveData<Resource<DashboardResponseModel>>
        get() = dashBoardData

    val auth: String = ""

    fun getDashBoardApi(phone: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (Utility.isNetworkAvailable(context)) {
                val response = dashBoardUseCase.getDashBoardApi(auth)
                try {
                    dashBoardData.postValue(response)
                } catch (ex: Exception) {
                    dashBoardData.postValue(Resource.Error(ex.message.toString()))
                    Log.e(TAG, "postAuthApi: ${ex.message} + ${response.message} ")
                }
            } else {
                dashBoardData.postValue(context?.getString(R.string.internetIsNotAvailable)
                    ?.let { Resource.Error(it) })
            }
        }
    }


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun getProfileApi() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (Utility.isNetworkAvailable(context)) {
                val response = dashBoardUseCase.getProfileApi(auth)
                try {
                    profileData.postValue(response)
                } catch (ex: Exception) {
                    profileData.postValue(Resource.Error(ex.message.toString()))
                }
            } else {
                profileData.postValue(context?.getString(R.string.internetIsNotAvailable)
                    ?.let { Resource.Error(it) })
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        context = null
    }


    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }
}