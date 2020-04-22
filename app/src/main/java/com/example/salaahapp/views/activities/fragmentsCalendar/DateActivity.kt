package com.example.salaahapp.views.activities.fragmentsCalendar

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.salaahapp.R
import com.example.salaahapp.database.MyAppDatabase
import com.example.salaahapp.helpers.UpdateDayWorkManager
import com.example.salaahapp.models.Prayer
import com.example.salaahapp.views.activities.activitiesLogin.HomeActivity
import com.example.salaahapp.views.fragments.CalendarFragment
import com.example.salaahapp.views.fragments.QuranFragment
import com.example.salaahapp.views.fragments.VerseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_date.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.util.concurrent.TimeUnit

import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.annimon.stream.operator.IntArray
import com.example.salaahapp.models.PrayerList
import com.example.salaahapp.service.MyIntentService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat

import java.util.*


class DateActivity : AppCompatActivity() {
    final var SELF_REMINDER_HOUR = 12
    lateinit var pendingIntent: PendingIntent
    lateinit var alarmManager: AlarmManager

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    companion object{
        val MESSAGE_STATUS = "message_status"
    }
    lateinit var radioGroup1: RadioGroup
    lateinit var radioGroup2: RadioGroup
    lateinit var radioGroup3: RadioGroup
    lateinit var radioGroup4: RadioGroup
    lateinit var radioGroup5: RadioGroup
    lateinit var myDB: MyAppDatabase
    var userID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)
        var currentDate = intent.getStringExtra("PickedDate")
        text_view_title_date.text = currentDate
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("users").child(userID).child("prayers")

        // Initialize the radio group
        radioGroup1 = findViewById(R.id.radio_group_fajar)
        radioGroup2 = findViewById(R.id.radio_group_dhuhar)
        radioGroup3 = findViewById(R.id.radio_group_asr)
        radioGroup4 = findViewById(R.id.radio_group_maghrib)
        radioGroup5 = findViewById(R.id.radio_group_isha)
        myDB = Room.databaseBuilder(this, MyAppDatabase::class.java,"prayerdb")
            .allowMainThreadQueries()
            .build()
        savePrayerTime()
//        var sharePrefUserId = getSharedPreferences("emailID", Context.MODE_PRIVATE)
//        userID = sharePrefUserId.getString("email","").toString()
//        var ext = "."
//        userID = userID.replace(ext,"")
        //var button1 = findViewById<RadioButton>(R.id.radio_fajar_pj)
        //button1.isChecked = true
//        var sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
//        var dateInString = "29-03-2020 17:11:59"
//        var date = sdf.parse(dateInString)
//
        var count = myDB.myDao().isDatabaseNull()
        if (count>0){
            var prayerList = myDB.myDao().getAllPrayer()
            for(i in 0..prayerList.size-1){
                listCheckedPrayer(prayerList[i].prayerName)
            }
//            if (prayerList.size == 5){
//                var data = PrayerList(prayerList)
//                databaseReference.child(date.toString()).setValue(data)
//                sendWorkManager()
//            }

        }
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        sendAlarmManager()
        //sendWorkManager()
    }

     private fun sendAlarmManager() {
        var c = Calendar.getInstance()
        c.timeInMillis = System.currentTimeMillis()
        c.set(Calendar.HOUR_OF_DAY, 9)
        c.set(Calendar.MINUTE, 36)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        var intent_send_broadcaset = Intent()
        intent_send_broadcaset.action = "com.example.SENDALARM"
        //sendBroadcast(intent_send_broadcaset)
        // intent = Intent(this, Main2Activity::class.java)
        var pendingIntent:PendingIntent=PendingIntent.getBroadcast( this.getApplicationContext(), 123, intent_send_broadcaset, 0)
        // var pendingIntent1 = PendingIntent.getBroadcast( this.getApplicationContext(), 124, intent, 0)
        //alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()
         //       + (5 * 1000),pendingIntent)
         alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.timeInMillis,pendingIntent)
    }

    private fun dailyUpdatePrayer(){
        val currentTime = Calendar.getInstance().time
        var sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
        var dateInString = "Sun Mar 29 17:26:59 CDT 2020"
        //var date = sdf.parse(dateInString)
        if(currentTime.toString() == dateInString){
            Log.d("Check current time", "Success$currentTime")
            //sendWorkManager()
            //databaseReference.child(userID).setValue(data)
            //databaseReference.child(date.toString()).setValue(data)
            myDB.myDao().clearTables()
        }
        else{
            Log.d("Check current time","Error")
        }

    }
    private fun compareCurrentHour(targetHour: Int): Boolean {
        val current = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return current == targetHour
    }
    private fun sendWorkManager(){
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        // Set Execution Timer
        dueDate.set(Calendar.HOUR_OF_DAY, 14)
        dueDate.set(Calendar.MINUTE, 28)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val dailyWorkRequest = OneTimeWorkRequest.Builder(UpdateDayWorkManager::class.java).setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).build()
        WorkManager.getInstance().enqueue(dailyWorkRequest)
        /**
         * Trying broadcasr receiver with work manager
         */
        var intent_send_broadcaset = Intent()
        intent_send_broadcaset.action = "com.example.SENDALARM"
        var pendingIntent:PendingIntent=PendingIntent.getBroadcast( this.getApplicationContext(), 123, intent_send_broadcaset, 0)
        //var intent = Intent(this, MyIntentService::class.java)
        //startService(intent)
        Log.d("WorkerManagerTester","Success")
