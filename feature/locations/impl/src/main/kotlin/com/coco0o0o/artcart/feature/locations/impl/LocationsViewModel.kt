package com.coco0o0o.artcart.feature.locations.impl

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.domain.GetFavoriteableLocationsUseCase
import com.coco0o0o.artcart.core.domain.RefreshLocationsUseCase
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.feature.locations.api.navigation.LocationsNavKey
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = LocationsViewModel.Factory::class)
class LocationsViewModel @AssistedInject constructor(
    private val savedStateHandle: SavedStateHandle,
    getFollowableArtLocationsUseCase: GetFavoriteableLocationsUseCase,
    private val refreshLocationsUseCase: RefreshLocationsUseCase,
    private val locationsRepository: LocationsRepository,
    // TODO: see comment below
    @Assisted val key: LocationsNavKey,
) : ViewModel() {

    val permissionGrantedState = MutableStateFlow<Boolean?>(null)

    val uiState: StateFlow<ArtNearbyUiState> =
        combine(
            permissionGrantedState,
            getFollowableArtLocationsUseCase()
        ) { permissionGranted, locations ->
            when (permissionGranted) {
                false -> {
                    Firebase.crashlytics.log("LocationsViewModel uiState permissionGranted: $permissionGranted")
                    ArtNearbyUiState.PermissionNotGranted
                }
                true -> {
                    Firebase.crashlytics.log("LocationsViewModel uiState permissionGranted: $permissionGranted")
                    if (locations.isNotEmpty()) {
                        ArtNearbyUiState.ArtLocations(
                            currentLocation = currentLocation.value, // must be non null by now
                            artLocations = locations,
                        )
                    } else {
                        ArtNearbyUiState.Empty
                    }
                }
                null -> {
                    Firebase.crashlytics.log("LocationsViewModel uiState permissionGranted: $permissionGranted")
                    ArtNearbyUiState.Loading
                }
            }
        }
            .onEach {
                Firebase.crashlytics.log("LocationsViewModel uiState onEach: $it")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ArtNearbyUiState.Loading,
            )
    val currentLocation = MutableStateFlow<ArtCartLatLng?>(null)

    init {
        currentLocation
            .distinctUntilChanged {
                old, new -> old?.latitude == new?.latitude && old?.longitude == new?.longitude
            }
            .onEach {
                if (it != null) {
                    Firebase.crashlytics.log("LocationsViewModel updateCurrentLocation: ${it.latitude}, ${it.longitude}")
                    refreshLocationsUseCase(lat = it.latitude, lng = it.longitude)
                }
            }.launchIn(viewModelScope)
    }

    fun updateCurrentLocation(lat: Double, lng: Double) {
        Log.d("LocationsViewModel", "updateCurrentLocation: $lat, $lng")
        Firebase.crashlytics.log("LocationsViewModel updateCurrentLocation: $lat, $lng")
        viewModelScope.launch {
            currentLocation.value = (ArtCartLatLng(lat, lng))
        }
    }

    fun onSaveLocation(locationId: String, followed: Boolean) {
        viewModelScope.launch {
            locationsRepository.updateLocationIsFavorite(locationId, isFavorite = followed)
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {
        permissionGrantedState.value = granted
    }

    @AssistedFactory
    interface Factory {
        fun create(key: LocationsNavKey): LocationsViewModel
    }
}

sealed interface ArtNearbyUiState {
    data object Loading : ArtNearbyUiState

    data class ArtLocations(
        val currentLocation: ArtCartLatLng?,
        val artLocations: List<ArtLocation>,
    ) : ArtNearbyUiState

    data object PermissionNotGranted : ArtNearbyUiState
    data object Empty : ArtNearbyUiState
}
