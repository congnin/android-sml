package com.mindorks.sample.whatsapp.ambients

import androidx.compose.ambientOf
import com.mindorks.sample.whatsapp.data.repository.FakerRepository

val fakerRepository = ambientOf<FakerRepository> { error("Faker Repository not found") }