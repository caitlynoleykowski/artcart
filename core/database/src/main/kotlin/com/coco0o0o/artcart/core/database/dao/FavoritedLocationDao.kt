package com.coco0o0o.artcart.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.coco0o0o.artcart.core.database.model.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query(value = "SELECT * FROM locations WHERE id = :id")
    fun getLocationEntity(id: String): Flow<LocationEntity>

    @Query(value = "SELECT * FROM locations")
    fun getLocationEntities(): Flow<List<LocationEntity>>

    @Query(value = "SELECT * FROM locations WHERE isFavorite = 1")
    suspend fun getFavoriteLocationEntities(): List<LocationEntity>

    @Query(value = "SELECT * FROM locations WHERE isFavorite = 1")
    fun getFavoriteLocationEntitiesFlow(): Flow<List<LocationEntity>>

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertLocations(entities: List<LocationEntity>)

    /**
     * Updates isFavorite in location entity with id
     */
    @Query("UPDATE locations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateLocationIsFavorite(id: String, isFavorite: Boolean)

    @Query("DELETE FROM locations WHERE isFavorite = 0")
    suspend fun deleteNonFavorites()

    @Query(
        "DELETE FROM locations WHERE id NOT IN (:idsToKeep)"
    )
    suspend fun deleteAllExcept(idsToKeep: List<String>)
}
