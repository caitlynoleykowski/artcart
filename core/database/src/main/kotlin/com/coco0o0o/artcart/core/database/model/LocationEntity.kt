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

package com.coco0o0o.artcart.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coco0o0o.artcart.core.model.data.ArtLocation

@Entity(
    tableName = "locations",
)
data class LocationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val rating: Float = 0.0f,
    val address: String = "",
    val url: String = "",
    val photoUrl: String = "",
    val primaryTypeName: String = "",
    val isFavorite: Boolean = false,
)

fun LocationEntity.asDomainModel() = ArtLocation(
    id = id,
    name = name,
    rating = rating,
    address = address,
    url = url,
    photoUrl = photoUrl,
    primaryTypeName = primaryTypeName,
    isFavorite = isFavorite,
)
