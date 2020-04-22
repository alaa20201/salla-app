package com.example.salaahapp.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast
import androidx.room.Room
import com.example.salaahapp.database.MyAppDatabase
import com.example.salaahapp.models.PrayerList
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MyIntentService: IntentService("myThread") {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var myDB:MyAppDatabase
    var userID = ""

    override fun onHandleIntent(intent: Intent?) {
        // Toast.makeText(this,"Intent Service onHandle", Toast.LENGTH_SHORT).show()
        //var t : Int = 10
        //var s = intent?.extras?.getString("key1")
//        for(i in 1..7){
//            Thread.sleep(1000)
//        }
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("users").child(userID).child("prayers")
        var prayerList = myDB.myDao().getAllPrayer()
        var data = PrayerList(prayerList)
        var date = Calendar.getInstance().time.toString()
        databaseReference.child(date).setValue(data)

    }
    override fun onCreate() {
        super.onCreate()
        //Toast.makeText(this,"Intent Service Created", Toast.LENGTH_SHORT).show()
        myDB = Room.databaseBuilder(this, MyAppDatabase::class.java,"prayerdb")
            .allowMainThreadQueries()
            .build()
        var sharePrefUserId = getSharedPreferences("emailID", Context.MODE_PRIVATE)
        userID = sharePrefUserId.getString("email","").toString()
        var ext = "."
        userID = userID.replace(ext,"")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Toast.makeText(this,"Intent Service Started", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        myDB.clearAllTables()
       // Toast.makeText(this,"Intent Service Finished", Toast.LENGTH_SHORT).show()
    }
}