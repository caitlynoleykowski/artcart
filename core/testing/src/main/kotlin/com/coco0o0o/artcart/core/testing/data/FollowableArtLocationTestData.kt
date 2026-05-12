
@file:Suppress("ktlint:standard:max-line-length")
package com.coco0o0o.artcart.core.testing.data

import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.model.data.FavoriteableArtLocation

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
