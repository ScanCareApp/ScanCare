package com.dicoding.scancare.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("token")
	val token: String
)
