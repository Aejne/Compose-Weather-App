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

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aejne.weather.models.WeatherResponse
import com.aejne.weather.utils.TimeUtil
import com.aejne.weather.utils.sampleWeatherData
import com.example.weather.R

@Composable
fun SunDetails(
    modifier: Modifier = Modifier,
    weatherResponse: WeatherResponse
) {
    var targetProgress: Double by rememberSaveable { mutableStateOf(0.0) }

    val progressValue by animateFloatAsState(
        targetValue = (targetProgress * 180.0).toFloat(),
        animationSpec = tween(2000, 500)
    )

    LaunchedEffect(0) {
        val currentTime = TimeUtil.currentTime()
        Log.d("SunDetails", "LaunchedEffect")
        targetProgress = TimeUtil.getTimeProgress(
            weatherResponse.sys.sunrise.toLong(),
            currentTime,
            weatherResponse.sys.sunset.toLong()
        ).coerceIn(0.0, 1.0)
        Log.d("SunDetails", "LaunchedEffect, new targetProgress = $targetProgress")
    }

    val arcColor = MaterialTheme.colors.onSurface

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(280.dp)
                .padding(horizontal = 24.dp)
        ) {
            drawArc(
                color = arcColor,
                startAngle = 180f,
                sweepAngle = 180f,
                topLeft = Offset(0f, 150f),
                useCenter = false,
                style = Stroke(
                    width = 16.0f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 20f), 20f)
                )
            )

            drawArc(
                color = Color.Yellow,
                startAngle = 180f,
                sweepAngle = progressValue,
                topLeft = Offset(0f, 150f),
                useCenter = false,
                style = Stroke(
                    width = 16.0f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 20f), 20f)
                )
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-100).dp)
        ) {
            SunDescription(
                contentAlignment = Alignment.TopStart,
                drawable = R.drawable.ic_sunrise,
                timestamp = weatherResponse.sys.sunrise
            )

            SunDescription(
                contentAlignment = Alignment.TopEnd,
                drawable = R.drawable.ic_sunset,
                timestamp = weatherResponse.sys.sunset
            )
        }
    }
}

@Composable
fun SunDescription(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment,
    drawable: Int,
    timestamp: String
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = contentAlignment) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = drawable),
                contentDescription = "Sunrise",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
            )
            Text(
                modifier = modifier.padding(top = 2.dp),
                text = TimeUtil.formatTimestamp(timestamp = timestamp),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Preview
@Composable
fun SunDetailsPreview() {
    SunDetails(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(color = Color.White),
        weatherResponse = sampleWeatherData
    )
}
