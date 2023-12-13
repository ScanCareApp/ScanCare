package com.dicoding.scancare.data.repository

import com.dicoding.scancare.data.database.IngredientEntity
import com.dicoding.scancare.data.database.PredictHistoryDatabase
import com.dicoding.scancare.data.database.ProductEntity
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.data.remote.api.ApiService
import com.dicoding.scancare.data.remote.response.Response
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.HttpException

class MainRepository private constructor(
    private val apiService: ApiService,
    private val predictHistoryDatabase: PredictHistoryDatabase
) {
    suspend fun predictImage(
        imageFile: MultipartBody.Part
    ): ResultState<Response> {
        return try {
            val response = apiService.predictImage(imageFile)
            ResultState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }

    suspend fun saveScanResult(productEntity: ProductEntity, ingredients: List<IngredientEntity>) {

        predictHistoryDatabase.predictHistoryDao().insertProduct(productEntity)
        predictHistoryDatabase.predictHistoryDao().insertIngredients(ingredients)
    }

    suspend fun getAllScannedProducts(): List<ProductEntity>{
        return predictHistoryDatabase.predictHistoryDao().getAllScannedProducts()
    }

    suspend fun getProductByNoBPOM(noBPOM: String): ProductEntity? {
        return predictHistoryDatabase.predictHistoryDao().getProductByNoBPOM(noBPOM)
    }

    suspend fun getIngredientsByProductId(productNoBPOM: String): List<IngredientEntity> {
        return predictHistoryDatabase.predictHistoryDao().getIngredientsByProductId(productNoBPOM)
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            apiService: ApiService,
            predictHistoryDatabase: PredictHistoryDatabase

        ) =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService, predictHistoryDatabase)
            }.also { instance = it }
    }

}