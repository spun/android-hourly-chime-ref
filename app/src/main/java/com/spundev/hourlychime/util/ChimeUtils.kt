package com.spundev.hourlychime.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.spundev.hourlychime.widget.ChimeWidgetProvider
import com.spundev.hourlychime.extensions.setAlarm
import com.spundev.hourlychime.receiver.PlayAlarmReceiver
import com.spundev.hourlychime.receiver.BootReceiver
import java.util.*

object ChimeUtils {

    @JvmStatic
    fun createAlarmPendingIntent(context: Context): PendingIntent {
        // Alarm pending intent
        return Intent(context, PlayAlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
    }

    @JvmStatic
    fun setupNextAlarm(context: Context) {
        // Next alarm time
        val nextAlarm = Calendar.getInstance()
        nextAlarm.add(Calendar.HOUR, 1)
        nextAlarm.set(Calendar.MINUTE, 0)
        nextAlarm.set(Calendar.SECOND, 0)

        // Alarm pending intent
        val alarmIntent = createAlarmPendingIntent(context)

        // Set alarm
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.setAlarm(AlarmManager.RTC_WAKEUP, nextAlarm.timeInMillis, alarmIntent)
    }

    @JvmStatic
    fun enableAlarm(context: Context) {
        // Setup next alarm
        setupNextAlarm(context)

        // Enable boot receiver
        val receiver = ComponentName(context, BootReceiver::class.java)
        context.packageManager?.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )

        // Update widgets state
        ChimeWidgetProvider.updateChimeListWidgets(context)
    }

    @JvmStatic
    fun disableAlarm(context: Context) {
        // Cancel alarm
        val alarmIntent = createAlarmPendingIntent(context)
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.cancel(alarmIntent)

        // Disable boot receiver
        val receiver = ComponentName(context, BootReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        )

        // Update widgets state
        ChimeWidgetProvider.updateChimeListWidgets(context)
    }
}