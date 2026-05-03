/*
 * Copyright 2021 The Android Open Source Project
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

package com.coco0o0o.artcart.feature.interests.impl

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coco0o0o.artcart.core.designsystem.component.NiaBackground
import com.coco0o0o.artcart.core.designsystem.component.NiaLoadingWheel
import com.coco0o0o.artcart.core.designsystem.theme.NiaTheme
import com.coco0o0o.artcart.core.ui.DevicePreviews
import com.coco0o0o.artcart.feature.interests.api.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import java.util.concurrent.TimeUnit
import kotlin.collections.forEach

@SuppressLint("MissingPermission") // TODO: Confused as to whether this is a problem if the user has accepted yet
@Composable
fun InterestsScreen(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InterestsViewModel,
    shouldHighlightSelectedTopic: Boolean = false,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    /*
      Start handle location updates
     */
    var locationRequest by remember {
        mutableStateOf<LocationRequest?>(null)
    }
    // Keeps track of received location updates as text
    var locationUpdates by remember {
        mutableStateOf("")
    }

    // TODO: This is aggressively updating the location, reconsider
    LaunchedEffect(locationRequest) {
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.MINUTES.toMillis(3)
        ).build()
    }
    // Only register the location updates effect when we have a request
    if (locationRequest != null) {
        LocationUpdatesEffect(locationRequest!!) { result ->
            result.locations.firstOrNull()?.let {
                viewModel.updateCurrentLocation(lat = it.latitude, lng = it.longitude)
            }
            // For each result update the text
            for (currentLocation in result.locations) {
                locationUpdates = "${System.currentTimeMillis()}:\n" +
                        "- @lat: ${currentLocation.latitude}\n" +
                        "- @lng: ${currentLocation.longitude}\n" +
                        "- Accuracy: ${currentLocation.accuracy}\n\n" +
                        locationUpdates
            }
        }
    }

    /*
      End handle location updates
     */
    Column() {
//        Text(
//            text = "Large Header",
//            style = MaterialTheme.typography.displayLarge // Predefined large style
//        )
        InterestsMap(
            uiState,
            modifier.fillMaxHeight(0.5f)
        )
        InterestsScreen(
            uiState = uiState,
            followTopic = viewModel::followTopic,
            onTopicClick = {
                // TODO: this violates SSOT, events should go through the ViewModel
                //viewModel.onTopicClick(it)
                onTopicClick(it)
            },
            shouldHighlightSelectedTopic = shouldHighlightSelectedTopic,
            //modifier = modifier.background(ComposeColor.Red, RectangleShape),
        )
    }
}

@Composable
fun InterestsMap(
    uiState: ArtNearbyUiState,
    modifier: Modifier = Modifier,
) {
    Screen(uiState, modifier)

//
//    when (uiState) {
//        ArtNearbyUiState.Loading, ArtNearbyUiState.Empty -> { listOf()} // Do nothing
//        is ArtNearbyUiState.ArtLocations -> {
//            uiState.artLocations
////            GoogleMap(
////                googleMapOptionsFactory = {
////                    GoogleMapOptions().mapId("DEMO_MAP_ID")
////                },
////                cameraPositionState = cameraPositionState,
//////        onPOIClick = {
//////            Log.d(TAG, "POI clicked: ${it.name}")
//////        }
////                modifier = modifier,
////            ) {
////                val context = LocalContext.current
////            }
////            GoogleMapWithLocations(
////                artLocations = uiState.artLocations,
////                modifier,
////            )
//        }
//    }
}

//
//@Composable
//private fun GoogleMapWithLocations(
//    artLocations: List<ArtLocation>,
//    modifier: Modifier = Modifier,
////    onAddLocation: (LocationData) -> Unit,
////    onDeleteLocation: (LocationKey) -> Unit
//) {
//    val here = LatLng(38.5636174,-121.4777596)
//    val hereMarkerState = rememberUpdatedMarkerState(position = here)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(here, 10f)
//    }
//    GoogleMap(
//        modifier = modifier,
//        cameraPositionState = cameraPositionState,
//        onMapClick = { position -> print("yayy2!") } //onAddLocation(LocationData(position)) }
//    ) {
//        Locations(artLocations)
//    }
//}
//
//@Composable
//fun Marker(
//    position: LatLng,
//    onClick: () -> Boolean = { false },
//) {
//    val markerState = rememberUpdatedMarkerState(position = position)
//
//    Marker(
//        state = markerState,
//        onClick = { onClick() }
//    )
//}
//
//@Composable
//private fun Locations(
//    artLocations: List<ArtLocation>,
//) = artLocations.forEach { artLocation ->
//    key(artLocation.id) {
//        Marker(
//            position = LatLng(artLocation.latitude, artLocation.longitude),
//            onClick = {
//                print("yayy!!")
//                true // consume click event to prevent camera move to marker
//            }
//        )
//    }
//}

@Composable
internal fun InterestsScreen(
    uiState: ArtNearbyUiState,
    followTopic: (String, Boolean) -> Unit,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    shouldHighlightSelectedTopic: Boolean = false,
) {


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (uiState) {
            ArtNearbyUiState.Loading ->
                NiaLoadingWheel(
                    contentDesc = stringResource(id = R.string.feature_interests_api_loading),
                )

            is ArtNearbyUiState.ArtLocations ->
                LocationsTabContent(
                    locations = uiState.artLocations,
                    onTopicClick = onTopicClick,
                    onFollowButtonClick = followTopic,
                    selectedTopicId = null, // uiState.selectedTopicId,
                    shouldHighlightSelectedTopic = shouldHighlightSelectedTopic,
                )

            is ArtNearbyUiState.Empty -> InterestsEmptyScreen()
        }
    }
    LocationPermissionsEffect()
}

