package com.sperez.appcountries.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sperez.appcountries.intents.CountriesSate
import com.sperez.appcountries.model.Country
import com.sperez.appcountries.network.APICountriesService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CountriesViewModel: ViewModel() {

    var currentState = mutableStateOf<CountriesSate?>(null)
    //var countriesList = mutableStateOf<List<Country>>(emptyList())
    private val moshiConverterFactory =
        MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())

    private val retrofit =
        Retrofit.Builder().baseUrl( "https://restcountries.com/v3.1/")
            .addConverterFactory(moshiConverterFactory)
            .build()
    private val service = retrofit.create(APICountriesService::class.java)

    private val fields = arrayOf("name","capital","flags")

    init {
        getCountries()
    }

    private fun getCountries() {
        currentState.value = CountriesSate.Loading
        viewModelScope.launch {
            try {
                val response = service.getCountries(fields)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (!body.isNullOrEmpty())
                        currentState.value = CountriesSate.Display(body)
                } else {
                    Log.e("RESPONSE ERROR", response.errorBody().toString())
                    currentState.value = CountriesSate.Error
                }
            } catch (e: Exception) {
                Log.e("RETROFIT", e.message.toString())
                currentState.value = CountriesSate.Error
            }
        }
    }
}