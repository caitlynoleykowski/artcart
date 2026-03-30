/*
 * Copyright 2022 The Android Open Source Project
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

package com.coco0o0o.artcart.core.data.repository

import com.coco0o0o.artcart.core.database.dao.LocationDao
import com.coco0o0o.artcart.core.database.model.LocationEntity
import com.coco0o0o.artcart.core.database.model.asDomainModel
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.network.NetworkDataSource
import com.coco0o0o.artcart.core.network.model.NetworkLocation
import com.coco0o0o.artcart.core.network.model.toPhotoUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Disk storage backed implementation of the [LocationsRepository].
 * Reads are exclusively from local storage to support offline access.
 */
internal class OfflineFirstLocationsRepository @Inject constructor(
    private val locationDao: LocationDao,
    private val network: NetworkDataSource,
) : LocationsRepository {

    override suspend fun refreshLocations(lat: Double, lng: Double) { // TODO: insert params lat lng
        val networkPlaces = network.getLocations(
            latitude = lat,
            longitude = lng,
        )
        val networkLocations = networkPlaces.places
        // TODO: Switch to io dispatcher maybe?
        // Get favorites and update the network results with that data
        val favorites = locationDao.getFavoriteLocationEntities()
        val locationEntities = networkLocations
            .map { networkLocation ->
                val localMatches = favorites.filter { it.id == networkLocation.id }
                val isFavorite = localMatches.firstOrNull()?.isFavorite ?: false
                networkLocation.toLocationEntity(isFavorite = isFavorite)
            }
        // Delete existing non favorites
        locationDao.deleteNonFavorites()
        // Upsert using network results with favorited data
        locationDao.upsertLocations(
            entities = locationEntities,
        )
    }

    fun NetworkLocation.toLocationEntity(isFavorite: Boolean = false): LocationEntity {
        return LocationEntity(
            id = id,
            name = displayName.text,
            address = formattedAddress,
            latitude = location.latitude,
            longitude = location.longitude,
            rating = rating,
            url = "",
            photoUrl = photos.firstOrNull()?.name?.toPhotoUrl() ?: "",
            primaryTypeName = primaryTypeDisplayName.text,
            isFavorite = isFavorite,
        )
    }

    override fun getLocations(): Flow<List<ArtLocation>> =
        locationDao.getLocationEntities().map { locationEntities ->
            locationEntities.map(LocationEntity::asDomainModel)
        }

    override fun getFavoriteLocations(): Flow<List<ArtLocation>> =
        locationDao.getFavoriteLocationEntitiesFlow().map { locationEntities ->
            locationEntities.map(LocationEntity::asDomainModel)
        }

    override suspend fun updateLocationIsFavorite(id: String, isFavorite: Boolean) {
        locationDao.updateLocationIsFavorite(
            id = id,
            isFavorite = isFavorite,
        )
    }

    override fun getLocation(id: String): Flow<ArtLocation> {
        return locationDao.getLocationEntity(id).map(LocationEntity::asDomainModel)
    }
}
