package com.coco0o0o.artcart.core.data.repository

import android.util.Log
import com.coco0o0o.artcart.core.database.dao.LocationDao
import com.coco0o0o.artcart.core.database.model.LocationEntity
import com.coco0o0o.artcart.core.database.model.asDomainModel
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.network.Dispatcher
import com.coco0o0o.artcart.core.network.Dispatchers
import com.coco0o0o.artcart.core.network.NetworkDataSource
import com.coco0o0o.artcart.core.network.di.ApplicationScope
import com.coco0o0o.artcart.core.network.model.NetworkLocation
import com.coco0o0o.artcart.core.network.model.toPhotoUrl
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
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
    @ApplicationScope private val applicationScope: CoroutineScope,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : LocationsRepository {

    override suspend fun refreshLocations(lat: Double, lng: Double) { // TODO: insert params lat lng
        try {
            val networkPlaces = network.getLocations(
                latitude = lat,
                longitude = lng,
            )
            val networkLocations = networkPlaces.places
            Firebase.crashlytics.log("refreshLocations: $lat, $lng, networkLocations size: ${networkLocations.size}")
            // TODO: Switch to io dispatcher maybe?
            // Get favorites and update the network results with that data
            val favorites = locationDao.getFavoriteLocationEntities()
            val networkLocationsWithIsFavoritedData = networkLocations
                .map { networkLocation ->
                    val localMatches = favorites.filter { it.id == networkLocation.id }
                    val isFavorite = localMatches.firstOrNull()?.isFavorite ?: false
                    networkLocation.toLocationEntity(isFavorite = isFavorite)
                }
            // Upsert using network results with favorited data
            locationDao.upsertLocations(
                entities = networkLocationsWithIsFavoritedData,
            )
            // Delete old results
            val idsToKeep = favorites.map { it.id} + networkLocationsWithIsFavoritedData.map { it.id }
            Log.d("OfflineFirstLocationsRepository", "refreshLocations: idsToKeep: ${idsToKeep}")
            locationDao.deleteAllExcept(idsToKeep)
        } catch (e: Exception) {
            Firebase.crashlytics.log("refreshLocations: $lat, $lng")
            Firebase.crashlytics.recordException(e)
        }
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

    override fun getLocations(): Flow<List<ArtLocation>> {
        return locationDao.getLocationEntities().map { locationEntities ->
            Firebase.crashlytics.log("getLocations: locationEntities size: ${locationEntities.size}")
            locationEntities.map(LocationEntity::asDomainModel)
        }
    }

//    fun getFlashcardSets(userId: String): Flow<List<FlashcardSet>> {
//
//        applicationScope.launch(ioDispatcher) { // launch a coroutine so the db Flow can be returned before the network call finishes - important for low network connectivity / high network latency use cases
//
//            try {
//
//                val flashcardSets = networkDataSource.getFlashcardSets(userId)
//                flashcardSetDao.upsert(flashcardSets)
//
//            } catch (e: IOException) { // Catch only IOException to avoid swallowing a coroutine CancellationException
//
//                // Log e, silent failure to update data from network
//                Log.i("FlashcardSetsRepository", "Failure to refresh data from network", e)
//
//            }
//
//        }
//
//        return flashcardSetDao.getAll(userId)
//
//    }

    // This one is good if we're good with a silent failure from the network
//    override fun getLocations(): Flow<List<ArtLocation>> {
//        applicationScope.launch(ioDispatcher) {
//            Log.i("OfflineFirstLocationsRepository", "started playing around")
//            try {
//                refreshLocations(0.0, 0.0)
//            } catch (e: IOException) {
//                Log.i("OfflineFirstLocationsRepository", "IOException", e)
//            }
//        }
//        print("finished playing around")
//        Log.i("OfflineFirstLocationsRepository", "finished playing around")
//        return locationDao.getLocationEntities().map { locationEntities ->
//            locationEntities.map(LocationEntity::asDomainModel)
//        }
//    }

//    // Network call to refresh data with errors propogated back up and flow of db values returned
//    data class LocationsResult(
//        val networkResult: LocationsNetworkResult,
//        val locations: Flow<List<ArtLocation>>,
//    )
//    sealed interface LocationsNetworkResult {
//        data object Success : LocationsNetworkResult
//        data class Error(val throwable: Throwable) : LocationsNetworkResult
//    }
//    override suspend fun getLocations(): LocationsResult {
//        val networkResult = try {
//            withTimeout(2.seconds) {
//                val networkPlaces = network.getLocations(latitude = 0.0, longitude = 0.0)
//                val networkLocations = networkPlaces.places
//                val locationEntities = networkLocations.map { it.toLocationEntity() }
//                locationDao.upsertLocations(
//                    entities = locationEntities,
//                )
//            }
//            LocationsNetworkResult.Success
//        } catch(e: TimeoutCancellationException) {
//            LocationsNetworkResult.Error(e)
//        } catch(e: IOException) {
//            LocationsNetworkResult.Error(e)
//        }
//
//        return LocationsResult(
//            networkResult = networkResult,
//            locations = locationDao.getLocationEntities()
//                .map { entities -> entities.map(LocationEntity::asDomainModel) }
//        )
//    }

    // Nothing about this one makes sense. It's squeezing two things into one call for no purpose
//    fun getThings(): Pair<Flow<LocationsNetworkResult>, Flow<List<ArtLocation>>> {
//        val networkResultFlow = flow {
//            val networkResult = try {
//                val networkPlaces = network.getLocations(latitude = 0.0, longitude = 0.0)
//                val networkLocations = networkPlaces.places
//                val locationEntities = networkLocations.map { it.toLocationEntity() }
//                locationDao.upsertLocations(
//                    entities = locationEntities,
//                )
//                LocationsNetworkResult.Success
//            } catch (e: TimeoutCancellationException) {
//                LocationsNetworkResult.Error(e)
//            } catch (e: IOException) {
//                LocationsNetworkResult.Error(e)
//            }
//            emit(networkResult)
//        }
//        val dbFlow = locationDao.getLocationEntities().map { locationEntities ->
//                    locationEntities.map(LocationEntity::asDomainModel)
//                }
//        return Pair(networkResultFlow, dbFlow)
//    }

//    fun getThings(): Flow<Result<List<ArtLocation>>> {
//        try {
//            val result = withTimeout(2000L) {
//                async {
//                    // Perform long-running task
//                    "Success"
//                }.await()
//            }
//        } catch (e: TimeoutCancellationException) {
//            // Handle the timeout
//        }
//        val dbFlow = locationDao.getLocationEntities().map { locationEntities ->
//            locationEntities.map(LocationEntity::asDomainModel)
//        }
//            .map { Result.success(it) }
//        return dbFlow
//    }
//
//    suspend fun getFlashcardSets(userId: Int): Flow<List<FlashcardSet>> {
//        try {
//            val flashcardSets = networkDataSource.getFlashcardSets(userId
//        } catch (e: Exception) {
//            // Log e, silent failure
//        }
//        flashcardSetDao.upsert(flashcardSets)
//        return flashcardSetDao.getAll(userId)
//    }

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