@Composable
private fun InterestsEmptyScreen() {
    Text(text = stringResource(id = R.string.feature_interests_api_empty_header))
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun LocationPermissionsEffect() {
    // Permission requests should only be made from an Activity Context, which is not present
    // in previews
    if (LocalInspectionMode.current) return
    if (VERSION.SDK_INT < VERSION_CODES.TIRAMISU) return
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    )
    LaunchedEffect(locationPermissionState) {
        val status = locationPermissionState.status
        if (status is PermissionStatus.Denied && !status.shouldShowRationale) {
            locationPermissionState.launchPermissionRequest()
        }
    }
}
@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun LocationUpdatesEffect(
    locationRequest: LocationRequest,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onUpdate: (result: LocationResult) -> Unit,
) {
    val context = LocalContext.current
    val currentOnUpdate by rememberUpdatedState(newValue = onUpdate)

    // Whenever on of these parameters changes, dispose and restart the effect.
    DisposableEffect(locationRequest, lifecycleOwner) {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                currentOnUpdate(result)
            }
        }
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                locationClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper(),
                )
            } else if (event == Lifecycle.Event.ON_STOP) {
                locationClient.removeLocationUpdates(locationCallback)
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@DevicePreviews
@Composable
fun InterestsScreenLoading() {
    NiaTheme {
        NiaBackground {
            InterestsScreen(
                uiState = ArtNearbyUiState.Loading,
                followTopic = { _, _ -> },
                onTopicClick = {},
            )
        }
    }
}

@DevicePreviews
@Composable
fun InterestsScreenEmpty() {
    NiaTheme {
        NiaBackground {
            InterestsScreen(
                uiState = ArtNearbyUiState.Empty,
                followTopic = { _, _ -> },
                onTopicClick = {},
            )
        }
    }
}















//private class DataModel {
//    /**
//     * Location data.
//     */
//    val locationDataMap = mutableStateMapOf<LocationKey, LocationData>()
//}

/**
 * Data type representing a location.
 *
 * This only stores location position, for demonstration purposes,
 * but could hold other data related to the location.
 */
@Immutable
private data class LocationData(val position: LatLng)

/**
 * Unique, stable key for location
 */
private class LocationKey

private typealias KeyedLocationData = Pair<LocationKey, LocationData>

/**
 * Demonstrates how to sync a data model with a changing collection of
 * location markers using keys.
 *
 * The user can add a location marker to the model by clicking the map and delete a location from
 * the model by clicking a marker.
 *
 * This example reuses the simple non-draggable Marker approach from the
 * `UpdatingNoDragMarkerWithDataModelActivity` example, which encapsulates
 * [MarkerState] to provide a cleaner API surface.
 */
//class MarkersCollectionActivity : ComponentActivity() {
//    private val dataModel = DataModel()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MapsComposeSampleTheme {
//                Screen(
//                    dataModel = dataModel,
//                    modifier = Modifier.fillMaxSize()
//                        .systemBarsPadding(),
//                )
//            }
//        }
//    }
//}

val sacramento = LatLng(38.5636174,-121.4777596)
val defaultCameraPosition = CameraPosition.fromLatLngZoom(sacramento, 11f)

@Composable
private fun Screen(
    uiState: ArtNearbyUiState,
    modifier: Modifier = Modifier
) = GoogleMapWithLocations(
    modifier = modifier,
    keyedLocationData = when(uiState) {
        ArtNearbyUiState.Loading, ArtNearbyUiState.Empty -> emptyList()
        is ArtNearbyUiState.ArtLocations -> uiState.artLocations.map { LocationKey() to LocationData(LatLng(it.latitude, it.longitude))}
    },
//    onAddLocation = { locationData ->
//        dataModel.locationDataMap += LocationKey() to locationData
//    },
//    onDeleteLocation = { key ->
//        dataModel.locationDataMap -= key
//    }
)

/**
 * A GoogleMap with locations represented by markers
 *
 * @param keyedLocationData model data for location markers with unique keys.
 * Uses a [Collection] type to keep it independent of our data model.
 * @param onAddLocation location addition events for updating data model
 * @param onDeleteLocation location deletion events for updating data model
 */
@Composable
private fun GoogleMapWithLocations(
    keyedLocationData: Collection<KeyedLocationData>,
    modifier: Modifier = Modifier,
//    onAddLocation: (LocationData) -> Unit,
//    onDeleteLocation: (LocationKey) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState { position = defaultCameraPosition }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = {} // position -> onAddLocation(LocationData(position)) }
    ) {
        Locations(
            keyedLocationData = keyedLocationData,
            onLocationClick = {} // onDeleteLocation
        )
    }
}

/**
 * Renders locations on a GoogleMap
 *
 * @param keyedLocationData model data for location markers with unique keys.
 * @param onLocationClick location click events
 */
@Composable
private fun Locations(
    keyedLocationData: Collection<KeyedLocationData>,
    onLocationClick: (LocationKey) -> Unit
) = keyedLocationData.forEach { (key, locationData) ->
    key(key) {
        Marker(
            position = locationData.position,
            onClick = {
                onLocationClick(key)
                true // consume click event to prevent camera move to marker
            }
        )
    }
}

@Composable
fun Marker(
    position: LatLng,
    onClick: () -> Boolean = { false },
) {
    val markerState = rememberUpdatedMarkerState(position = position)

    Marker(
        state = markerState,
        onClick = { onClick() }
    )
}
