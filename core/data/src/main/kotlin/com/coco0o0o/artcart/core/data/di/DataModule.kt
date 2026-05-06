package com.coco0o0o.artcart.core.data.di

import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.data.repository.OfflineFirstLocationsRepository
import com.coco0o0o.artcart.core.data.util.ConnectivityManagerNetworkMonitor
import com.coco0o0o.artcart.core.data.util.NetworkMonitor
import com.coco0o0o.artcart.core.data.util.TimeZoneBroadcastMonitor
import com.coco0o0o.artcart.core.data.util.TimeZoneMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsLocationRepository(
        offlineFirstLocationsRepository: OfflineFirstLocationsRepository,
    ): LocationsRepository

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    internal abstract fun binds(impl: TimeZoneBroadcastMonitor): TimeZoneMonitor
}
