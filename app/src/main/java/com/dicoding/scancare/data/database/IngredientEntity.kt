package com.dicoding.scancare.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "productNoBPOM")
    var productNoBPOM: String, // Kunci asing untuk menghubungkan ke ProductEntity
    @ColumnInfo(name = "nameIngredients")
    var nameIngredients: String,
    @ColumnInfo(name = "fungsi")
    var fungsi: String
)
