package com.coco0o0o.artcart.core.testing.repository

import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.model.data.ArtLocation
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestLocationsRepository : LocationsRepository {
    private val locationsFlow: MutableSharedFlow<List<ArtLocation>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun refreshLocations(lat: Double, lng: Double) {
        TODO("Not yet implemented")
    }

    override fun getLocations(): Flow<List<ArtLocation>> = locationsFlow
    override fun getFavoriteLocations(): Flow<List<ArtLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateLocationIsFavorite(id: String, isFavorite: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getLocation(id: String): Flow<ArtLocation> =
        locationsFlow.map { topics -> topics.find { it.id == id }!! }

    // Used by tests only
    fun updateLocations(artLocations: List<ArtLocation>) {
        locationsFlow.tryEmit(artLocations)
    }

}
