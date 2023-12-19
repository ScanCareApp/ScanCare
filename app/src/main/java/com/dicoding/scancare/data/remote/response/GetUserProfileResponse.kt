package com.dicoding.scancare.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetUserProfileResponse(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("creationDate")
	val creationDate: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
