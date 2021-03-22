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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aejne.weather.models.WeatherResponse
import com.aejne.weather.ui.theme.MyTheme
import com.aejne.weather.ui.theme.pink100
import com.aejne.weather.utils.DataUtils
import com.aejne.weather.utils.sampleWeatherData
import com.example.weather.R

@Composable
fun CurrentWeather(
    modifier: Modifier = Modifier,
    weatherResponse: WeatherResponse
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                WeatherIndicator(weatherDescription = weatherResponse.weather.first().main)
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = DataUtils.formatTemperature(weatherResponse.main.temp),
                    style = MaterialTheme.typography.h3
                )
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .offset(x = (-8).dp, y = (-8).dp),
                    painter = painterResource(id = R.drawable.ic_weather_celsius),
                    contentDescription = "Celsius",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            val feelsLikeTemp = stringResource(
                id = R.string.degree_format,
                formatArgs = arrayOf(DataUtils.formatTemperature(weatherResponse.main.feels_like))
            )
            Text(
                text = "Feels like $feelsLikeTemp",
                style = MaterialTheme.typography.body2
            )

            val hiTemp = stringResource(
                id = R.string.degree_format,
                formatArgs = arrayOf(DataUtils.formatTemperature(weatherResponse.main.temp_max))
            )
            val lowTemp = stringResource(
                id = R.string.degree_format,
                formatArgs = arrayOf(DataUtils.formatTemperature(weatherResponse.main.temp_min))
            )

            Text(
                text = "Hi: $hiTemp | Low: $lowTemp",
                style = MaterialTheme.typography.body2
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            WeatherDetail(
                title = "Humidity",
                value = "${weatherResponse.main.humidity}",
                suffix = "%",
                drawable = R.drawable.ic_humidity,
                contentDescription = "Humidity"
            )

            WeatherDetail(
                title = "Pressure",
                value = "${weatherResponse.main.pressure}",
                suffix = " mBar",
                drawable = R.drawable.ic_barometer,
                contentDescription = "Humidity"
            )

            WeatherDetail(
                title = "Wind",
                value = "${weatherResponse.wind.speed}",
                suffix = " km/h",
                drawable = R.drawable.ic_wind_deg,
                drawableRotation = (weatherResponse.wind.deg).toFloat(),
                contentDescription = "Wind"
            )
        }
    }
}

@Composable
fun WeatherIndicator(weatherDescription: String) {
    DataUtils.getWeatherIcon(weatherDescription = weatherDescription)?.let {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = it),
                contentDescription = "Current weather image",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
            )
            Text(
                text = weatherDescription,
                fontSize = 12.sp
            )
        }
    } ?: run {
        Text(
            text = weatherDescription,
            fontSize = 12.sp
        )
    }
}

@Preview("Current weather")
@Composable
fun CurrentWeatherPreview() {
    MyTheme {
        CurrentWeather(
            modifier = Modifier.background(pink100),
            weatherResponse = sampleWeatherData
        )
    }
}
