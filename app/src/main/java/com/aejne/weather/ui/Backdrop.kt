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

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aejne.weather.ui.theme.MyTheme
import com.aejne.weather.utils.verticalGradientScrim
import dev.chrisbanes.accompanist.insets.statusBarsPadding

val cityList = listOf(
    "Stockholm",
    "Paris",
    "London",
    "Gabes",
    "Berlin",
    "Moscow",
    "Sydney",
    "Bogota",
    "Buenos Aires",
    "New York",
    "Beijing",
    "Mumbai",
    "Tel Aviv",
    "Los Angeles",
    "Detroit",
    "Rio de Janeiro"
)

@Composable
fun Backdrop(
    modifier: Modifier = Modifier,
    onNavClick: () -> Unit,
    onCitySelected: (String) -> Unit
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalGradientScrim(
                    color = Color.Black.copy(alpha = 0.38f),
                    startYPercentage = 0.0f,
                    endYPercentage = 1.0f
                )
                .statusBarsPadding()
        ) {
            HomeAppBar(
                modifier = Modifier.fillMaxWidth(),
                onNavClick = onNavClick
            )

            Column(
                modifier = Modifier
                    .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
                    .padding(start = 16.dp, top = 8.dp),
            ) {
                cityList.forEach {
                    CityChip(
                        modifier = Modifier.padding(vertical = 4.dp),
                        city = it,
                        onClick = { city -> onCitySelected(city) }
                    )
                }
            }
        }
    }
}

@Composable
fun CityChip(
    modifier: Modifier = Modifier,
    city: String,
    onClick: (String) -> Unit = { }
) {
    Text(
        modifier = modifier
            .border(
                color = MaterialTheme.colors.onSurface,
                shape = RoundedCornerShape(percent = 50),
                width = 1.dp
            )
            .clickable { onClick(city) }
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp
            ),
        color = MaterialTheme.colors.onSurface,
        text = city,
        style = MaterialTheme.typography.body2
    )
}

@Preview("Dark City chip")
@Composable
fun CityChipPreviewDark() {
    MyTheme(darkTheme = true) {
        CityChip(city = "Stockholm")
    }
}

@Preview("Light City chip")
@Composable
fun CityChipPreviewLight() {
    MyTheme(darkTheme = false) {
        CityChip(city = "Stockholm")
    }
}

@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    onNavClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavClick) {
                    Icon(imageVector = Icons.Outlined.WbSunny, contentDescription = "Navigation icon")
                }

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    text = "ComposeWeather",
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center
                )
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = modifier
    )
}

@Preview
@Composable
fun BackdropPreview() {
    MyTheme {
        Backdrop(modifier = Modifier.fillMaxSize(), onNavClick = { /*TODO*/ }, onCitySelected = { })
    }
}
