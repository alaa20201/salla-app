package com.example.salaahapp.views.activities.activitiesLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applandeo.materialcalendarview.CalendarView
import com.example.salaahapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeView()
    }
    private fun changeView(){
        login_button.setOnClickListener {
            var intentLogin = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intentLogin)
        }
        register_button.setOnClickListener {
            var intentRegister = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intentRegister)
        }
    }


}
