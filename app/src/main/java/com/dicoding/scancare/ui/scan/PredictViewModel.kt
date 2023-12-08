package com.dicoding.scancare.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.data.remote.response.Response
import com.dicoding.scancare.data.repository.MainRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PredictViewModel(private val repository: MainRepository): ViewModel()  {
    private val liveData = MutableLiveData<ResultState<Response>>()
    fun predictImage(file: MultipartBody.Part): LiveData<ResultState<Response>> {
        viewModelScope.launch {
            val result = repository.predictImage(file)
            liveData.value = result
        }
        return liveData
    }
}