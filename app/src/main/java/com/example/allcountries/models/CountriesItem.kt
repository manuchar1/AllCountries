package com.example.allcountries.models


import androidx.annotation.Keep

@Keep
data class CountriesItem(
    val area: Double?,
    val capital: String,
    val flag: String,
    val latlng: List<Double>,
    val name: String,
    val nativeName: String,
    val population: Int,
    val region: String,
    val subregion: String,
)