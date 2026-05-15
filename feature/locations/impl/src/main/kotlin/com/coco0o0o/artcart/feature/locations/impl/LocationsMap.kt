package com.coco0o0o.artcart.feature.locations.impl

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.feature.locations.api.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Immutable
private data class LocationData(val artLocation: ArtLocation)
private class LocationKey
private typealias KeyedLocationData = Pair<LocationKey, LocationData>

val nullIsland = LatLng(0.0,0.0)
val defaultCameraPosition = CameraPosition.fromLatLngZoom(nullIsland, 11f)

@Composable
fun ArtCartMap(
    uiState: ArtNearbyUiState,
    currentLocation: ArtCartLatLng?,
    modifier: Modifier = Modifier,
    onMapPan: (LatLng) -> Unit = {},
) = GoogleMapWithLocations(
    modifier = modifier,
    currentLocation = currentLocation?.toLatLng(),
    keyedLocationData = when(uiState) {
        ArtNearbyUiState.Loading,
        ArtNearbyUiState.Empty,
        ArtNearbyUiState.PermissionNotGranted
            -> emptyList()
        is ArtNearbyUiState.ArtLocations ->
            uiState.artLocations.map {
                LocationKey() to LocationData(artLocation = it)
            }
    },
    onMapPan = onMapPan,
)

internal fun ArtCartLatLng.toLatLng() = LatLng(latitude, longitude)

@Composable
private fun GoogleMapWithLocations(
    keyedLocationData: Collection<KeyedLocationData>,
    currentLocation: LatLng?,
    modifier: Modifier = Modifier,
    onMapPan: (LatLng) -> Unit = {},
) {

    val cameraPositionState = rememberCameraPositionState { position = defaultCameraPosition }

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 13f)
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = {}, // position -> onAddLocation(LocationData(position)) }
    ) {
        Locations(
            keyedLocationData = keyedLocationData,
            onLocationClick = {} // onDeleteLocation
        )
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            onMapPan(cameraPositionState.position.target)
        }
    }
}

@Composable
private fun Locations(
    keyedLocationData: Collection<KeyedLocationData>,
    onLocationClick: (LocationKey) -> Unit
) = keyedLocationData.forEach { (key, locationData) ->
    key(key) {
        ArtCartMarker(
            artLocation = locationData.artLocation,
            onClick = {
                onLocationClick(key)
                false // use default click behavior
            },
        )
    }
}

@Composable
fun ArtCartMarker(
    artLocation: ArtLocation,
    onClick: () -> Boolean = { false },
) {
    // The below uses default marker info window functionality. Add custom info window later
    val markerState = rememberUpdatedMarkerState(
        position = LatLng(
            artLocation.latitude,
            artLocation.longitude,
        ),
    )
    val markerIcon = rememberMarkerIcon()

    Marker(
        state = markerState,
        icon = markerIcon,
        title = artLocation.name,
        snippet = buildString {
            append(artLocation.address)

            if (artLocation.rating > 0f) {
                append("\n⭐ ${artLocation.rating}")
            }
        },
        onClick = { onClick() },
    )
}

@Composable
fun rememberMarkerIcon(): BitmapDescriptor {
    val context = LocalContext.current

    return remember {
        val drawable = ContextCompat.getDrawable(
            context,
            R.drawable.feature_locations_api_ic_map_marker
        )!!

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}