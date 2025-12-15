package com.example.imccalculator.data.bmi

import com.example.imccalculator.model.bmi.BMI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BMIRepositoryImpl(
    private val dao: BMIDao
) : BMIRepository {

    override suspend fun insert(
        name: String,
        age: Int,
        gender: String,
        weight: Double,
        height: Double,
        bmi: Double,
        bmiClass: String,
        tmb: Double,
        idealWeight: Double,
        dailyCalories: Double,
        activityLevelFactor: Double,
        timestamp: Long
    ) {
        val entity = BMIEntity(
            name = name,
            age = age,
            gender = gender,
            weight = weight,
            height = height,
            bmi = bmi,
            bmiClass = bmiClass,
            tmb = tmb,
            idealWeight = idealWeight,
            dailyCalories = dailyCalories,
            activityLevelFactor = activityLevelFactor,
            timestamp = timestamp
        )

        dao.insert(entity)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun delete(bmi: BMI) {
        val entity = BMIEntity(
            id = bmi.id,
            name = bmi.name,
            age = bmi.age,
            gender = bmi.gender,
            weight = bmi.weight,
            height = bmi.height,
            bmi = bmi.bmi,
            bmiClass = bmi.bmiClass,
            tmb = bmi.tmb,
            idealWeight = bmi.idealWeight,
            dailyCalories = bmi.dailyCalories,
            activityLevelFactor = bmi.activityLevelFactor,
            timestamp = bmi.timestamp
        )
        dao.delete(entity)
    }

    override fun getAll(): Flow<List<BMI>> {
        return dao.getAll().map { entities ->
            entities.map {
                entity -> BMI(
                    id = entity.id,
                    name = entity.name,
                    age = entity.age,
                    gender = entity.gender,
                    weight = entity.weight,
                    height = entity.height,
                    bmi = entity.bmi,
                    bmiClass = entity.bmiClass,
                    tmb = entity.tmb,
                    idealWeight = entity.idealWeight,
                    dailyCalories = entity.dailyCalories,
                    activityLevelFactor = entity.activityLevelFactor,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    override suspend fun getById(id: Long): BMI {
        val entity = dao.getById(id)
        return BMI(
                id = entity.id,
                name = entity.name,
                age = entity.age,
                gender = entity.gender,
                weight = entity.weight,
                height = entity.height,
                bmi = entity.bmi,
                bmiClass = entity.bmiClass,
                tmb = entity.tmb,
                idealWeight = entity.idealWeight,
                dailyCalories = entity.dailyCalories,
                activityLevelFactor = entity.activityLevelFactor,
                timestamp = entity.timestamp
        )
    }
}