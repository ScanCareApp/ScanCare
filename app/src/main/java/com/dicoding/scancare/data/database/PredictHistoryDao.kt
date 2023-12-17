package com.dicoding.scancare.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PredictHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Query("SELECT * FROM product WHERE noBPOM = :noBPOM")
    suspend fun getProductByNoBPOM(noBPOM: String): ProductEntity?

    @Query("SELECT * FROM ingredients WHERE productNoBPOM = :productNoBPOM")
    suspend fun getIngredientsByProductId(productNoBPOM: String): List<IngredientEntity>

    @Query("SELECT * FROM product WHERE user_id = :userId")
    suspend fun getAllScannedProducts(userId: String): List<ProductEntity>

}