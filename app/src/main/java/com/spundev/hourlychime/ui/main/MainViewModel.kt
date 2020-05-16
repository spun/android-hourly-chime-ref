package com.spundev.hourlychime.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.spundev.hourlychime.repository.ChimeStateRepository
import com.spundev.hourlychime.util.ChimeStatePreferencesUtils
import com.spundev.hourlychime.util.ChimeUtils

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Chime state preference repository
    private val chimeStateRepository = ChimeStateRepository()

    // Chime state LiveData
    val isChimeEnabled: LiveData<Boolean> =
            Transformations.map(chimeStateRepository.getChimeState(getApplication())) { it }

    fun enableAlarm() {
        // Update preference value
        ChimeStatePreferencesUtils.setState(getApplication(), true)
        // Enable alarm
        ChimeUtils.enableAlarm(getApplication())
    }

    fun disableAlarm() {
        // Update preference value
        ChimeStatePreferencesUtils.setState(getApplication(), false)
        // Disable alarm
        ChimeUtils.disableAlarm(getApplication())
    }
}