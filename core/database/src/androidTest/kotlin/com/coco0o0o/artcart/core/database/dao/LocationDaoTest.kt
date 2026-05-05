/*
 * Copyright 2024 The Android Open Source Project
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

package com.coco0o0o.artcart.core.database.dao

import com.coco0o0o.artcart.core.database.model.LocationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class LocationDaoTest : DatabaseTest() {

    @Test
    fun getLocations() = runTest {
        insertLocations()

        val savedLocations = locationDao.getLocationEntities().first()

        assertEquals(
            listOf("1", "2", "3"),
            savedLocations.map { it.id },
        )
    }

    @Test
    fun getTopic() = runTest {
        insertLocations()

        val savedLocationEntity = locationDao.getLocationEntity("2").first()

        assertEquals("performance", savedLocationEntity.name)
    }

    @Test
    fun upsertLocation() = runTest {
        insertLocations()
        locationDao.upsertLocations(
            listOf(testLocationEntity("1", "compose")),
        )

        val savedLocations = locationDao.getLocationEntities()

        assertEquals(3, savedLocations.first().size)
    }

    private suspend fun insertLocations() {
        val locationEntities = listOf(
            testLocationEntity("1", "compose"),
            testLocationEntity("2", "performance"),
            testLocationEntity("3", "headline"),
        )
        locationDao.upsertLocations(locationEntities)
    }
}

private fun testLocationEntity(
    id: String = "0",
    name: String,
) = LocationEntity(
    id = id,
    name = name,
    url = "",
)
