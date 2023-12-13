package com.dicoding.scancare.data.di

import android.content.Context
import com.dicoding.scancare.data.database.PredictHistoryDatabase
import com.dicoding.scancare.data.remote.api.ApiConfig
import com.dicoding.scancare.data.repository.MainRepository

object Injection {

    fun provideRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getApiService()
        val predictHistoryDatabase = PredictHistoryDatabase.getInstance(context)
        return MainRepository.getInstance(apiService, predictHistoryDatabase)
    }

}