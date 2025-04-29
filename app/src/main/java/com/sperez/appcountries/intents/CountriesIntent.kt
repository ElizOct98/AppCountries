package com.sperez.appcountries.intents

import com.sperez.appcountries.model.Country

sealed class CountriesIntent {
    object GetCountries : CountriesIntent()
}

sealed class CountriesSate{
    object Loading: CountriesSate()
    object Error: CountriesSate()
    class Display(val list: List<Country>): CountriesSate()
}