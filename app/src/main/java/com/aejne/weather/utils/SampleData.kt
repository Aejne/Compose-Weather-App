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
package com.aejne.weather.utils

import com.aejne.weather.models.Coord
import com.aejne.weather.models.DailyItem
import com.aejne.weather.models.HourlyItem
import com.aejne.weather.models.Main
import com.aejne.weather.models.OneCallResponse
import com.aejne.weather.models.Sys
import com.aejne.weather.models.Temp
import com.aejne.weather.models.Weather
import com.aejne.weather.models.WeatherResponse
import com.aejne.weather.models.Wind

val sampleWeatherData = WeatherResponse(
    coord = Coord(
        18.0649f,
        59.3326f
    ),
    weather = listOf(
        Weather(
            id = 800,
            main = "Clear",
            description = "Clear sky",
            icon = "01n"
        )
    ),
    main = Main(
        temp = -1.73,
        feels_like = -5.9,
        temp_min = -2.78,
        temp_max = 4.2,
        pressure = 1025.0,
        humidity = 51.0
    ),
    wind = Wind(
        speed = 1.54,
        deg = 120.0
    ),
    sys = Sys(
        sunrise = "1616129539",
        sunset = "1616173121"
    )
)

val sampleExtendedData = OneCallResponse(
    hourly = listOf(
        HourlyItem(
            "1616129539",
            14.4,
            listOf(
                Weather(
                    id = 800,
                    main = "Clear",
                    description = "Clear sky",
                    icon = "01n"
                )
            )
        )
    ),
    daily = listOf(
        DailyItem(
            "1616129539",
            Temp(10.0, 8.5, 12.1, 7.2),
            listOf(
                Weather(
                    id = 800,
                    main = "Clear",
                    description = "Clear sky",
                    icon = "01n"
                )
            )
        )
    )
)
