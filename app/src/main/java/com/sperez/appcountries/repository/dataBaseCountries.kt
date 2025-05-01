package com.sperez.appcountries.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sperez.appcountries.model.dataBase.Countries

@Database(entities = [Countries::class], version = 1)
abstract class DataBaseCountries: RoomDatabase() {
    abstract fun dao(): CountriesDAO
}