package com.dicoding.scancare.data.repository

import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.data.remote.api.ApiService
import com.dicoding.scancare.data.remote.response.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException

class MainRepository private constructor(
    private val apiService: ApiService
){
    suspend fun predictImage(
        imageFile: MultipartBody.Part
    ): ResultState<Response> {
        return try {
            val response = apiService.predictImage(imageFile)
            ResultState.Success(response)
        }catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            ResultState.Error(errorMessage)
        }catch (e: Exception){
            ResultState.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService)
            }.also { instance = it }
    }

}