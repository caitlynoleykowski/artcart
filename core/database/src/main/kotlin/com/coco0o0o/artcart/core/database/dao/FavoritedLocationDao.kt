package com.coco0o0o.artcart.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.coco0o0o.artcart.core.database.model.LocationEntity
import com.coco0o0o.artcart.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query(value = "SELECT * FROM locations")
    fun getLocationEntities(): Flow<List<LocationEntity>>

    @Query(value = "SELECT * FROM locations WHERE isFavorite = 1")
    suspend fun getFavoriteLocationEntities(): List<LocationEntity>

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertLocations(entities: List<LocationEntity>)

    @Query("DELETE FROM locations WHERE isFavorite = 0")
    suspend fun deleteNonFavorites()
}
