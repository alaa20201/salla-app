package com.example.salaahapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.salaahapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    lateinit var animation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animation = AnimationUtils.loadAnimation(applicationContext,
            R.anim.move
        )
        image_view_slash.animation
        image_view_slash.startAnimation(animation)

        var handler = Handler()
        handler.postDelayed({
            checkLogin()
        },5000)
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    //To do//
                    return@OnCompleteListener
                }

                // Get the Instance ID token//
                val token = task.result!!.token
                Log.d("token", token)
                val msg = getString(R.string.fcm_token, token)
            })
    }
    private fun checkLogin(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
