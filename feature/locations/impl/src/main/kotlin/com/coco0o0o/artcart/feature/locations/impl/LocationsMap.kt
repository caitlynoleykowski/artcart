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
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import androidx.compose.foundation.layout.*
import com.coco0o0o.artcart.core.ui.LocationsItem
import com.google.maps.android.compose.CameraMoveStartedReason

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
    onSaveLocationClick: (String, Boolean) -> Unit,
    onLocationClick: (String) -> Unit,
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
    onUserPannedMap = onMapPan,
    onLocationClick = onLocationClick,
    onSaveLocationClick = onSaveLocationClick,
)

internal fun ArtCartLatLng.toLatLng() = LatLng(latitude, longitude)

@Composable
private fun GoogleMapWithLocations(
    keyedLocationData: Collection<KeyedLocationData>,
    currentLocation: LatLng?,
    modifier: Modifier = Modifier,
    onUserPannedMap: (LatLng) -> Unit = {},
    onSaveLocationClick: (String, Boolean) -> Unit,
    onLocationClick: (String) -> Unit,
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
        onMapClick = {},
    ) {
        Locations(
            keyedLocationData = keyedLocationData,
            onLocationClick = onLocationClick,
            onSaveLocationClick = onSaveLocationClick,
        )
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving &&
            cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE
        ) {
            onUserPannedMap(cameraPositionState.position.target)
        }
    }
}

@Composable
private fun Locations(
    keyedLocationData: Collection<KeyedLocationData>,
    onSaveLocationClick: (String, Boolean) -> Unit,
    onLocationClick: (String) -> Unit,
) = keyedLocationData.forEach { (key, locationData) ->
    key(key) {
        ArtCartMarker(
            artLocation = locationData.artLocation,
            onSaveLocationClick = onSaveLocationClick,
            onLocationClick = onLocationClick,
        )
    }
}

@Composable
fun ArtCartMarker(
    artLocation: ArtLocation,
    onSaveLocationClick: (String, Boolean) -> Unit,
    onLocationClick: (String) -> Unit,
) {
    val markerState = rememberUpdatedMarkerState(
        position = LatLng(
            artLocation.latitude,
            artLocation.longitude,
        ),
    )

    val markerIcon = rememberMarkerIcon()

    val artLocationId = artLocation.id

    MarkerInfoWindowContent(
        state = markerState,
        icon = markerIcon,
        onClick = {
            markerState.showInfoWindow()
            false
        },
        onInfoWindowClick = {
            onLocationClick(artLocationId)
        }
    ) { marker ->
        LocationsItem(
            name = artLocation.name,
            following = artLocation.isFavorite,
            description = artLocation.address,
            secondaryDescription = artLocation.primaryTypeName,
            locationImageUrl = artLocation.photoUrl,
            onClick = {
                onLocationClick(artLocationId)
            },
            onSaveButtonClick = {
                onSaveLocationClick(artLocationId, !artLocation.isFavorite)
            },
            isSelected = false,
            modifier = Modifier.fillMaxWidth(fraction = .8f),
            showImage = false,
            showSaveButton = false,
        )
//        Column(
//            modifier = Modifier
//                .width(220.dp)
//                .clip(RoundedCornerShape(16.dp))
//                .background(Color.White)
//                .border(
//                    width = 2.dp,
//                    color = Color(0xFFFB4C8E),
//                    shape = RoundedCornerShape(16.dp),
//                )
//                .padding(12.dp),
//        ) {
//
//            Text(
//                text = artLocation.name,
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                color = Color(0xFF4A102A),
//            )
//
//            Spacer(modifier = Modifier.height(6.dp))
//
//            Text(
//                text = artLocation.address,
//                fontSize = 13.sp,
//                color = Color.DarkGray,
//            )
//
//            if (artLocation.rating > 0f) {
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = "⭐ ${artLocation.rating}",
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    color = Color(0xFF4A102A),
//                )
//            }
//        }
    }
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