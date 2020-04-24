package com.example.salaahapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salaahapp.R
import com.example.salaahapp.models.PrayerViewData


class PrayerAdapter(
    private val prayerList: ArrayList<PrayerViewData>,
    val action: (String,Int,String,Int)-> Unit
    ): RecyclerView.Adapter<PrayerAdapter.PrayerVH>() {

    inner class PrayerVH(val view: View): RecyclerView.ViewHolder(view){
        val prayerNameView: TextView = view.findViewById(R.id.text_view_prayer_title)
        val prayerTypeSpinner: NumberPicker = view.findViewById(R.id.np_picker)
        val prayerTimeView: TextView = view.findViewById(R.id.text_view_prayer_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerVH {
        return PrayerVH(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_prayer_cell, parent, false)
        )
    }

    override fun getItemCount(): Int = prayerList.size

    override fun onBindViewHolder(holder: PrayerVH, position: Int) {
        val prayer = prayerList[position]
        holder.prayerNameView.text = "Al ${prayer.prayerType}"
        holder.prayerTimeView.text = prayer.prayerTime
        setSpinner(holder.prayerTypeSpinner, prayer.practiceType, prayer.position)
        holder.prayerTypeSpinner.setOnScrollListener(object : NumberPicker.OnScrollListener {
            private var oldValue = 0
            override fun onScrollStateChange(
                numberPicker: NumberPicker,
                scrollState: Int
            ) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    oldValue = numberPicker.value
                    update(numberPicker.value, position)
                }
            }
        })
    }
    fun update(scrollState: Int, pos: Int){
        action(prayerList[pos].prayerType, scrollState, prayerList[pos].prayerTime, pos)
    }

    private fun setSpinner(prayerTypeSpinner: NumberPicker, prayerType: ArrayList<String>, position: Int) {
        val array = arrayOfNulls<String>(prayerType.size)
        prayerType.toArray(array)
        prayerTypeSpinner.minValue = 0
        prayerTypeSpinner.maxValue = prayerType.size -1
        prayerTypeSpinner.displayedValues = array
        prayerTypeSpinner.value = position
        if (position != 0) prayerTypeSpinner.isEnabled = false
    }
}