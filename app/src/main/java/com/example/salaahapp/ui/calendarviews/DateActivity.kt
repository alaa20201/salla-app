package com.example.salaahapp.ui.calendarviews
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.example.salaahapp.R
import com.example.salaahapp.ui.homescreen.HomeActivity
import kotlinx.android.synthetic.main.activity_date.*
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salaahapp.adapters.PrayerAdapter
import com.example.salaahapp.utils.savePrayer


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
            var intent: Intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("from", "DA")
            intent.putExtra("PickedDate", currentDate)
            startActivity(intent)
        }
    }
    private fun prayerTypeSelected(prayerName: String, prayerPractice: Int, prayerTime: String, pos: Int){
        viewModel.updatePracticeMap(prayerName,pos,prayerPractice)
        viewModel.UpdateTimeMap(prayerName,prayerTime)
    }

}