//        var mWorkManager = WorkManager.getInstance()
//        var mRequest = PeriodicWorkRequest.Builder(UpdateDayWorkManager::class.java,2, TimeUnit.MINUTES).build()
//        mWorkManager.enqueue(mRequest)
//        mWorkManager.getWorkInfoByIdLiveData(mRequest.id)
//            .observe(this, object : Observer<WorkInfo> {
//                override fun onChanged(@Nullable workInfo: WorkInfo?) {
//                    if (workInfo != null) {
//                        // Testing
//                        val state = workInfo.state
//                        Toast.makeText(this@DateActivity,state.toString()+ "\n",Toast.LENGTH_LONG).show()
//                    }
//                }
//            })
    }
    private fun listCheckedPrayer(name:String){
        var prayerList = myDB.myDao().readPrayerName(name)
        var prayerType = prayerList.prayerType
        if(name == "Fajar"){
            if (prayerType == "PJ"){
                radio_fajar_pj.isChecked = true
            }
            else if (prayerType == "WJ"){
                radio_fajar_wj.isChecked = true
            }
            else {
                radio_fajar_kh.isChecked = true
            }
        }
        if(name == "Dhuhar"){
            if (prayerType == "PJ"){
                radio_dhuhar_pj.isChecked = true
            }
            else if (prayerType == "WJ"){
                radio_dhuhar_wj.isChecked = true
            }
            else {
                radio_dhuhar_kh.isChecked = true
            }
        }
        if(name == "Asr"){

            if (prayerType == "PJ"){
                radio_asr_pj.isChecked = true
            }
            else if (prayerType == "WJ"){
                radio_asr_wj.isChecked = true
            }
            else {
                radio_asr_kh.isChecked = true
            }
        }
        if(name == "Maghrib"){
            if (prayerType == "PJ"){
                radio_maghrib_pj.isChecked = true
            }
            else if (prayerType == "WJ"){
                radio_maghrib_wj.isChecked = true
            }
            else {
                radio_maghrib_kh.isChecked = true
            }
        }
        if(name == "Isha"){
            if (prayerType == "PJ"){
                radio_isha_pj.isChecked = true
            }
            else if (prayerType == "WJ"){
                radio_isha_wj.isChecked = true
            }
            else {
                radio_isha_kh.isChecked = true
            }
        }
        else{
            Log.d("DateActivity","Nothing in the database")
        }
    }


    private fun savePrayerTime(){
        // Getting instance of Room
        var myDB = Room.databaseBuilder(this, MyAppDatabase::class.java,"prayerdb")
            .allowMainThreadQueries()
            .build()
        button_save_daily_prayer.setOnClickListener {
            // Store according to radio group selected
            var isFajarInsrted = myDB.myDao().isPrayerInserted("Fajar")
            var isDhuharInserted = myDB.myDao().isPrayerInserted("Dhuhar")
            var isAsrrInserted = myDB.myDao().isPrayerInserted("Asr")
            var isMagInserted =  myDB.myDao().isPrayerInserted("Maghrib")
            var isIshaInserted =  myDB.myDao().isPrayerInserted("Isha")
            if (isFajarInsrted == 0) // if not null
            {
                savePrayer1(myDB)
            }
            if(isDhuharInserted ==0){
                savePrayer2(myDB)
            }
            if(isAsrrInserted == 0){
                savePrayer3(myDB)
            }
            if(isMagInserted == 0){
                savePrayer4(myDB)
            }
            if(isIshaInserted == 0){
                savePrayer5(myDB)
            }
            startActivity(Intent(this,HomeActivity::class.java))
        }

    }


    private fun savePrayer1(myDB:MyAppDatabase){
        // If no button is clicked
        var isComplete = false
        if (radioGroup1.checkedRadioButtonId == -1)
        {
            isComplete
        }
        else{ // if one is selected
            isComplete = true
            var position =  radioGroup1.checkedRadioButtonId
            var radioButton1 = findViewById<RadioButton>(position)
           // radioButton1.is = true
            //radioButton1.setBackgroundResource(R.color.colorPrimaryDark)

            var prayerType = radioButton1.text.toString() // returning which prayer type for fajar
            var prayerName = text_view_fajar_title.text.toString()
            var prayerTime = text_view_fajar_time.text.toString()
            var prayer = Prayer(prayerName,prayerType,prayerTime,isComplete) // passing the completed to true
            myDB.myDao().addPrayer(prayer)
            Toast.makeText(applicationContext,"Fajar Salaah Completed", Toast.LENGTH_LONG).show()
        }
    }

    private fun savePrayer2(myDB:MyAppDatabase){
        // If no button is clicked
        var isComplete = false
        if (radioGroup2.checkedRadioButtonId == -1)
        {
            isComplete
        }
        else{ // if one is selected
            isComplete = true
            var position =  radioGroup2.checkedRadioButtonId
            var radioButton2 = findViewById<RadioButton>(position)
            var prayerType = radioButton2.text.toString() // returning which prayer type for fajar
            var prayerName = text_view_dhuhar_title.text.toString()
            var prayerTime = text_view_dhuhar_time.text.toString()
            var prayer = Prayer(prayerName,prayerType,prayerTime,isComplete) // passing the completed to true
            myDB.myDao().addPrayer(prayer)
            Toast.makeText(applicationContext,"Dhuhar Salaah Completed", Toast.LENGTH_LONG).show()
        }
    }
    private fun savePrayer3(myDB:MyAppDatabase){
        // If no button is clicked
        var isComplete = false
        if (radioGroup3.checkedRadioButtonId == -1)
        {
            isComplete
        }
        else{ // if one is selected
            isComplete = true
            var position =  radioGroup3.checkedRadioButtonId
            var radioButton3 = findViewById<RadioButton>(position)
            var prayerType = radioButton3.text.toString() // returning which prayer type for fajar
            var prayerName = text_view_asr_title.text.toString()
            var prayerTime = text_view_asr_time.text.toString()
            var prayer = Prayer(prayerName,prayerType,prayerTime,isComplete) // passing the completed to true
            myDB.myDao().addPrayer(prayer)
            Toast.makeText(applicationContext,"Asr Salaah Completed", Toast.LENGTH_LONG).show()
        }
    }
    private fun savePrayer4(myDB:MyAppDatabase){
        // If no button is clicked
        var isComplete = false
        if (radioGroup4.checkedRadioButtonId == -1)
        {
            isComplete
        }
        else{ // if one is selected
            isComplete = true
            var position =  radioGroup4.checkedRadioButtonId
            var radioButton4 = findViewById<RadioButton>(position)
            var prayerType = radioButton4.text.toString() // returning which prayer type for fajar
            var prayerName = text_view_maghrib_title.text.toString()
            var prayerTime = text_view_maghrib_time.text.toString()
            var prayer = Prayer(prayerName,prayerType,prayerTime,isComplete) // passing the completed to true
            myDB.myDao().addPrayer(prayer)
            Toast.makeText(applicationContext,"Maghrib Salaah Completed", Toast.LENGTH_LONG).show()
        }
    }
    private fun savePrayer5(myDB:MyAppDatabase){
        // If no button is clicked
        var isComplete = false
        if (radioGroup5.checkedRadioButtonId == -1)
        {
            isComplete
        }
        else{ // if one is selected
            isComplete = true
            var position =  radioGroup5.checkedRadioButtonId
            var radioButton5 = findViewById<RadioButton>(position)
            var prayerType = radioButton5.text.toString() // returning which prayer type for fajar
            var prayerName = text_view_isha_title.text.toString()
            var prayerTime = text_view_isha_time.text.toString()
            var prayer = Prayer(prayerName,prayerType,prayerTime,isComplete) // passing the completed to true
            myDB.myDao().addPrayer(prayer)
            Toast.makeText(applicationContext,"Isha Salaah Completed", Toast.LENGTH_LONG).show()
        }
    }


}
