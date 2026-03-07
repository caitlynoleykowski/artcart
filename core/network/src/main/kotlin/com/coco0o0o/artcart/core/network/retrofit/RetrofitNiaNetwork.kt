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

package com.coco0o0o.artcart.core.network.retrofit

import androidx.tracing.trace
import com.coco0o0o.artcart.core.network.NiaNetworkDataSource
import com.coco0o0o.artcart.core.network.model.NetworkChangeList
import com.coco0o0o.artcart.core.network.model.NetworkLocation
import com.coco0o0o.artcart.core.network.model.NetworkNewsResource
import com.coco0o0o.artcart.core.network.model.NetworkPlaces
import com.coco0o0o.artcart.core.network.model.NetworkTopic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Int
import kotlin.String
import kotlin.collections.List

const val API_KEY = "AIzaSyAqjWW92spl7JH6ah_u9yGiFAaLSBUuBos"

/**
 * Retrofit API declaration for NIA Network API
 */
private interface RetrofitNiaNetworkApi {

    @Serializable
    data class LatLng(
        val latitude: Double,
        val longitude: Double,
    )
    @Serializable
    data class Circle(
        val center: LatLng,
        val radius: Double,
    )
    @Serializable
    data class LocationRestriction(
        val circle: Circle,
    )
    @Serializable
    data class LocationsBody(
        val includedTypes: List<String>,
        val maxResultCount: Int,
        val locationRestriction: LocationRestriction,
    )

//    @GET(value = "topics")
//    suspend fun getTopics(
//        @Query("id") ids: List<String>?,
//    ): NetworkResponse<List<NetworkTopic>>

    // e.g.
    /*
        {
          "includedTypes": [
            "art_gallery",
            "art_museum",
            "art_studio",
            "museum",
            "performing_arts_theater",
            "sculpture"
          ],
          "maxResultCount": 10,
          "locationRestriction": {
            "circle": {
              "center": {
                "latitude": 38.5636216,
                "longitude": -121.4803345
              },
              "radius": 500
            }
          }
        }
     */
    @Headers(
        "X-Goog-FieldMask: *",
        "X-Goog-Api-Key: $API_KEY"
    )
    @POST(value = "v1/places:searchNearby")
    suspend fun getLocations(
        @Body locationsBody: LocationsBody,
    ): NetworkPlaces

    @GET(value = "topics")
    suspend fun getTopics(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkTopic>>

    @GET(value = "newsresources")
    suspend fun getNewsResources(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkNewsResource>>

    @GET(value = "changelists/topics")
    suspend fun getTopicChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/newsresources")
    suspend fun getNewsResourcesChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>
}

//private const val NIA_BASE_URL = "https://places.googleapis.com/v1/places/GyuEmsRBfy61i59si0?fields=addressComponents&key=YOUR_API_KEY"
private const val NIA_BASE_URL = "https://places.googleapis.com"

/**
 * Wrapper for data provided from the [NIA_BASE_URL]
 */
@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

/**
 * [Retrofit] backed [NiaNetworkDataSource]
 */
@Singleton
internal class RetrofitNiaNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : NiaNetworkDataSource {

    private val networkApi = trace("RetrofitNiaNetwork") {
        Retrofit.Builder()
            .baseUrl(NIA_BASE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            // Add http interceptor for the API key
            .build()
            .create(RetrofitNiaNetworkApi::class.java)
    }

    override suspend fun getTopics(ids: List<String>?): List<NetworkTopic> =
        networkApi.getTopics(ids = ids).data

    override suspend fun getLocations(latitude: Double, longitude: Double): NetworkPlaces =
        networkApi.getLocations(
            RetrofitNiaNetworkApi.LocationsBody(
                includedTypes = listOf(
                    "art_gallery",
                    "art_museum",
                    "art_studio",
                    "sculpture"
                ),
             maxResultCount = 20,
                locationRestriction = RetrofitNiaNetworkApi.LocationRestriction(
                    RetrofitNiaNetworkApi.Circle(
                        RetrofitNiaNetworkApi.LatLng(
                            latitude = latitude,
                            longitude = longitude
                        ),
                        radius = 3000.0
                    )
                ),
            )
        )

    override suspend fun getNewsResources(ids: List<String>?): List<NetworkNewsResource> =
        networkApi.getNewsResources(ids = ids).data

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getTopicChangeList(after = after)

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getNewsResourcesChangeList(after = after)
}
