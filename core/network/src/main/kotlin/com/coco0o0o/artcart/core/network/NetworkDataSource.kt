package com.coco0o0o.artcart.core.network

import com.coco0o0o.artcart.core.network.model.NetworkPlaces

interface NetworkDataSource {
    suspend fun getLocations(latitude: Double, longitude: Double): NetworkPlaces
}
