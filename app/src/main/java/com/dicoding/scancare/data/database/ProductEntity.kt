package com.dicoding.scancare.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product")
data class   ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "noBPOM")
    var noBPOM: String,

    @ColumnInfo(name = "productName")
    var productName: String,

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String
)
