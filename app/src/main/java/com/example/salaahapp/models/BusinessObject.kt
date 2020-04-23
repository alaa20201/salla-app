package com.example.salaahapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PrayerType(
    var Fajr: String,
    var Dhuhr: String,
    var Asr: String,
    var Maghrib: String,
    var Isha: String
): Parcelable

@Parcelize
data class PrayerViewData(
    var prayerType: String,
    var practiceType: ArrayList<String>,
    var prayerTime: String,
    var position: Int
): Parcelable

@Parcelize
data class PrayerView(
    var prayerView: ArrayList<PrayerViewData>
): Parcelable