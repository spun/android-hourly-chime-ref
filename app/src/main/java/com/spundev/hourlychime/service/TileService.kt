package com.spundev.hourlychime.service

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import androidx.annotation.RequiresApi
import com.spundev.hourlychime.util.ChimeStatePreferencesUtils
import com.spundev.hourlychime.util.ChimeStatePreferencesUtils.CHIME_STATE_KEY
import com.spundev.hourlychime.util.ChimeUtils

@RequiresApi(Build.VERSION_CODES.N)
class TileService : TileService(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        private const val TAG = "TileService"
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        Log.d(TAG, "onTileRemoved: ")
    }

    override fun onTileAdded() {
        super.onTileAdded()
        Log.d(TAG, "onTileAdded: ")
    }

    override fun onStartListening() {
        super.onStartListening()

        // Set the initial/current value
        val stateValue = ChimeStatePreferencesUtils.getState(this)
        // Update tile
        val tile = qsTile
        tile.state = if (stateValue) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()

        // Start listening for changes
        startPreferencesListener(this)
    }

    override fun onStopListening() {
        super.onStopListening()
        // Stop listening for changes
        stopPreferencesListener(this)
    }

    override fun onClick() {
        super.onClick()

        // New tile value
        val newTileState = when (qsTile.state) {
            Tile.STATE_ACTIVE -> false
            Tile.STATE_INACTIVE -> true
            else -> false
        }

        // Update value in preferences
        ChimeStatePreferencesUtils.setState(this, newTileState)

        // Setup alarm
        if (newTileState) {
            ChimeUtils.enableAlarm(this)
        } else {
            ChimeUtils.disableAlarm(this)
        }
    }

    private fun startPreferencesListener(context: Context) {
        ChimeStatePreferencesUtils.getSharedPreferences(context).registerOnSharedPreferenceChangeListener(this)
    }

    private fun stopPreferencesListener(context: Context) {
        ChimeStatePreferencesUtils.getSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if (key == CHIME_STATE_KEY) {
            // Current value in preferences
            val stateValue = ChimeStatePreferencesUtils.getState(this)

            // Update tile
            val tile = qsTile
            tile.state = if (stateValue) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.updateTile()
        }
    }
}
