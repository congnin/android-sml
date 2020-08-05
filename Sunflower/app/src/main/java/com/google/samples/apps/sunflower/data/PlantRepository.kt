package com.google.samples.apps.sunflower.data

/*
* Repository module for handling data operations.
* */
class PlantRepository private constructor(private val plantDao: PlantDao) {

    fun getPlants() = plantDao.getPlants()

    fun getPlant(plantId: String) = plantDao.getPlant(plantId)

    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int) =
        plantDao.getPlantsWithGrowZoneNumber(growZoneNumber)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PlantRepository? = null

        fun getInstance(plantDao: PlantDao) =
            instance ?: synchronized(this) {
                instance ?: PlantRepository(plantDao).also { instance = it }
            }
    }
}