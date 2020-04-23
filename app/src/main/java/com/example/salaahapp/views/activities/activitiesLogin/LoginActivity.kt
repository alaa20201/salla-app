package com.example.salaahapp.views.activities.activitiesLogin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.salaahapp.R
import com.example.salaahapp.utils.FireBaseUtils.RC_SIGN_IN
import com.example.salaahapp.utils.FireBaseUtils.auth
import com.example.salaahapp.utils.FireBaseUtils.currentUserID
import com.example.salaahapp.utils.FireBaseUtils.databaseReference
import com.example.salaahapp.utils.FireBaseUtils.firebaseDatabase
import com.example.salaahapp.utils.FireBaseUtils.mGoogleSignInClient
import com.example.salaahapp.utils.FireBaseUtils.signInWithGoogle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        forgotPassword()
    }

    private fun init() {
        val sign_in_google = findViewById<View>(R.id.button_log_in_google) as SignInButton
        button_login.setOnClickListener {
            login()
        }
        new_user_text_view.setOnClickListener {
            var intentNewUser = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentNewUser)
        }
        sign_in_google.setOnClickListener {
            signInGoogle()
        }
    }

    private fun login() {
        var email = edit_text_username.text.toString()
        var password = edit_text_pass.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(applicationContext, "Username cannot be blank", Toast.LENGTH_SHORT)
                .show()
        }
        if (password.isEmpty()) {
            Toast.makeText(applicationContext, "Password cannot be blank", Toast.LENGTH_SHORT)
                .show()
        }
        //if(email!=null && password!=null){
        else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this
            ) { task ->
                if (task.isSuccessful) {
                    var sharePref = getSharedPreferences("emailID", Context.MODE_PRIVATE)
                    var editor = sharePref.edit()
                    editor.putString("email",email)
                    editor.apply()

                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))

                    Toast.makeText(applicationContext, "Login Successfully", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Wrong email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        /**
         * Just for testing, so we don't have to reenter the user name + pass everytime
          */
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun signInGoogle() {
        signInWithGoogle(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResults(task)

        }
    }
    private fun handleResults(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!

            updateUI(account)

        }catch (e: ApiException){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        }
    }
    private fun updateUI(account: GoogleSignInAccount) {
        currentUserID = account.id
        currentUserID?.let {
            startActivity(Intent(this,HomeActivity::class.java))
        }

    }
    private fun forgotPassword(){
        check_box_forgot_pass.setOnClickListener {
            startActivity(Intent(this,
                RequestPasswordActivity::class.java))
        }
    }
}
