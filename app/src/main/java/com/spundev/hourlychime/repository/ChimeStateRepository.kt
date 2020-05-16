package com.spundev.hourlychime.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.spundev.hourlychime.util.ChimeStatePreferencesUtils
import com.spundev.hourlychime.util.SharedPreferenceBooleanLiveData

class ChimeStateRepository {

    // LiveData of the chime state shared preference
    fun getChimeState(context: Context): LiveData<Boolean> {
        return SharedPreferenceBooleanLiveData(
                ChimeStatePreferencesUtils.getSharedPreferences(context),
                ChimeStatePreferencesUtils.CHIME_STATE_KEY,
                false)
    }
}