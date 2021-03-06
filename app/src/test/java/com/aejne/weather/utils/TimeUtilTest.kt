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

import junit.framework.TestCase.assertEquals
import org.junit.Test

class TimeUtilTest {
    @Test
    fun testSunrise() {
        val date = TimeUtil.formatTimestamp("1616129539")
        assertEquals("05:52", date)
    }

    @Test
    fun testSunset() {
        val date = TimeUtil.formatTimestamp("1616173121")
        assertEquals("17:58", date)
    }

    @Test
    fun testProgress() {
        val currentTime = 1616251975960
        val progress = TimeUtil.getTimeProgress(1616129539, currentTime, 1616173121)

        assertEquals(0.82, progress, 0.01)
    }
}
