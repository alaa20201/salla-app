package com.example.salaahapp.ui.activitiesLogin

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.salaahapp.R
import com.example.salaahapp.glurry.BlurBuilder.blur
import com.example.salaahapp.ui.homescreen.HomeActivity
import com.example.salaahapp.utils.saveTheUserName
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {

    private lateinit var phoneText: EditText
    private lateinit var codeText: EditText
    private lateinit var coninueBtn: Button
    private var checker = ""
    private var phoneNumber = ""
    private lateinit var phoneAuthLayout: RelativeLayout
    lateinit var progressBar: ProgressDialog
    lateinit var nameView: EditText
    var username = ""
    //fire base variables
    private lateinit var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mResendingToken: PhoneAuthProvider.ForceResendingToken
    private var mVerificationId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val originalBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.ais)
        val blurredBitmap = blur(this, originalBitmap)
        findViewById<RelativeLayout>(R.id.login_ac).background =
            BitmapDrawable(resources, blurredBitmap)

        phoneText = findViewById(R.id.phoneText)
        nameView = findViewById(R.id.name)
        progressBar = ProgressDialog(this)
        codeText = findViewById(R.id.codeText)
        coninueBtn = findViewById(R.id.continueNextButton)
        phoneAuthLayout = findViewById(R.id.phoneAuth)
        mAuth = FirebaseAuth.getInstance()

        coninueBtn.setOnClickListener {
            if (coninueBtn.text.equals("Submit") || checker.equals("Code sent")){
                val verificationCode = codeText.text.toString()
                if (verificationCode.isBlank()){
                    Toast.makeText(this, "Enter the verification code first.",Toast.LENGTH_LONG).show()
                }else{
                    progressBar.setMessage("Verfiying...")
                    progressBar.show()
                    val phoneAuthCredential: PhoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode)
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

            }else{
                progressBar.setMessage("Verfiying your number, Please hold tite , You will receive a code to start flying")
                progressBar.show()
                phoneNumber = "+1"+phoneText.text.toString()
                username = nameView.text.toString()
                if (username.isNullOrBlank()){
                    Log.d("username","user epty")
                    Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG)
                    progressBar.dismiss()
                    return@setOnClickListener
                }
                if (phoneNumber.isNullOrBlank()){
                    Toast.makeText(this, "Please provide valid phone number", Toast.LENGTH_LONG)
                }else{
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        this,
                        mCallBacks
                    )
                }
            }
        }
        mCallBacks = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                p0?.let { signInWithPhoneAuthCredential(it) }
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                Log.d("error222", p0?.message)
                Toast.makeText(this@LoginActivity, "Invalid phone number, please try again"+p0?.message, Toast.LENGTH_LONG).show()
                progressBar.dismiss()
                phoneAuthLayout.visibility = View.VISIBLE
                codeText.visibility = View.GONE
                coninueBtn.text = "Continue"

            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(p0, p1)
                mVerificationId = p0.toString()
                if (p1 != null) {
                    mResendingToken = p1
                }
                progressBar.dismiss()
                phoneAuthLayout.visibility = View.GONE
                checker = "Code sent"
                coninueBtn.text = "Submit"
                codeText.visibility = View.VISIBLE
                Toast.makeText(this@LoginActivity, "Code has been sent",Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        user?.apply {
            if (this != null){
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.putExtra("from", "LGN")
                startActivity(intent)
            }
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressBar.dismiss()
                    saveTheUserName(username)
                    navigateToHomeScreen()
                } else {
                    progressBar.dismiss()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "The code you intered is not correct",Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun navigateToHomeScreen(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("from", "LGN")
        startActivity(intent)
        finish()
    }
}
