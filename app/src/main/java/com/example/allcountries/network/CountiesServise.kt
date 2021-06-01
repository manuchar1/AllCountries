package com.example.allcountries.network

import com.example.allcountries.Constants
import com.example.allcountries.models.CountriesItem
import retrofit2.Response
import retrofit2.http.GET

interface CountiesServise {
    @GET(Constants.API_ENDPOINT)
    suspend fun getCountries():Response<List<CountriesItem>>

}