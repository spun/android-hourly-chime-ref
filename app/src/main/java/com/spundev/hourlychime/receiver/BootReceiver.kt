package com.spundev.hourlychime.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import com.spundev.hourlychime.util.ChimeStatePreferencesUtils
import com.spundev.hourlychime.util.ChimeUtils

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_BOOT_COMPLETED) {
            // If the alarm is currently enabled in the preferences
            val isEnabled = ChimeStatePreferencesUtils.getState(context!!)
            if (isEnabled) {
                // Set the alarm here.
                ChimeUtils.enableAlarm(context)
            }
        }
    }
}
