package com.example.salaahapp.views.activities.activitiesLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.salaahapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_request_password.*

class RequestPasswordActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_password)
        auth = FirebaseAuth.getInstance()
        init()
    }
    private fun init() {
        button_reset.setOnClickListener {
            var email = edit_text_email_reset.text.toString()
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(object: OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if(task.isSuccessful){
                            Toast.makeText(applicationContext,
                                "Password request link has been sent to $email", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@RequestPasswordActivity,LoginActivity::class.java))
                        }
                        else{
                            Toast.makeText(applicationContext,"Oops. Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }
}
