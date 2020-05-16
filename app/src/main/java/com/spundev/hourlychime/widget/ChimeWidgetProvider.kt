package com.spundev.hourlychime.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.spundev.hourlychime.MainActivity
import com.spundev.hourlychime.R
import com.spundev.hourlychime.util.ChimeStatePreferencesUtils
import com.spundev.hourlychime.util.ChimeUtils

/**
 * Implementation of App Widget functionality.
 */
class ChimeWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // If the toggle was clicked
        if (TOGGLE_ACTION == intent.action) {
            // Check the current state of the chime
            if (ChimeStatePreferencesUtils.getState(context)) {
                // Update preference value
                ChimeStatePreferencesUtils.setState(context, false)
                // Disable alarm
                ChimeUtils.disableAlarm(context)
            } else {
                // Update preference value
                ChimeStatePreferencesUtils.setState(context, true)
                // Enable alarm
                ChimeUtils.enableAlarm(context)
            }
            // The "updateChimeListWidgets" method is already called from ChimeUtils
            // enableAlarm and disableAlarm.
        }
    }

    companion object {

        private const val TOGGLE_ACTION: String = "toggleChimeWidgetButtonClick"

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.chime_widget)

            // Check if chime is currently enabled
            if (ChimeStatePreferencesUtils.getState(context)) {
                // If enabled, we show the on toggle
                views.setImageViewResource(R.id.appwidget_toggle, R.drawable.ic_widget_toggle_on)
            } else {
                // If enabled, we show the off toggle
                views.setImageViewResource(R.id.appwidget_toggle, R.drawable.ic_widget_toggle_off)
            }

            // Set the click on logo pending intent
            val openAppIntent = Intent(context, MainActivity::class.java)
            val openAppPendingIntent = PendingIntent.getActivity(context, appWidgetId, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.appwidget_logo, openAppPendingIntent)

            // Set the click on toggle pending intent
            views.setOnClickPendingIntent(R.id.appwidget_toggle, getPendingSelfIntent(context, TOGGLE_ACTION))

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        // Widget toggle click Pending intent
        private fun getPendingSelfIntent(context: Context, action: String): PendingIntent {
            val intent = Intent(context, ChimeWidgetProvider::class.java)
            intent.action = action
            return PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        // Public method that allow us to update the widgets if we change the chime state
        // from anywhere in the application
        fun updateChimeListWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, ChimeWidgetProvider::class.java))
            // Trigger data update to handle the GridView widgets and force a data refresh
            // appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list)

            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
}

