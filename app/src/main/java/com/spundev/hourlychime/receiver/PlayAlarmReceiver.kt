package com.spundev.hourlychime.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import com.spundev.hourlychime.R
import com.spundev.hourlychime.util.ChimeSoundPreferencesUtils
import com.spundev.hourlychime.util.ChimeStatePreferencesUtils
import com.spundev.hourlychime.util.ChimeUtils

class PlayAlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "PlayAlarmReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        // Check the sound type preference al play
        when (ChimeSoundPreferencesUtils.getSoundType(context!!)) {
            ChimeSoundPreferencesUtils.CHIME_SOUND_TYPE_MEDIA -> playAsMedia(context)
            ChimeSoundPreferencesUtils.CHIME_SOUND_TYPE_RING -> playAsRing(context)
            ChimeSoundPreferencesUtils.CHIME_SOUND_TYPE_ALARM -> playAsAlarm(context)
            else -> playAsMedia(context)
        }

        // Check if the alarm is currently enabled in preferences. If we received the broadcast
        // we could assume that the alarm is already enabled, but we are using the same
        // broadcast receiver for the "Test chime" button in settings.
        val isEnabled = ChimeStatePreferencesUtils.getState(context)
        if (isEnabled) {
            // Set the alarm here.
            ChimeUtils.setupNextAlarm(context)
        }
    }


    private fun playAsMedia(context: Context) {
        // Play sound as media
        val mp = MediaPlayer.create(context, R.raw.chime)
        mp.setOnCompletionListener { it.release() }
        mp.start()
    }

    private fun playAsRing(context: Context) {
        // Play sound as ring
        try {
            // Prepare media player
            val mp = MediaPlayer()
            val mediaPath = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.chime)
            mp.setDataSource(context, mediaPath)
            mp.setOnCompletionListener { it.release() }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val attrs = AudioAttributes.Builder()
                        // .setUsage(AudioAttributes.USAGE_ALARM)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                mp.setAudioAttributes(attrs)
            } else {
                // mp.setAudioStreamType(AudioManager.STREAM_ALARM)
                mp.setAudioStreamType(AudioManager.STREAM_NOTIFICATION)
            }

            // Play
            mp.prepare()
            mp.start()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "playAsMedia: ", e)
        }
    }


    private fun playAsAlarm(context: Context) {
        // Play sound as alarm
        try {
            // Prepare media player
            val mp = MediaPlayer()
            val mediaPath = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.chime)
            mp.setDataSource(context, mediaPath)
            mp.setOnCompletionListener { it.release() }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val attrs = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                mp.setAudioAttributes(attrs)
            } else {
                mp.setAudioStreamType(AudioManager.STREAM_ALARM)
            }

            // Play
            mp.prepare()
            mp.start()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "playAsMedia: ", e)
        }
    }
}
