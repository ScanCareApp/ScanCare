package com.dicoding.scancare.data.remote.response

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("product_details")
	val productDetails: ProductDetails
)

data class IngredientsItem(

	@field:SerializedName("nameIngredients")
	val nameIngredients: String,

	@field:SerializedName("idIngredient")
	val idIngredient: String,

	@field:SerializedName("fungsi")
	val fungsi: String
)

data class ProductDetails(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("NoBPOM")
	val noBPOM: String,

	@field:SerializedName("ingredients")
	val ingredients: List<IngredientsItem>,

	@field:SerializedName("product_name")
	val productName: String
)

