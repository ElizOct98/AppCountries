package com.sperez.appcountries.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sperez.appcountries.model.dataBase.Countries

@Dao
interface CountriesDAO {

    @Query(value = "SELECT * FROM country")
    fun getCountries(): List<Countries>

    @Insert
    fun insertCountries(country: Countries)

}