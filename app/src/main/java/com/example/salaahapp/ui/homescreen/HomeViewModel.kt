package com.example.salaahapp.ui.homescreen

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.salaahapp.models.PrayerType
import com.example.salaahapp.models.PrayerView
import com.example.salaahapp.models.PrayerViewData
import com.example.salaahapp.network.api.SallaApi
import com.example.salaahapp.network.response.Timings
import com.example.salaahapp.utils.convert
import com.example.salaahapp.utils.convertToTiming
import com.prolificinteractive.materialcalendarview.CalendarDay
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HomeViewModel : ViewModel() {
    val city = "Bangalore"
    val country = "India"
    val method = 8
    var _data: MutableLiveData<PrayerType> = MutableLiveData()
    val data: LiveData<PrayerType> = _data
    var sallaApi: SallaApi? = null
    var prayerViewData: PrayerView = PrayerView(ArrayList())
    var map: Map<String, String>? = null
    val _selected_date: MutableLiveData<CalendarDay> = MutableLiveData()
    val selected_date: LiveData<CalendarDay> = _selected_date
    var sourceview = ""
    val positionMaps: Map<String, Int> = hashMapOf(
        "" to 0,
        "Pray With Jamaa" to 1,
        "Pray W/O Jamaa" to 2,
        "khazaa" to 3
    )

    /**
     * setting data for calendar fragment
     */
    val _prayerData: MutableLiveData<Map<String, Map<String, String>>> = MutableLiveData()
    val prayerData: LiveData<Map<String, Map<String, String>>> = _prayerData

    fun setDataFromBundle(bundle: Bundle){
        val from = bundle.getString("from")
        when(from){
            "LGN" -> {
                sourceview = "LGN"
                _selected_date.value = CalendarDay.today()
            }
            else -> _selected_date.value = CalendarDay(Date(bundle.getString("PickedDate")))
        }

    }
    fun setSelectedDay(day: CalendarDay){
        _selected_date.value = day
    }

    fun setData(map: Map<String, Map<String, String>>){
        _prayerData.postValue(map)
    }

    fun getSallahData() {
        val call = sallaApi?.getPosts()
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val timing: Timings? = convertToTiming(response)
                timing?.convert()
                timing?.apply {
                    _data.value = PrayerType(Fajr, Dhuhr, Asr, Maghrib, Isha)
                    map = mapOf(
                        "Fajr" to Fajr,
                        "Dhuhr" to Dhuhr,
                        "Asr" to Asr,
                        "Maghrib" to Maghrib,
                        "Isha" to Isha

                    )
                    for (key in map!!.keys){
                        prayerViewData.prayerView.add(PrayerViewData(key, arrayListOf(
                            "Please select",
                            "Pray With Jamaa",
                            "Pray W/O Jamaa",
                            "khazaa"
                        ), map!![key]!!,0))
                    }
                }

            }

        })

    }

    fun updateDateSelected(date: String) {
        prayerData.value?.get(date)?.let {
            for (pkey in it.keys){
                for (pr in prayerViewData.prayerView){
                    if (pr.prayerType == pkey){
                        pr.position = positionMaps[it[pkey]]!!
                    }
                }
            }
        }
    }

    fun resetDateSelected(){
        for (pr in prayerViewData.prayerView){
            pr.position = 0
        }
    }
}