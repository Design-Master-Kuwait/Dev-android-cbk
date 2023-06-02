package com.example.newbankingproject.ui.deshboard.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.utils.KeyStorePreference
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

/**MainViewModel is view model class is used for call data from usecase*/
@HiltViewModel
class MainViewModel @Inject constructor(
    private val dashBoardUseCase: DashBoardUseCase,
    @ApplicationContext var context: Context?
) : ViewModel() {

    private val profileData = MutableLiveData<Resource<ProfileResponseModel>>()
    val _profileData: LiveData<Resource<ProfileResponseModel>>
        get() = profileData

    private val dashBoardData = MutableLiveData<Resource<DashboardResponseModel>>()
    val _dashBoardData: LiveData<Resource<DashboardResponseModel>>
        get() = dashBoardData

    /**getDashBoardApi is used to call profile api*/
    fun getDashBoardApi() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (Utility.isNetworkAvailable(context)) {
                val response = dashBoardUseCase.getDashBoardApi()
                try {
                    dashBoardData.postValue(response)
                } catch (ex: Exception) {
                    dashBoardData.postValue(Resource.Error(ex.message.toString()))
                }
            } else {
                dashBoardData.postValue(context?.getString(R.string.internetIsNotAvailable)
                    ?.let { Resource.Error(it) })
            }
        }
    }

    /** coroutineExceptionHandler is used to handle the exceptions in coroutine*/
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    /**getProfileApi is used to call the profile api*/
    fun getProfileApi() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (Utility.isNetworkAvailable(context)) {
                val response = dashBoardUseCase.getProfileApi()
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
        private val TAG: String = MainViewModel::class.java.simpleName
    }
}