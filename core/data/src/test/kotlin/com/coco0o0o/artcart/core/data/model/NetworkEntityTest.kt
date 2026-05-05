/*
 * Copyright 2022 The Android Open Source Project
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

package com.coco0o0o.artcart.core.data.model

import com.coco0o0o.artcart.core.network.model.DisplayName
import com.coco0o0o.artcart.core.network.model.LatLng
import com.coco0o0o.artcart.core.network.model.NetworkLocation
import com.coco0o0o.artcart.core.network.model.PrimaryTypeDisplayName
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals

class NetworkEntityTest {

    @Test
    fun networkLocationToDatabaseModel() {
        val networkLocation = NetworkLocation(
            id = "0",
            rating = 0f,
            displayName = DisplayName(
                text = "testname0",
                languageCode = "test language"
            ),
            formattedAddress = "test address 0",
            location = LatLng(0.0, 0.0),
            photos = emptyList(),
            primaryTypeDisplayName = PrimaryTypeDisplayName(
                text = "test type"
            ),
        )
        val entity = networkLocation.toLocationEntity() // TODO

        assertEquals("0", entity.id)
        assertEquals("testname0", entity.name)
        assertEquals("testurl0", entity.url)
    }
}
