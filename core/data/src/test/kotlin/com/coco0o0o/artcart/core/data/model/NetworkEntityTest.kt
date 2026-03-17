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

import com.coco0o0o.artcart.core.model.data.NewsResource
import com.coco0o0o.artcart.core.model.data.Topic
import com.coco0o0o.artcart.core.network.model.NetworkNewsResource
import com.coco0o0o.artcart.core.network.model.NetworkTopic
import com.coco0o0o.artcart.core.network.model.asExternalModel
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals

class NetworkEntityTest {

    // TODO: NetworkLocation
//    @Test
//    fun networkTopicMapsToDatabaseModel() {
//        val networkModel = NetworkTopic(
//            id = "0",
//            name = "Test",
//            shortDescription = "short description",
//            longDescription = "long description",
//            url = "URL",
//            imageUrl = "image URL",
//        )
//        val entity = networkModel.asEntity()
//
//        assertEquals("0", entity.id)
//        assertEquals("Test", entity.name)
//        assertEquals("short description", entity.shortDescription)
//        assertEquals("long description", entity.longDescription)
//        assertEquals("URL", entity.url)
//        assertEquals("image URL", entity.imageUrl)
//    }
}
