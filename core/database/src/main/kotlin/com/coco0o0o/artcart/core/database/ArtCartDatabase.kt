package com.coco0o0o.artcart.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coco0o0o.artcart.core.database.dao.LocationDao
import com.coco0o0o.artcart.core.database.model.LocationEntity
import com.coco0o0o.artcart.core.database.util.InstantConverter

@Database(
    entities = [
        LocationEntity::class,
    ],
    version = 1,
    autoMigrations = [
    ],
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
)
internal abstract class ArtCartDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
}
