package com.spundev.hourlychime.ui.settings

import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.spundev.hourlychime.R
import com.spundev.hourlychime.ui.settings.adapter.SoundTypeSpinnerAdapter
import com.spundev.hourlychime.util.ChimeSoundPreferencesUtils
import com.spundev.hourlychime.util.ChimeUtils

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingsFragment : Fragment() {

    companion object {
        private const val TAG = "SettingsFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        // Sound type Spinner
        val soundTypeSpinner: Spinner = rootView.findViewById(R.id.soundTypeSpinner)

        // Spinner adapter
        val soundTypesArray = resources.getStringArray(R.array.array_sound_types)
        val soundTypeSpinnerAdapter = SoundTypeSpinnerAdapter(
                requireContext(),
                soundTypesArray
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            soundTypeSpinner.adapter = adapter
        }

        // get the view model
        val viewModel: SettingsViewModel by viewModels()
        // set the current value
        viewModel.soundType.let {
            // Retrieve sound type from string
            ChimeSoundPreferencesUtils.getStringFromSoundType(requireContext(), it)
        }.also {
            val spinnerPosition = soundTypeSpinnerAdapter.getPosition(it)
            soundTypeSpinner.setSelection(spinnerPosition)
        }

        // Spinner listener
        soundTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get selected value
                val selectedValue = parent.getItemAtPosition(position) as String

                // Convert string value to valid sound type
                ChimeSoundPreferencesUtils.getSoundTypeFromString(
                        requireContext(),
                        selectedValue
                ).also {
                    // Change volume controls
                    updateVolumeControls(it)
                    // Update view model
                    viewModel.soundType = it
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d(TAG, "onNothingSelected: ")
            }
        }

        // Test chime button
        val chimeTestButton = rootView.findViewById<Button>(R.id.chimeTestButton)
        chimeTestButton.setOnClickListener {
            ChimeUtils.createAlarmPendingIntent(requireContext()).send()
            Snackbar.make(rootView, getString(R.string.settings_playing_chime_message), Snackbar.LENGTH_SHORT).show()
        }

        return rootView
    }


    override fun onResume() {
        super.onResume()
        ChimeSoundPreferencesUtils.getSoundType(requireContext()).also {
            updateVolumeControls(it)
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
    }

    // ensure that volume controls adjust the correct stream
    private fun updateVolumeControls(soundType: Int) {

        when (soundType) {
            ChimeSoundPreferencesUtils.CHIME_SOUND_TYPE_MEDIA -> AudioManager.STREAM_MUSIC
            ChimeSoundPreferencesUtils.CHIME_SOUND_TYPE_RING -> AudioManager.STREAM_RING
            ChimeSoundPreferencesUtils.CHIME_SOUND_TYPE_ALARM -> AudioManager.STREAM_ALARM
            else -> AudioManager.USE_DEFAULT_STREAM_TYPE
        }.also {
            requireActivity().volumeControlStream = it
        }
    }
}
