package com.sperez.appcountries.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sperez.appcountries.intents.CountriesIntent
import com.sperez.appcountries.intents.CountriesSate
import com.sperez.appcountries.model.dataBase.Countries
import com.sperez.appcountries.model.dataBase.from
import com.sperez.appcountries.network.APICountriesService
import com.sperez.appcountries.repository.DataBaseCountries
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CountriesViewModel: ViewModel() {

    var currentState = mutableStateOf<CountriesSate?>(null)
    var intetnts = MutableSharedFlow<CountriesIntent>()
    var countriesDatabase : DataBaseCountries? = null
    val dao by lazy { countriesDatabase?.dao() }

    private val moshiConverterFactory =
        MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())

    private val retrofit =
        Retrofit.Builder().baseUrl( "https://restcountries.com/v3.1/")
            .addConverterFactory(moshiConverterFactory)
            .build()
    private val service = retrofit.create(APICountriesService::class.java)

    private val fields = arrayOf("name","capital","flags")

    init {

        viewModelScope.launch {
            intetnts.collect{
                when(it){

                    is CountriesIntent.GetCountries -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            currentState.value = CountriesSate.Loading
                            val infoDataBase = countriesDatabase?.dao()?.getCountries()
                            if (infoDataBase.isNullOrEmpty()){
                                getCountries()
                            }
                            else{
                                currentState.value = CountriesSate.Display(infoDataBase)

                            }
                        }


                    }
                }
            }
        }
    }

    fun dispatchEvent(event: CountriesIntent) {
        viewModelScope.launch {
            intetnts.emit(event)
        }
    }

    private fun getCountries() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = service.getCountries(fields)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (!body.isNullOrEmpty()) {
                        val listInfoCountries : MutableList<Countries> = mutableListOf()
                        body.forEach {
                            val dataCountries = from(it)
                            dao?.insertCountries(dataCountries)
                            listInfoCountries.add(dataCountries)
                        }
                        currentState.value = CountriesSate.Display(listInfoCountries)
                    }
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

    fun initDatabase(dataBase: DataBaseCountries){
        countriesDatabase = dataBase
    }
}