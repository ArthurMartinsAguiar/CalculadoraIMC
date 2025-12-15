package com.example.imccalculator.data.bmi

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [BMIEntity::class],
    version = 2,
)
abstract class BMIDatabase : RoomDatabase() {

    abstract val bmiDao: BMIDao
}

object BMIDatabaseProvider {

    @Volatile
    private var INSTANCE: BMIDatabase? = null

    fun provide(context: Context): BMIDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                        context.applicationContext,
                        BMIDatabase::class.java,
                        "bmi-calculator"
                    ).fallbackToDestructiveMigration(false)
                .build()
            INSTANCE = instance
            instance
        }
    }
}