package com.google.samples.apps.sunflower.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.samples.apps.sunflower.data.UnsplashRepository

class GalleryViewModelFactory(
    private val unsplashRepository: UnsplashRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GalleryViewModel(unsplashRepository) as T
    }
}