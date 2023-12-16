package com.dicoding.scancare.data.remote.response

import com.google.gson.annotations.SerializedName

data class PutUserProfileResponse(
	@field:SerializedName("message")
	val message: String
)
