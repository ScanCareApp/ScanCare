package com.dicoding.scancare.data.remote.api

import com.dicoding.scancare.data.remote.response.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict_image")
    suspend fun predictImage(
        @Part file: MultipartBody.Part,
    ): Response
}