package com.dicoding.scancare.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.scancare.data.preference.UserModel
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.data.remote.response.LoginResponse
import com.dicoding.scancare.data.remote.response.PutUserProfileResponse
import com.dicoding.scancare.data.remote.response.RegisterResponse
import com.dicoding.scancare.data.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _loginResult = MutableLiveData<ResultState<LoginResponse>>()
    val loginResult: LiveData<ResultState<LoginResponse>> get() = _loginResult
    private val livedata = MutableLiveData<ResultState<PutUserProfileResponse>>()
    private val _registerResult = MutableLiveData<ResultState<RegisterResponse>>()
    val registerResult: LiveData<ResultState<RegisterResponse>> get() = _registerResult

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = ResultState.Loading
            _loginResult.value = userRepository.loginUser(email, password)
        }
    }

    fun registerUser(address: String,email: String, password: String, name: String) {
        viewModelScope.launch {
            _registerResult.value = ResultState.Loading
            _registerResult.value = userRepository.registerUser(address,email, password, name)
        }
    }

    fun getUserProfile(user_id: String) = userRepository.getUserProfile(user_id)


    fun updateProfile(
        user_id: String,
        username: RequestBody,
        address: RequestBody,
        image: MultipartBody.Part
    ): LiveData<ResultState<PutUserProfileResponse>>{
        viewModelScope.launch {
            val result = userRepository.updateProfile(user_id, username, address, image)
            livedata.value = result
        }
        return livedata
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}