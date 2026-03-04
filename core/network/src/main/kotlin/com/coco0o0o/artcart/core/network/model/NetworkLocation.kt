package com.coco0o0o.artcart.core.network.model

import com.coco0o0o.artcart.core.model.data.ArtLocation
import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
data class DisplayName(
    val text: String = "",
    val languageCode: String = "",
)

/**
 * Network representation of [ArtLocation]
 */
@Serializable
data class NetworkLocation(
    val id: String,
    val rating: Float = 0.0f,
    val displayName: DisplayName = DisplayName(),
    val formattedAddress: String = "",
)

// TODO: Give json names and name the variables what they are in the domain
@Serializable
data class NetworkPlaces(
    val places: List<NetworkLocation> = emptyList(),
)

fun NetworkLocation.toDomainModel(): ArtLocation =
    ArtLocation(
        id = id,
        rating = rating,
        name = displayName.text,
        address = formattedAddress,
        url = "",
    )
