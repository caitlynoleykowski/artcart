package com.coco0o0o.artcart.core.network.model

import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.network.retrofit.API_KEY
import kotlinx.serialization.Serializable

@Serializable
data class DisplayName(
    val text: String = "",
    val languageCode: String = "",
)

@Serializable
data class LatLng(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)

@Serializable
data class Photos(
    val name: String,
)

@Serializable
data class PrimaryTypeDisplayName(
    val text: String = "",
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
    val location: LatLng, // TODO: This really shouldn't have a default value we should throw out the location if it's missing
    val photos: List<Photos> = emptyList(),
    val primaryTypeDisplayName: PrimaryTypeDisplayName = PrimaryTypeDisplayName(),
)

// TODO: Give json names and name the variables what they are in the domain
@Serializable
data class NetworkPlaces(
    val places: List<NetworkLocation> = emptyList(),
)

fun String.toPhotoUrl(): String {
    return "https://places.googleapis.com/v1/$this/media?key=${API_KEY}&maxWidthPx=200&maxHeightPx=200"
}