package com.example.salaahapp.views.activities.activitiesLogin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.salaahapp.R
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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var googleSignInOption: GoogleSignInOptions
    val RC_SIGN_IN = 2
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        forgotPassword()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        val sign_in_google = findViewById<View>(R.id.button_log_in_google) as SignInButton
        googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOption)
        button_login.setOnClickListener {
            login()
        }
        new_user_text_view.setOnClickListener {
            var intentNewUser = Intent(this@LoginActivity, RegisterActivity::class.java)
            //intentSecond.putExtra("DATA", )
            startActivity(intentNewUser)
        }
        sign_in_google.setOnClickListener {
                view: View? -> signInGoogle()
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
            //email = saveUsername
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, object :
                OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
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

            })
        }
        /**
         * Just for testing, so we don't have to reenter the user name + pass everytime
          */
        startActivity(Intent(this, HomeActivity::class.java))
    }
    private fun signInGoogle() {
        val signInIntent :Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN )
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
        startActivity(Intent(this,HomeActivity::class.java))
    }
    private fun forgotPassword(){
        check_box_forgot_pass.setOnClickListener {
            //Toast.makeText(this,"Check box Clicked",Toast.LENGTH_SHORT).show()
            //check_box_forgot_pass.isChecked = false
            startActivity(Intent(this,
                RequestPasswordActivity::class.java))
        }
    }
}
