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

import com.example.weather.R
import kotlin.math.roundToInt

object DataUtil {
    fun formatTemperature(temperature: Double): String {
        return temperature.roundToInt().toString()
    }

    fun getWeatherIcon(weatherDescription: String): Int? {
        return when (weatherDescription) {
            "Clear" -> R.drawable.ic_weather_day_sunny
            "Clouds" -> R.drawable.ic_weather_day_cloudy
            "Snow" -> R.drawable.ic_weather_snow
            "Drizzle" -> R.drawable.ic_weather_showers
            "Haze" -> R.drawable.ic_weather_day_haze
            "Fog" -> R.drawable.ic_weather_day_fog
            "Smoke" -> R.drawable.ic_weather_smoke
            else -> null
        }
    }
}
