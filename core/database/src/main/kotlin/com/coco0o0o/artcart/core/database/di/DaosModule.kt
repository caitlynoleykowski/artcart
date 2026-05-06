package com.coco0o0o.artcart.core.database.di

import com.coco0o0o.artcart.core.database.ArtCartDatabase
import com.coco0o0o.artcart.core.database.dao.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesLocationDao(
        database: ArtCartDatabase,
    ): LocationDao = database.locationDao()
}
