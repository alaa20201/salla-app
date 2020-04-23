package com.example.salaahapp.views.activities.fragmentsCalendar

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.salaahapp.models.PrayerView
import com.example.salaahapp.models.PrayerViewData

class DateViewModel:ViewModel() {
    var practiceArrayPos = -1
    var prayerList: PrayerView = PrayerView(ArrayList())
    var practiceMap: MutableMap<String, String> = mutableMapOf(
        "Fajr" to "",
        "Dhuhr" to "",
        "Asr" to "",
        "Maghrib" to "",
        "Isha" to ""
    )
    var timeMap: MutableMap<String, String> = mutableMapOf(
        "Fajr" to "",
        "Dhuhr" to "",
        "Asr" to "",
        "Maghrib" to "",
        "Isha" to ""
    )

    fun setPrayerList(bundle: Bundle){
         prayerList.prayerView = bundle?.getParcelableArrayList<PrayerViewData>("prayers") as ArrayList<PrayerViewData>
        for (prayer in prayerList.prayerView){
            if (prayer.position != 0)
                practiceMap[prayer.prayerType] = prayer.practiceType[prayer.position]
        }
    }

    fun updatePracticeMap(key: String, pos: Int, pos2: Int){
        practiceMap[key] = prayerList.prayerView[pos].practiceType[pos2]
        Log.d("practice", practiceMap.toString())
    }

    fun UpdateTimeMap(key: String, value: String){
        timeMap[key] = value
        Log.d("time", timeMap.toString())

    }
}