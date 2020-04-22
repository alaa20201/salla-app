package com.example.salaahapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "prayer_table")
data class Prayer(

    @PrimaryKey
    @ColumnInfo(name = "prayer_name")
    var prayerName:String,

    @ColumnInfo(name = "prayer_type")
    var prayerType:String,

    @ColumnInfo(name = "prayer_time")
    var prayerTime:String,

    @ColumnInfo(name = "prayer_isCompleted")
    var isComplete:Boolean= false
)