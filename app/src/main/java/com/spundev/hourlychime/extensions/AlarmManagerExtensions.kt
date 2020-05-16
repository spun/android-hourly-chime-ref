package com.spundev.hourlychime.extensions

import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build

fun AlarmManager.setAlarm(type: Int, triggerAtMillis: Long, operation: PendingIntent) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> setExactAndAllowWhileIdle(type, triggerAtMillis, operation)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> setExact(type, triggerAtMillis, operation)
        else -> set(type, triggerAtMillis, operation)
    }
}