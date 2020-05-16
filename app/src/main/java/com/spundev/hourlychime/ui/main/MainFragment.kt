package com.spundev.hourlychime.ui.main


import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.spundev.hourlychime.R

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)

        // Current state info text
        val stateTextView: TextView = rootView.findViewById(R.id.subtitle_textView)
        // Switch widget
        val chimeStateSwitch = rootView.findViewById<SwitchCompat>(R.id.chimeStateSwitch)

        // get the view model
        val viewModel: MainViewModel by viewModels()
        viewModel.isChimeEnabled.observe(viewLifecycleOwner, Observer { isChimeEnabled ->
            // Update the switch
            chimeStateSwitch.isChecked = isChimeEnabled
            // Update the text
            stateTextView.text = if (isChimeEnabled) getString(R.string.chime_state_enabled) else getString(R.string.chime_state_disabled)
        })

        // We use setOnClickListener instead of setOnCheckedChangeListener to avoid
        // triggering the event when the ViewModel observer is used
        chimeStateSwitch.setOnClickListener {
            if (chimeStateSwitch.isChecked) {
                viewModel.enableAlarm()
            } else {
                viewModel.disableAlarm()
            }
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                // We do the navigation manually instead of using
                // onNavDestinationSelected to keep the custom
                // transition between destinations
                findNavController().navigate(R.id.action_main_to_settings)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
