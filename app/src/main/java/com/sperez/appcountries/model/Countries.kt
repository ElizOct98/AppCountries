package com.sperez.appcountries.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Country(
    @Json(name = "flags")
    val image: Images,
    @Json(name = "name")
    val name: Names,
    @Json(name = "capital")
    val capital: List<String>
)

@JsonClass(generateAdapter = true)
data class Images(
    val png: String
)

@JsonClass(generateAdapter = true)
data class Names(
    val common: String
)
