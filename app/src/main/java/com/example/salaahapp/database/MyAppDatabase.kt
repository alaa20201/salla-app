package com.example.salaahapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.salaahapp.models.Prayer

@Database(entities = arrayOf(Prayer::class), version = 1) // Each netity = 1 table
abstract class MyAppDatabase: RoomDatabase(){

    // Has 0 arguments adn return DAO
    abstract fun myDao() : MyDao
}