package com.coco0o0o.artcart.core.domain

import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.model.data.ArtLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 */
class GetFavoriteableLocationsUseCase @Inject constructor(
    private val locationsRepository: LocationsRepository,
) {
    operator fun invoke(): Flow<List<ArtLocation>> {
        return locationsRepository.getLocations()
    }
}
