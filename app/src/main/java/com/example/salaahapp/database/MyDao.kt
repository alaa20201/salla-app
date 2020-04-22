package com.example.salaahapp.database

import androidx.room.*
import com.example.salaahapp.models.Prayer


@Dao
interface MyDao {
    @Insert
    fun addPrayer(prayer: Prayer)

    @Update
    fun updatePrayer(prayer: Prayer)

    @Delete
    fun deletePrayer(prayer: Prayer)

    @Query("SELECT * FROM prayer_table WHERE prayer_name LIKE :prayerName")
    fun readPrayerName(prayerName: String): Prayer

    @Query("SELECT * from prayer_table")
    fun getAllPrayer(): List<Prayer>

//    @Query("SELECT * FROM prayer_table WHERE prayer_name LIKE :prayerName")
//    fun isPrayerInserted(prayerName: String): Boolean

    @Query("SELECT  COUNT(prayer_name)FROM prayer_table WHERE prayer_name LIKE :prayerName")
    fun isPrayerInserted(prayerName: String): Int

    @Query("SELECT COUNT(*)  FROM prayer_table")
    fun isDatabaseNull(): Int

    @Query("DELETE FROM prayer_table")
    fun clearTables()

}