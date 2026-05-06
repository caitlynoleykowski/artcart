package com.coco0o0o.artcart.core.database.di

import android.content.Context
import androidx.room.Room
import com.coco0o0o.artcart.core.database.ArtCartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesArtCartDatabase(
        @ApplicationContext context: Context,
    ): ArtCartDatabase = Room.databaseBuilder(
        context,
        ArtCartDatabase::class.java,
        "artcart-database",
    ).build()
}
