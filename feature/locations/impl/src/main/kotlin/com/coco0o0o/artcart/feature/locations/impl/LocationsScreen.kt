package com.coco0o0o.artcart.feature.locations.impl

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coco0o0o.artcart.core.designsystem.component.ArtCartBackground
import com.coco0o0o.artcart.core.designsystem.component.ArtCartLoadingWheel
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.ui.DevicePreviews
import com.coco0o0o.artcart.feature.locations.api.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission") // TODO: Confused as to whether this is a problem if the user has accepted yet
@Composable
fun LocationsScreen(
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationsViewModel,
    shouldHighlightSelectedTopic: Boolean = false,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()

    val context = LocalContext.current

    /*
      Start handle location updates
     */
    var locationRequest by remember {
        mutableStateOf<LocationRequest?>(null)
    }
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    // Keeps track of received location updates as text
    var locationUpdates by remember {
        mutableStateOf("")
    }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onLocationPermissionResult(granted)
        hasLocationPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            viewModel.onLocationPermissionResult(granted = true)
            // Get initial location
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener {
                viewModel.updateCurrentLocation(lat = it.latitude, lng = it.longitude)
            }

            val POLL_LOCATION = false
            if (POLL_LOCATION) {
                // Get updates every minute, trying to keep this minimal but effective
                locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    TimeUnit.MINUTES.toMillis(1)
                ).build()
            }
        }
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
    SplitSliderScreen(
        topContent = {
            LocationsMap(
                uiState,
                currentLocation,
//                modifier.fillMaxHeight(0.5f),
                onMapPan = {
                    viewModel.updateCurrentLocation(
                        lat = it.latitude,
                        lng = it.longitude
                    )
                }
            )
        },
        bottomContent = {
            Column() {
                Text(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 0.dp,
                    ),
                    text = stringResource(id = R.string.feature_locations_api_nearby_art),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                LocationsScreen(
                    uiState = uiState,
                    saveLocation = viewModel::saveLocation,
                    onLocationClick = {
                        onLocationClick(it)
                    },
                    shouldHighlightSelectedLocation = shouldHighlightSelectedTopic,
                )
            }
        },
    )
}

@Composable
fun LocationsMap(
    uiState: ArtNearbyUiState,
    currentLocation: LatLng?,
    modifier: Modifier = Modifier,
    onMapPan: (LatLng) -> Unit = {},
) {
    Screen(uiState, currentLocation, modifier, onMapPan)
}

@Composable
internal fun LocationsScreen(
    uiState: ArtNearbyUiState,
    saveLocation: (String, Boolean) -> Unit,
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    shouldHighlightSelectedLocation: Boolean = false,
) {


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (uiState) {
            ArtNearbyUiState.Loading ->
                Box(modifier.fillMaxSize()) {
                    ArtCartLoadingWheel(
                        modifier = modifier
                            .align(Alignment.Center),
                        contentDesc = stringResource(id = R.string.feature_locations_api_loading),
                    )
                }
            is ArtNearbyUiState.ArtLocations ->
                LocationsTabContent(
                    locations = uiState.artLocations,
                    onLocationClick = onLocationClick,
                    onSaveClick = saveLocation,
                    selectedLocationId = null, // TODO: uiState.selectedLocationId,
                    shouldHighlightSelectedLocation = shouldHighlightSelectedLocation,
                )
            is ArtNearbyUiState.PermissionNotGranted -> {
                Box(modifier.fillMaxSize()) {
                    LocationsPermissionNotGrantedScreen(
                        modifier = modifier
                            .align(Alignment.Center),
                    )
                }
            }
            is ArtNearbyUiState.Empty -> {
                Box(modifier.fillMaxSize()) {
                    LocationsEmptyScreen(
                        modifier = modifier
                            .align(Alignment.Center),
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationsEmptyScreen(modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = stringResource(id = R.string.feature_locations_api_empty_header),)
}

@Composable
private fun LocationsPermissionNotGrantedScreen(modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = stringResource(id = R.string.feature_locations_api_location_permissions_not_granted),)
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
fun LocationsScreenLoading() {
    ArtCartTheme {
        ArtCartBackground {
            LocationsScreen(
                uiState = ArtNearbyUiState.Loading,
                saveLocation = { _, _ -> },
                onLocationClick = {},
            )
        }
    }
}

@DevicePreviews
@Composable
fun LocationsScreenEmpty() {
    ArtCartTheme {
        ArtCartBackground {
            LocationsScreen(
                uiState = ArtNearbyUiState.Empty,
                saveLocation = { _, _ -> },
                onLocationClick = {},
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
private data class LocationData(val artLocation: ArtLocation)

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

val sacramento = LatLng(0.0,0.0)
val defaultCameraPosition = CameraPosition.fromLatLngZoom(sacramento, 11f)

@Composable
private fun Screen(
    uiState: ArtNearbyUiState,
    currentLocation: LatLng?,
    modifier: Modifier = Modifier,
    onMapPan: (LatLng) -> Unit = {},
) = GoogleMapWithLocations(
    modifier = modifier,
    currentLocation = currentLocation,
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
//    onAddLocation = { locationData ->
//        dataModel.locationDataMap += LocationKey() to locationData
//    },
//    onDeleteLocation = { key ->
//        dataModel.locationDataMap -= key
//    }
    onMapPan = onMapPan,
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
    currentLocation: LatLng?,
    modifier: Modifier = Modifier,
//    onAddLocation: (LocationData) -> Unit,
//    onDeleteLocation: (LocationKey) -> Unit
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