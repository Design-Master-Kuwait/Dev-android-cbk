package com.example.newbankingproject.ui.login.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.LoginResponseModel
import com.example.domain.model.RegisterResponseModel
import com.example.domain.useCase.LoginUseCase
import com.example.domain.utils.Resource
import com.example.newbankingproject.R
import com.example.newbankingproject.util.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**LoginViewModel is responsible to handle the apis and data for login screen*/
@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext var context: Context?,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val loginData = MutableLiveData<Resource<LoginResponseModel>>()
    val _loginData: LiveData<Resource<LoginResponseModel>>
        get() = loginData

    private val registerData = MutableLiveData<Resource<RegisterResponseModel>>()
    val _registerData: LiveData<Resource<RegisterResponseModel>>
        get() = registerData

    val customer = "Customer"

    /**postAuthApi is used to call the post auth api*/
    fun postAuthApi(phone: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (Utility.isNetworkAvailable(context)) {
                val response = loginUseCase.postAuthApi(phone, pass)
                try {
                    loginData.postValue(response)
                } catch (ex: Exception) {
                    loginData.postValue(Resource.Error(ex.message.toString()))
                    Log.e(TAG, "postAuthApi: ${ex.message} + ${response.message} ")
                }
            } else {
                loginData.postValue(context?.getString(R.string.internetIsNotAvailable)
                    ?.let { Resource.Error(it) })
            }
        }
    }


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    /**postRegisterApi is used to call the post registration api*/
    fun postRegisterApi(name: String, phone: String, pass: String, role: String = customer) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (Utility.isNetworkAvailable(context)) {
                val response = loginUseCase.postRegisterApi(name, phone, pass, role)
                try {
                    registerData.postValue(response)
                } catch (ex: Exception) {
                    registerData.postValue(Resource.Error(ex.message.toString()))
                }
            } else {
                registerData.postValue(context?.getString(R.string.internetIsNotAvailable)
                    ?.let { Resource.Error(it) })
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        context = null
    }

    companion object{
        private val TAG = LoginViewModel::class.java.simpleName
    }
}