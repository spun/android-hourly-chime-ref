package com.spundev.hourlychime.util

import android.content.Context
import android.content.SharedPreferences
import com.spundev.hourlychime.R

object ChimeStatePreferencesUtils {

    // Preference keys
    const val CHIME_STATE_KEY = "CHIME_STATE_KEY"

    @JvmStatic
    fun getSharedPreferences(context: Context): SharedPreferences {
        val preferencesFileName = context.getString(R.string.chime_state_preferences_file_key)
        return context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE)
    }

    @JvmStatic
    fun getState(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean(CHIME_STATE_KEY, false)
    }

    @JvmStatic
    fun setState(context: Context, newState: Boolean) {
        val sharedPreferencesEdit = getSharedPreferences(context).edit()
        sharedPreferencesEdit.putBoolean(CHIME_STATE_KEY, newState).apply()
    }
}