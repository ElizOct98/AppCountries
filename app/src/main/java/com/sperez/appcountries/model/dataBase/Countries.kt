package com.sperez.appcountries.model.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sperez.appcountries.model.api.Country

@Entity(tableName = "country")
data class Countries(
    @PrimaryKey(autoGenerate = true)
    val key: Int?= null,
    @ColumnInfo(name = "image_flag")
    val image: String,
    @ColumnInfo(name = "name_country")
    val name: String,
    @ColumnInfo(name = "capital_country")
    val capital: String
)

fun from (apiCountries: Country) : Countries {
    return Countries(
        image = apiCountries.image.png,
        name = apiCountries.name.common,
        capital = apiCountries.capital.first()
    )
}
