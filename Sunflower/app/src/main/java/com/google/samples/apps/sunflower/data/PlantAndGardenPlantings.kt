package com.google.samples.apps.sunflower.data

import androidx.room.Embedded
import androidx.room.Relation

/*
* This class captures the relationships between a [Plant] and a user's [GardenPlanting], which is
* used by Room to fetch the related entities.
* */
data class PlantAndGardenPlantings(
    @Embedded
    val plant: Plant,

    @Relation(parentColumn = "id", entityColumn = "plant_id")
    val gardenPlantings: List<GardenPlanting> = emptyList()
)