package com.dicoding.scancare

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.scancare.data.di.Injection
import com.dicoding.scancare.data.repository.MainRepository
import com.dicoding.scancare.data.repository.UserRepository
import com.dicoding.scancare.ui.register.UserViewModel
import com.dicoding.scancare.ui.scan.PredictViewModel

class ViewModelFactory private constructor(private val mainRepository: MainRepository, private val userRepository: UserRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PredictViewModel::class.java)) {
            return PredictViewModel(mainRepository) as T
        }
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context), Injection.provideUserRepository(context))
        }.also { instance = it }
    }
}