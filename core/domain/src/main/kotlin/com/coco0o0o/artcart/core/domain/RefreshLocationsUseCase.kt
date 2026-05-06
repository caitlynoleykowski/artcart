package com.coco0o0o.artcart.core.domain

import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import javax.inject.Inject

class RefreshLocationsUseCase @Inject constructor(
    private val locationsRepository: LocationsRepository,
) {
    suspend operator fun invoke(lat: Double, lng: Double) {
        return locationsRepository.refreshLocations(lat, lng)
    }
}
