package com.example.allcountries.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.allcountries.models.CountriesItem
import com.example.allcountries.network.NetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountriesViewModel: ViewModel() {

    private val countriesLiveData = MutableLiveData<List<CountriesItem>>().apply {
        mutableListOf<CountriesItem>()
    }

    val _countriesLiveData: LiveData<List<CountriesItem>> = countriesLiveData

    private val loadingLiveData = MutableLiveData<Boolean>()
    val _loadingLiveData: LiveData<Boolean> = loadingLiveData

    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            getCountries()
        }
    }

    private suspend fun getCountries() {
        loadingLiveData.postValue(true)
        val result = NetworkClient.counstriesService.getCountries()
        if (result.isSuccessful) {
            val items = result.body()
            countriesLiveData.postValue(items)
        }
        loadingLiveData.postValue(false)

    }
}