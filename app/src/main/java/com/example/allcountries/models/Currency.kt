package com.example.allcountries.models


import androidx.annotation.Keep

@Keep
data class Currency(
    val code: String,
    val name: String?,
    val symbol: String?
)