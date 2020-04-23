package com.example.salaahapp.views.activities.fragmentsCalendar

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.salaahapp.R
import com.example.salaahapp.database.MyAppDatabase
import com.example.salaahapp.helpers.UpdateDayWorkManager
import com.example.salaahapp.models.Prayer
import com.example.salaahapp.views.activities.activitiesLogin.HomeActivity
import com.example.salaahapp.views.fragments.CalendarFragment
import com.example.salaahapp.views.fragments.QuranFragment
import com.example.salaahapp.views.fragments.VerseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_date.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.util.concurrent.TimeUnit

import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annimon.stream.operator.IntArray
import com.example.salaahapp.adapters.PrayerAdapter
import com.example.salaahapp.models.PrayerList
import com.example.salaahapp.models.PrayerViewData
import com.example.salaahapp.service.MyIntentService
import com.example.salaahapp.utils.savePrayer
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat

import java.util.*
import kotlin.collections.ArrayList


class DateActivity : AppCompatActivity() {

    lateinit var viewModel: DateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)
        viewModel = ViewModelProviders.of(this).get(DateViewModel::class.java)
        intent.extras?.let { viewModel.setPrayerList(it) }
        var currentDate = intent.getStringExtra("PickedDate")
        text_view_title_date.text = currentDate

        val recyclerView = findViewById<RecyclerView>(R.id.prayer_container)
        val adapter = PrayerAdapter(viewModel.prayerList.prayerView, ::prayerTypeSelected)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        findViewById<Button>(R.id.button_save_daily_prayer).setOnClickListener {
            savePrayer(currentDate, viewModel.practiceMap)
        }
    }
    private fun prayerTypeSelected(prayerName: String, prayerPractice: Int, prayerTime: String, pos: Int){
        viewModel.updatePracticeMap(prayerName,pos,prayerPractice)
        viewModel.UpdateTimeMap(prayerName,prayerTime)
    }

}
