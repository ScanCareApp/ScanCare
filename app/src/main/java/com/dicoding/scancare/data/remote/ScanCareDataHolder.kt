package com.dicoding.scancare.data.remote
import com.dicoding.scancare.data.remote.response.IngredientsItem
import com.dicoding.scancare.data.remote.response.ProductDetails

object ScanCareDataHolder {

    private var productDetails: ProductDetails? = null
    private var ingredientsList: List<IngredientsItem?>? = null

    fun updateProductDetails(details: ProductDetails) {
        productDetails = details
    }

    fun updateIngredients(ingredients: List<IngredientsItem?>) {
        ingredientsList = ingredients
    }

    fun getProductDetails(): ProductDetails? {
        return productDetails
    }

    fun getIngredients(): List<IngredientsItem?>? {
        return ingredientsList
    }

}
