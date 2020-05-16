package com.spundev.hourlychime.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.spundev.hourlychime.util.ChimeSoundPreferencesUtils

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    var soundType: Int = ChimeSoundPreferencesUtils.getSoundType(application)
        set(value) {
            ChimeSoundPreferencesUtils.setSoundType(getApplication(), value)
            field = value
        }
}