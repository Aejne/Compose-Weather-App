/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aejne.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aejne.weather.models.OneCallResponse
import com.aejne.weather.models.WeatherResponse
import com.aejne.weather.repository.MainRepository
import com.aejne.weather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _city = MutableLiveData("Stockholm")
    val city: LiveData<String> = _city

    private val _weatherResponse = MutableLiveData<Resource<WeatherResponse>>()

    val weatherResponse: LiveData<Resource<WeatherResponse>>
        get() = _weatherResponse

    private val _extendedWeatherData = MutableLiveData<OneCallResponse>()

    val extendedWeatherData: LiveData<OneCallResponse>
        get() = _extendedWeatherData

    init {
        getWeatherData()
    }

    fun onCityChange(newCity: String) {
        _city.value = newCity
        getWeatherData()
    }

    private fun getWeatherData() = viewModelScope.launch {

        city.value?.let { city ->
            _weatherResponse.postValue(Resource.loading(null))
            delay(1000)
            mainRepository.getWeather(city).let { response ->
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        getExtendedWeatherData(it.coord.lat, it.coord.lon).join()
                        _weatherResponse.postValue(Resource.success(it))
                    }
                } else {
                    _weatherResponse.postValue(Resource.error(response.errorBody().toString(), null))
                }
            }
        }
    }

    private fun getExtendedWeatherData(lat: Float, lon: Float) = viewModelScope.launch {
        mainRepository.getExtendedData(lat, lon).let { response ->
            if (response.isSuccessful) {
                _extendedWeatherData.postValue(response.body())
            }
        }
    }
}
