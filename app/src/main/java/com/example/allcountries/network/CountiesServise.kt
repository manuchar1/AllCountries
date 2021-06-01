package com.example.allcountries.network

import com.example.allcountries.Constants
import com.example.allcountries.models.Countries
import retrofit2.http.GET

interface CountiesServise {
    @GET(Constants.API_ENDPOINT)
    suspend fun getCountries(): Countries

}