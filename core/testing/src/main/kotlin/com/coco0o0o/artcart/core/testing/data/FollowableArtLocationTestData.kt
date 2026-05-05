/*
 * Copyright 2023 The Android Open Source Project
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

@file:Suppress("ktlint:standard:max-line-length")

package com.coco0o0o.artcart.core.testing.data

import com.coco0o0o.artcart.core.model.data.FavoriteableArtLocation
import com.coco0o0o.artcart.core.model.data.ArtLocation

val favoriteableArtLocationTestData: List<FavoriteableArtLocation> = listOf(
    FavoriteableArtLocation(
        artLocation = ArtLocation(
            id = "2",
            name = "Headlines",
            url = "",
        ),
        isFavorited = false,
    ),
    FavoriteableArtLocation(
        artLocation = ArtLocation(
            id = "3",
            name = "UI",
            url = "",
        ),
        isFavorited = true,
    ),
    FavoriteableArtLocation(
        artLocation = ArtLocation(
            id = "4",
            name = "Testing",
            url = "",
        ),
        isFavorited = false,
    ),
)
