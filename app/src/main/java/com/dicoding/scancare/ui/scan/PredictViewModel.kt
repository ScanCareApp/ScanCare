package com.dicoding.scancare.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.scancare.data.database.IngredientEntity
import com.dicoding.scancare.data.database.ProductEntity
import com.dicoding.scancare.data.preference.UserPreference
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.data.remote.ScanCareDataHolder
import com.dicoding.scancare.data.remote.response.Response
import com.dicoding.scancare.data.repository.MainRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PredictViewModel(private val repository: MainRepository, private val userPreference: UserPreference): ViewModel()  {
    private val _apiResponse = MutableLiveData<ResultState<Response>>()
    val apiResponse: LiveData<ResultState<Response>>
        get() = _apiResponse

    private val _scannedProducts = MutableLiveData<List<ProductEntity>>()
    val scannedProducts: LiveData<List<ProductEntity>>
        get() = _scannedProducts

    private val _selectedProduct = MutableLiveData<ProductEntity>()
    val selectedProduct: LiveData<ProductEntity>
        get() = _selectedProduct

    private val _selectedIngredients = MutableLiveData<List<IngredientEntity>>()
    val selectedIngredients: LiveData<List<IngredientEntity>>
        get() = _selectedIngredients


    fun predictImage(file: MultipartBody.Part): LiveData<ResultState<Response>> {
        val liveData = MutableLiveData<ResultState<Response>>()
        viewModelScope.launch {
            try {
                val result = repository.predictImage(file)
                liveData.postValue(result)
                if (result is ResultState.Success) {
                    processApiResponse(result.data) //
                }
            } catch (e: Exception) {
                liveData.postValue(ResultState.Error(e.message ?: "Unknown error"))
            }
        }
        return liveData
    }

    private fun processApiResponse(response: Response) {
        val details = response.productDetails
        val ingredientsList = details.ingredients


        ScanCareDataHolder.updateProductDetails(details)
        ScanCareDataHolder.updateIngredients(ingredientsList)

        viewModelScope.launch {
            val userModel = userPreference.getSession().first()
            val userId = userModel.userId

            val productEntity = ProductEntity(
                noBPOM = details.noBPOM,
                productName = details.productName,
                imageUrl = details.imageUrl,
                user_id = userId

            )
            val ingredientEntities = ingredientsList.map {
                IngredientEntity(
                    productNoBPOM = details.noBPOM,
                    nameIngredients = it?.nameIngredients ?: "",
                    fungsi = it?.fungsi ?: ""
                )
            }

            repository.saveScanResult(productEntity, ingredientEntities)
            getAllScannedProducts(userId)
        }
    }

    fun getAllScannedProducts(userId: String) {
        viewModelScope.launch {
            val products = repository.getAllScannedProducts(userId)
            _scannedProducts.postValue(products)
        }
    }

    fun getProductAndIngredientsByNoBPOM(noBPOM: String) {
        viewModelScope.launch {
            val product = repository.getProductByNoBPOM(noBPOM)
            product?.let {
                _selectedProduct.postValue(it)
                val ingredients = repository.getIngredientsByProductId(it.noBPOM)
                _selectedIngredients.postValue(ingredients)
            }
        }
    }
}