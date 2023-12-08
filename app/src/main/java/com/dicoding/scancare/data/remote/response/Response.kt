package com.dicoding.scancare.data.remote.response

import com.google.gson.annotations.SerializedName

data class Response(

    @field:SerializedName("predict_result")
    val productName: String? = null
)

