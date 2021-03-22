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

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aejne.weather.models.HourlyItem
import com.aejne.weather.models.OneCallResponse
import com.aejne.weather.models.WeatherResponse
import com.aejne.weather.ui.theme.MyTheme
import com.aejne.weather.ui.theme.pink100
import com.aejne.weather.utils.DataUtils
import com.aejne.weather.utils.TimeUtil
import com.aejne.weather.utils.sampleExtendedData
import com.aejne.weather.utils.sampleWeatherData
import com.example.weather.R
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@Composable
fun WeatherApp(viewModel: MainViewModel = viewModel()) {
    val city: String by viewModel.city.observeAsState("")
    val weatherResponse: WeatherResponse by viewModel.weatherResponse.observeAsState(
        sampleWeatherData
    )
    val extendedWeatherResponse: OneCallResponse by viewModel.extendedWeatherData.observeAsState(
        sampleExtendedData
    )

    var expanded: Boolean by rememberSaveable { mutableStateOf(false) }

    val frontTranslation by animateDpAsState(targetValue = if (expanded) 220.dp else 0.dp)

    Surface(modifier = Modifier.fillMaxSize()) {
        Backdrop(onNavClick = { expanded = !expanded }) {
            viewModel.onCityChange(it)
            expanded = false
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = frontTranslation, y = 56.dp)
            .statusBarsPadding()
            .clickable(enabled = expanded) { expanded = false },
        elevation = 12.dp,
        shape = CutCornerShape(topStart = 32.dp),
        color = MaterialTheme.colors.background
    ) {

        /*Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background",
            contentScale = ContentScale.FillHeight
        )*/

        WeatherDetails(
            modifier = Modifier.statusBarsPadding(),
            city = city,
            weatherResponse = weatherResponse,
            extendedWeatherResponse = extendedWeatherResponse
        )
    }
}

@Composable
fun WeatherDetails(
    modifier: Modifier = Modifier,
    city: String,
    weatherResponse: WeatherResponse,
    extendedWeatherResponse: OneCallResponse
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .scrollable(rememberScrollState(), Orientation.Vertical),

    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = city,
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        CurrentWeather(weatherResponse = weatherResponse)

        Spacer(modifier = Modifier.height(32.dp))

        LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(extendedWeatherResponse.hourly) {
                HourItem(item = it)
            }
        }

        SunDetails(
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 16.dp),
            weatherResponse = weatherResponse
        )
    }
}

@Composable
fun HourItem(
    modifier: Modifier = Modifier,
    item: HourlyItem
) {
    Column(
        modifier = modifier
            .width(64.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = TimeUtil.formatHour(item.dt),
            style = MaterialTheme.typography.body2
        )
        Image(
            modifier = Modifier.size(48.dp),
            painter = painterResource(
                id = DataUtils.getWeatherIcon(item.weather.first().main)
                    ?: R.drawable.ic_weather_day_sunny
            ),
            contentDescription = "Weather per day",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground)
        )
        Text(
            text = stringResource(
                id = R.string.degree_format,
                formatArgs = arrayOf(DataUtils.formatTemperature(item.temp))
            ),
            fontSize = 14.sp,
            style = MaterialTheme.typography.body2
        )
    }
}

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
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground)
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
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground)
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

@Composable
fun WeatherDetail(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    suffix: String,
    drawable: Int,
    drawableRotation: Float = 0f,
    contentDescription: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 4.dp),
            text = "$title: $value$suffix"
        )
        Image(
            modifier = Modifier
                .rotate(drawableRotation)
                .size(24.dp),
            painter = painterResource(id = drawable),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground)
        )
    }
}

@Preview("Current weather")
@Composable
fun CurrentWeatherPreview() {
    MyTheme {
        CurrentWeather(modifier = Modifier.background(pink100), weatherResponse = sampleWeatherData)
    }
}

@Preview("Weather Details")
@Composable
fun WeatherDetailsPreview() {
    MyTheme {
        WeatherDetails(
            city = "Stockholm",
            weatherResponse = sampleWeatherData,
            extendedWeatherResponse = sampleExtendedData
        )
    }
}

// @Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        WeatherApp()
    }
}

// @Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        WeatherApp()
    }
}
