package com.dicoding.scancare.data.di

import android.content.Context
import com.dicoding.scancare.data.database.PredictHistoryDatabase
import com.dicoding.scancare.data.preference.UserPreference
import com.dicoding.scancare.data.preference.dataStore
import com.dicoding.scancare.data.remote.api.ApiConfig
import com.dicoding.scancare.data.remote.api.AuthApiConfig
import com.dicoding.scancare.data.repository.MainRepository
import com.dicoding.scancare.data.repository.UserRepository

object Injection {

    fun provideRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getApiService()
        val predictHistoryDatabase = PredictHistoryDatabase.getInstance(context)
        return MainRepository.getInstance(apiService, predictHistoryDatabase)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val authApiService = AuthApiConfig.getAuthApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(authApiService, pref)
    }

      fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }

}