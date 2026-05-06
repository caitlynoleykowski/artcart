package com.coco0o0o.artcart.feature.locationdetails.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.result.Result
import com.coco0o0o.artcart.core.result.asResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = LocationViewModel.Factory::class)
class LocationViewModel @AssistedInject constructor(
    val locationsRepository: LocationsRepository,
    @Assisted val locationId: String,
) : ViewModel() {
    val locationUiState: StateFlow<LocationUiState> = locationUiState(
        locationId = locationId,
        locationsRepository = locationsRepository,
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocationUiState.Loading,
        )

    fun followLocationToggle(isFavorite: Boolean) {
        viewModelScope.launch {
            locationsRepository.updateLocationIsFavorite(locationId, isFavorite = isFavorite)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            locationId: String,
        ): LocationViewModel
    }
}

private fun locationUiState(
    locationId: String,
    locationsRepository: LocationsRepository,
): Flow<LocationUiState> {
    return locationsRepository.getLocation(locationId) // TODO: pull additional data from network
        .asResult()
        .map { locationResult ->
            when (locationResult) {
                is Result.Success -> {
                    val location = locationResult.data
                    LocationUiState.Success(
                        location,
                    )
                }

                is Result.Loading -> LocationUiState.Loading
                is Result.Error -> LocationUiState.Error
            }
        }
}

sealed interface LocationUiState {
    data class Success(val location: ArtLocation) : LocationUiState
    data object Error : LocationUiState
    data object Loading : LocationUiState
}