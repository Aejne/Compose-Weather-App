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

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtil {
    fun formatTimestamp(timestamp: String): String {
        return Instant
            .ofEpochMilli(timestamp.toLong() * 1000)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun formatHour(timestamp: String): String {
        return Instant
            .ofEpochMilli(timestamp.toLong() * 1000)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .format(DateTimeFormatter.ofPattern("HH"))
    }

    fun currentTime(): Long {
        return Instant.now().toEpochMilli()
    }

    fun getTimeProgress(startTimestamp: Long, currentTimestamp: Long, endTimestamp: Long): Double {
        val startTemporal = Instant
            .ofEpochMilli(startTimestamp * 1000)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()

        val endTemporal = Instant
            .ofEpochMilli(endTimestamp * 1000)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()

        val currentTime = Instant
            .ofEpochMilli(currentTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()

        val totalDuration = Duration.between(startTemporal, endTemporal)
        val passedDuration = Duration.between(startTemporal, currentTime)

        return (passedDuration.seconds.toDouble() / totalDuration.seconds.toDouble())
    }
}
