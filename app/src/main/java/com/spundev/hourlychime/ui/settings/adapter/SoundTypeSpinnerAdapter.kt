package com.spundev.hourlychime.ui.settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.spundev.hourlychime.R
import com.spundev.hourlychime.util.ChimeSoundPreferencesUtils

class SoundTypeSpinnerAdapter(context: Context, objects: Array<String>) : ArrayAdapter<String>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val itemView = if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            layoutInflater.inflate(R.layout.list_item_sound_type, parent, false)
        } else {
            convertView
        }

        val imageView: ImageView = itemView.findViewById(R.id.sound_type_imageView)
        val textView: TextView = itemView.findViewById(R.id.sound_type_textView)

        getItem(position)?.let { soundTypeString ->
            imageView.setImageResource(ChimeSoundPreferencesUtils.getSoundTypeImage(context, soundTypeString))
            textView.text = soundTypeString
        }

        return itemView
    }
}