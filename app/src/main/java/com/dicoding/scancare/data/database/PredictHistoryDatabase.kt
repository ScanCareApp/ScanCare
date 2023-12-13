package com.dicoding.scancare.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class, IngredientEntity::class],
    version = 1
)
abstract class PredictHistoryDatabase : RoomDatabase() {
    abstract fun predictHistoryDao(): PredictHistoryDao

    companion object{
        @Volatile
        private var INSTANCE: PredictHistoryDatabase? = null

        fun getInstance(context: Context): PredictHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PredictHistoryDatabase::class.java,
                    "predict_history_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}