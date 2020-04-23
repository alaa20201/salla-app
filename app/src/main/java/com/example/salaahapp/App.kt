package com.example.salaahapp

import android.app.Application
import android.content.Context

class App: Application() {
    var application: Application? = null
    override fun onCreate() {
        super.onCreate()
        if (application == null){
            application = this
        }

    }
}