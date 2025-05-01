package com.sperez.appcountries.network

import com.sperez.appcountries.model.api.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APICountriesService {
    @GET("region/america")
    suspend fun getCountries(@Query("fields") fields: Array<String>): Response<List<Country>>
}