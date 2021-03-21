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
package com.aejne.weather.api

import com.aejne.weather.models.OneCallResponse
import com.aejne.weather.models.WeatherResponse
import retrofit2.Response

// api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
interface ApiHelper {

    suspend fun getWeatherData(city: String): Response<WeatherResponse>
    suspend fun getExtendedData(lat: Float, lon: Float): Response<OneCallResponse>
}
