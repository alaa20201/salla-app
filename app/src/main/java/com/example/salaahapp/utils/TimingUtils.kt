package com.example.salaahapp.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.salaahapp.R
import com.example.salaahapp.network.response.Timings
import com.example.salaahapp.utils.FireBaseUtils.currentUserID
import com.example.salaahapp.utils.FireBaseUtils.databaseReference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.json.JSONObject


fun Timings.convert() {
    this.Fajr = convertToStandard(this.Fajr)
    this.Dhuhr = convertToStandard(this.Dhuhr)
    this.Asr = convertToStandard(this.Asr)
    this.Maghrib = convertToStandard(this.Maghrib)
    this.Isha = convertToStandard(this.Isha)
}

fun convertToStandard(type: String): String {
    try {
        val rawTimestamp = type.replace(":", "")

        val inputFormatter: DateTimeFormatter = DateTimeFormat.forPattern("HHmm")
        val outputFormatter: DateTimeFormatter = DateTimeFormat.forPattern("hh:mm a")
        val dateTime: DateTime =
            inputFormatter.parseDateTime(rawTimestamp)
        return outputFormatter.print(dateTime.millis)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun convertToTiming(response: retrofit2.Response<ResponseBody>): Timings?{
    try {
        val jsonObject = JSONObject(response.body()?.string())
        val dataJson = JSONObject(jsonObject.getString("data").toString())
        val timings = dataJson.getString("timings")
        val timing: Timings = Gson().fromJson(timings.toString(), Timings::class.java)
        return timing
    }catch (e:Exception){
        Log.d("error", e.message)
    }
    return null
}

fun savePrayer(date: String, map: Map<String, String>){
    val nDate = date.replace("/","_")
    currentUserID?.let {
        databaseReference?.child("users")?.child(it)?.child("prayers")?.child(nDate)?.updateChildren(map)
        ?.addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("prayersaved","successful")
            }else{
                Log.d("prayersaved","faild")
            }
        }
    }
}
fun saveTheUserName(name: String){
    currentUserID?.let {
        databaseReference?.child("users")?.child(it)?.child("name")?.setValue(name)
            ?.addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("name","successful")
                }else{
                    Log.d("name","faild")
                }
            }
    }
}
fun retrievePrayers(action: (Map<String, Map<String, String>>) -> Unit){
    val map: Map<String, Map<String, String>>
    currentUserID?.let {
        map = hashMapOf()
        databaseReference?.child("users")?.child(it)?.child("prayers")?.addValueEventListener(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (child in dataSnapshot.children) {
                map[child.key.toString().replace("_", "/")] = child.value as Map<String, String>
            }
            action(map)
        }

    })
    }
}
object FireBaseUtils{
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var currentUserID: String? = auth.currentUser?.uid
    var firebaseDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    var databaseReference: DatabaseReference? = firebaseDatabase?.reference
    val RC_SIGN_IN = 2
}