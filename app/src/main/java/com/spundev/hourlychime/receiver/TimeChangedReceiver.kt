package com.spundev.hourlychime.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_TIMEZONE_CHANGED
import android.content.Intent.ACTION_TIME_CHANGED
import com.spundev.hourlychime.util.ChimeStatePreferencesUtils
import com.spundev.hourlychime.util.ChimeUtils

class TimeChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_TIME_CHANGED || intent?.action == ACTION_TIMEZONE_CHANGED) {
            // If the alarm is currently enabled in the preferences
            val isEnabled = ChimeStatePreferencesUtils.getState(context!!)
            if (isEnabled) {
                // Set the alarm here.
                ChimeUtils.enableAlarm(context)
            }
        }
    }
}
