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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aejne.weather.models.DailyItem
import com.aejne.weather.models.HourlyItem
import com.aejne.weather.models.OneCallResponse
import com.aejne.weather.models.WeatherResponse
import com.aejne.weather.ui.theme.MyTheme
import com.aejne.weather.utils.DataUtils
import com.aejne.weather.utils.Resource
import com.aejne.weather.utils.Status
import com.aejne.weather.utils.TimeUtil
import com.aejne.weather.utils.sampleExtendedData
import com.aejne.weather.utils.sampleWeatherData
import com.example.weather.R
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@Composable
fun WeatherApp(viewModel: MainViewModel = viewModel()) {
    val city: String by viewModel.city.observeAsState("")
    val weatherResponse: Resource<WeatherResponse> by viewModel.weatherResponse.observeAsState(
        Resource.loading(null)
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
        color = MaterialTheme.colors.surface
    ) {
        when (weatherResponse.status) {
            Status.SUCCESS -> {
                WeatherDetails(
                    city = city,
                    weatherResponse = weatherResponse.data!!,
                    extendedWeatherResponse = extendedWeatherResponse
                )
            }
            Status.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.size(64.dp),
                        painter = painterResource(
                            id = R.drawable.ic_weather_day_lightning
                        ),
                        contentDescription = "Weather per day",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
                    )

                    Text(
                        text = "Could not find any decent weather for $city",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherDetails(
    modifier: Modifier = Modifier,
    city: String,
    weatherResponse: WeatherResponse,
    extendedWeatherResponse: OneCallResponse
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

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
                HourWeatherItem(item = it)
            }
        }

        extendedWeatherResponse.daily.forEach {
            DayWeatherItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                item = it
            )
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
fun HourWeatherItem(
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
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
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
fun DayWeatherItem(
    modifier: Modifier = Modifier,
    item: DailyItem
) {

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                Text(
                    text = TimeUtil.formatWeekDay(item.dt)
                )
            }

            val dayTemp = stringResource(
                id = R.string.degree_format,
                DataUtils.formatTemperature(item.temp.day)
            )

            val nightTemp = stringResource(
                id = R.string.degree_format,
                DataUtils.formatTemperature(item.temp.night)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                Row {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = "$dayTemp / $nightTemp",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.body2
                    )
                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(
                            id = DataUtils.getWeatherIcon(item.weather.first().main)
                                ?: R.drawable.ic_weather_day_sunny
                        ),
                        contentDescription = "Weather per day",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
                    )
                }
            }
        }
    }
}

@Preview("Day weather item")
@Composable
fun DayWeatherPreview() {
    MyTheme {
        DayWeatherItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .wrapContentHeight(),
            item = sampleExtendedData.daily.first()
        )
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
