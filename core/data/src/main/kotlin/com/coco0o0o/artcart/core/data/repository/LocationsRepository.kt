package com.coco0o0o.artcart.core.data.repository

import com.coco0o0o.artcart.core.model.data.ArtLocation
import kotlinx.coroutines.flow.Flow

interface LocationsRepository { //}: Syncable {

    suspend fun refreshLocations(lat: Double, lng: Double)

    fun getLocations(): Flow<List<ArtLocation>>

    fun getFavoriteLocations(): Flow<List<ArtLocation>>

    suspend fun updateLocationIsFavorite(id: String, isFavorite: Boolean)

    fun getLocation(id: String): Flow<ArtLocation>
}
