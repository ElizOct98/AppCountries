package com.sperez.appcountries.network

import com.sperez.appcountries.model.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APICountriesService {
    //"region/america?fields=name,capital,flags"
    //https://restcountries.com/v3.1/region/america?fields=name,capital,flags
    @GET("region/america")
    suspend fun getCountries(@Query("fields") fields: Array<String>): Response<List<Country>>
}