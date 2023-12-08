package com.dicoding.scancare.data.di

import com.dicoding.scancare.data.remote.api.ApiConfig
import com.dicoding.scancare.data.repository.MainRepository

object Injection {

    fun provideRepository(): MainRepository {
        val apiService = ApiConfig.getApiService()
        return MainRepository.getInstance(apiService)
    }

}