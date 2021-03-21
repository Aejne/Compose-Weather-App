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
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    // @GET("weather?q={city}&units=metric&appid=e23c7fd08bbbbd2608b67c01c0b6f00a")
    @GET("weather")
    suspend fun getWeatherData(
        @QueryMap options: Map<String, String>
    ): Response<WeatherResponse>

    @GET("onecall")
    suspend fun getExtraData(
        @QueryMap options: Map<String, String>
    ): Response<OneCallResponse>
}
