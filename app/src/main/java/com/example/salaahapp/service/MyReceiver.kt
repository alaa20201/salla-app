package com.example.salaahapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "alarm received", Toast.LENGTH_SHORT).show()
        Log.d("Alarm","alarm received")
        var intent = Intent(context,MyIntentService::class.java)
        context!!.startService(intent)
    }
}