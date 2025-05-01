package com.sperez.appcountries.intents

import com.sperez.appcountries.model.api.Country
import com.sperez.appcountries.model.dataBase.Countries

sealed class CountriesIntent {
    object GetCountries : CountriesIntent()
}

sealed class CountriesSate{
    object Loading: CountriesSate()
    object Error: CountriesSate()
    class Display(val list: List<Countries>): CountriesSate()
}