package com.spundev.hourlychime.util

import android.content.Context
import android.content.SharedPreferences
import com.spundev.hourlychime.R

object ChimeSoundPreferencesUtils {

    // Preference keys
    private const val CHIME_SOUND_TYPE_KEY = "CHIME_SOUND_TYPE_KEY"

    // Sound type values
    const val CHIME_SOUND_TYPE_MEDIA = 0
    const val CHIME_SOUND_TYPE_RING = 1
    const val CHIME_SOUND_TYPE_ALARM = 2

    @JvmStatic
    fun getSharedPreferences(context: Context): SharedPreferences {
        val preferencesFileName = context.getString(R.string.chime_sound_preferences_file_key)
        return context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE)
    }


    @JvmStatic
    fun getSoundType(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt(CHIME_SOUND_TYPE_KEY, CHIME_SOUND_TYPE_MEDIA)
    }

    @JvmStatic
    fun setSoundType(context: Context, newSoundType: Int) {
        val sharedPreferencesEdit = getSharedPreferences(context).edit()
        sharedPreferencesEdit.putInt(CHIME_SOUND_TYPE_KEY, newSoundType).apply()
    }

    @JvmStatic
    fun getSoundTypeFromString(context: Context, soundTypeString: String): Int {
        // Possible string values
        val typeMediaValue = context.getString(R.string.sound_type_media)
        val typeRingValue = context.getString(R.string.sound_type_ring)
        val typeAlarmValue = context.getString(R.string.sound_type_alarm)

        return when (soundTypeString) {
            typeMediaValue -> CHIME_SOUND_TYPE_MEDIA
            typeRingValue -> CHIME_SOUND_TYPE_RING
            typeAlarmValue -> CHIME_SOUND_TYPE_ALARM
            else -> CHIME_SOUND_TYPE_MEDIA
        }
    }

    @JvmStatic
    fun getStringFromSoundType(context: Context, soundType: Int): String {
        // Possible string values
        val typeMediaValue = context.getString(R.string.sound_type_media)
        val typeRingValue = context.getString(R.string.sound_type_ring)
        val typeAlarmValue = context.getString(R.string.sound_type_alarm)

        return when (soundType) {
            CHIME_SOUND_TYPE_MEDIA -> typeMediaValue
            CHIME_SOUND_TYPE_RING -> typeRingValue
            CHIME_SOUND_TYPE_ALARM -> typeAlarmValue
            else -> typeMediaValue
        }
    }

    @JvmStatic
    fun getSoundTypeImage(context: Context, soundTypeString: String): Int {
        return when (getSoundTypeFromString(context, soundTypeString)) {
            CHIME_SOUND_TYPE_MEDIA -> R.drawable.ic_media_black_24dp
            CHIME_SOUND_TYPE_RING -> R.drawable.ic_notifications_black_24dp
            CHIME_SOUND_TYPE_ALARM -> R.drawable.ic_alarm_black_24dp
            else -> R.drawable.ic_media_black_24dp
        }
    }
}