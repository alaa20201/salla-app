package com.example.salaahapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.salaahapp.R
import com.example.salaahapp.ui.activitiesLogin.LoginActivity
import com.example.salaahapp.ui.activitiesLogin.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*

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
