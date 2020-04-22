package com.example.salaahapp.views.activities.activitiesLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.salaahapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        init()
    }
    private fun init(){
        button_register.setOnClickListener {
            registerUser()
        }
        already_register_text_view.setOnClickListener {
            var intentSignUp = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intentSignUp)
        }
    }
    private fun registerUser() {
            var firstName = edit_text_register_name.text.toString()
            var mobile = edit_text_register_mobile.text.toString()
            var email = edit_text_register_email.text.toString()
            var password = edit_text_register_password.text.toString()
            if(firstName.isEmpty() || mobile.isEmpty() || email.isEmpty() || password.isEmpty() ){
                Toast.makeText(applicationContext,"Please fill out all requirements", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                        override fun onComplete(task: Task<AuthResult>) {
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Register Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                )
                            } else {
                                if (password.length < 6) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Your password needs to be at least 6 characters",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Your email is not valid",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        }
                    })

        }
    }
}
