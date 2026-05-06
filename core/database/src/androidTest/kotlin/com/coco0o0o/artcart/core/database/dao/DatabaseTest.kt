package com.coco0o0o.artcart.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.coco0o0o.artcart.core.database.ArtCartDatabase
import org.junit.After
import org.junit.Before

internal abstract class DatabaseTest {

    private lateinit var db: ArtCartDatabase
    protected lateinit var locationDao: LocationDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                ArtCartDatabase::class.java,
            ).build()
        }
        locationDao = db.locationDao()
    }

    @After
    fun teardown() = db.close()
}
