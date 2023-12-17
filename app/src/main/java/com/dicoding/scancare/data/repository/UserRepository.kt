package com.dicoding.scancare.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.scancare.data.model.LoginModel
import com.dicoding.scancare.data.model.RegisterModel
import com.dicoding.scancare.data.preference.UserModel
import com.dicoding.scancare.data.preference.UserPreference
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.data.remote.api.AuthApiService
import com.dicoding.scancare.data.remote.response.GetUserProfileResponse
import com.dicoding.scancare.data.remote.response.LoginResponse
import com.dicoding.scancare.data.remote.response.PutUserProfileResponse
import com.dicoding.scancare.data.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException

class UserRepository private constructor(
    private val authApiService: AuthApiService,
    private val userPreference: UserPreference
){

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }


    fun getUserProfile(user_id: String): LiveData<ResultState<GetUserProfileResponse>> = liveData{
        emit(ResultState.Loading)
        try {
            val response = authApiService.getUserProfile(user_id)
            emit(ResultState.Success(response))
        }catch (e: Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }

    suspend fun updateProfile(
        user_id: String,
        username: RequestBody?,
        address: RequestBody?
    ): ResultState<PutUserProfileResponse> {
        return try {
            val response = authApiService.updateProfile(user_id, null, username, address)
            ResultState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }

    suspend fun updateProfileWithImage(
        user_id: String,
        username: RequestBody?,
        address: RequestBody?,
        image: MultipartBody.Part?
    ): ResultState<PutUserProfileResponse> {
        return try {
            val response = authApiService.updateProfile(user_id, image, username, address)
            ResultState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }


    suspend fun loginUser(email: String, password: String): ResultState<LoginResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = authApiService.login(LoginModel(email, password))
                ResultState.Success(response)
            } catch (e: HttpException) {
                handleHttpException(e)
            } catch (e: Exception) {
                ResultState.Error(e.message.toString())
            }
        }

    suspend fun registerUser(
        address: String,
        email: String,
        password: String,
        name: String,
    ): ResultState<RegisterResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApiService.register(RegisterModel(address, email, password, name))
            ResultState.Success(response)
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }

    private fun handleHttpException(e: HttpException): ResultState.Error {
        val error = e.response()?.errorBody()?.string()
        Log.e("HTTP_ERROR", "Error response body: $error")

        try {
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            return ResultState.Error(errorMessage)
        } catch (jsonException: Exception) {
            // Handle jika pesan kesalahan tidak dapat diurai sebagai JSON
            return ResultState.Error("An error occurred during registration.")
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            authApiService: AuthApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(authApiService, userPreference)
            }.also { instance = it }
    }
}