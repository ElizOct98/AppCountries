package com.sperez.appcountries.intents

import com.sperez.appcountries.model.Country

sealed class CountriesIntent {
    object GetCountries : CountriesIntent()
    object Refresh : CountriesIntent()
}

sealed class CountriesSate{
    object Loading: CountriesSate()
    object Error: CountriesSate()
    class Display(val list: List<Country>): CountriesSate()
}