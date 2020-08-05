package com.google.samples.apps.sunflower.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.data.PlantRepository

/**
 * The ViewModel for [PlantListFragment].
 */
class PlantListViewModel internal constructor(
    plantRepository: PlantRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val plants: LiveData<List<Plant>> = getSavedGrowZoneNumber().switchMap {
        if (it == NO_GROW_ZONE) {
            plantRepository.getPlants()
        } else {
            plantRepository.getPlantsWithGrowZoneNumber(it)
        }
    }

    fun setGrowZoneNumber(num: Int) {
        savedStateHandle.set(GROW_ZONE_SAVED_STATE_KEY, num)
    }

    fun clearGrowZoneNumber() {
        savedStateHandle.set(GROW_ZONE_SAVED_STATE_KEY, NO_GROW_ZONE)
    }

    fun isFiltered() = getSavedGrowZoneNumber().value != NO_GROW_ZONE

    private fun getSavedGrowZoneNumber(): MutableLiveData<Int> {
        return savedStateHandle.getLiveData(GROW_ZONE_SAVED_STATE_KEY, NO_GROW_ZONE)
    }

    companion object {
        private const val NO_GROW_ZONE = -1
        private const val GROW_ZONE_SAVED_STATE_KEY = "GROW_ZONE_SAVED_STATE_KEY"
    }
}