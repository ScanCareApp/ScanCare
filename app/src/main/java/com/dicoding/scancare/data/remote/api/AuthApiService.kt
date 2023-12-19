package com.dicoding.scancare.data.remote.api

import com.dicoding.scancare.data.model.LoginModel
import com.dicoding.scancare.data.model.RegisterModel
import com.dicoding.scancare.data.remote.response.GetUserProfileResponse
import com.dicoding.scancare.data.remote.response.LoginResponse
import com.dicoding.scancare.data.remote.response.PutUserProfileResponse
import com.dicoding.scancare.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApiService {

    @GET("userProfile/{user_id}")
    suspend fun getUserProfile(
        @Path("user_id") user_id: String
    ): GetUserProfileResponse

    @Multipart
    @PUT("userProfile/{user_id}")
    suspend fun updateProfile(
        @Path("user_id") user_id: String,
        @Part file: MultipartBody.Part?,
        @Part("username") username: RequestBody?,
        @Part("address") address: RequestBody?
    ): PutUserProfileResponse

    @POST("signup")
    suspend fun register(
        @Body requestBody: RegisterModel
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body requestBody: LoginModel
    ): LoginResponse
}