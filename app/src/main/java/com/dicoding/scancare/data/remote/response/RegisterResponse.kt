package com.dicoding.scancare.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("message")
	val message: String
)
