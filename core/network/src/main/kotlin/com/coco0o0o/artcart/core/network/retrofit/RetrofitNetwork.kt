package com.coco0o0o.artcart.core.network.retrofit

import androidx.tracing.trace
import com.coco0o0o.artcart.core.network.NetworkDataSource
import com.coco0o0o.artcart.core.network.model.NetworkPlaces
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Int
import kotlin.String
import kotlin.collections.List

const val API_KEY = "AIzaSyAqjWW92spl7JH6ah_u9yGiFAaLSBUuBos"

private interface RetrofitArtCartNetworkApi {

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

    @Headers(
        "X-Goog-FieldMask: *",
        "X-Goog-Api-Key: $API_KEY"
    )
    @POST(value = "v1/places:searchNearby")
    suspend fun getLocations(
        @Body locationsBody: LocationsBody,
    ): NetworkPlaces
}

private const val PLACES_BASE_URL = "https://places.googleapis.com"

@Singleton
internal class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : NetworkDataSource {

    private val networkApi = trace("RetrofitArtCartNetwork") {
        Retrofit.Builder()
            .baseUrl(PLACES_BASE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            // Add ok http interceptor for the API key
            .build()
            .create(RetrofitArtCartNetworkApi::class.java)
    }

    override suspend fun getLocations(latitude: Double, longitude: Double): NetworkPlaces =
        networkApi.getLocations(
            RetrofitArtCartNetworkApi.LocationsBody(
                includedTypes = listOf(
                    "art_gallery",
                    "art_museum",
                    "art_studio",
                    "sculpture"
                ),
             maxResultCount = 20,
                locationRestriction = RetrofitArtCartNetworkApi.LocationRestriction(
                    RetrofitArtCartNetworkApi.Circle(
                        RetrofitArtCartNetworkApi.LatLng(
                            latitude = latitude,
                            longitude = longitude
                        ),
                        radius = 3000.0
                    )
                ),
            )
        )
}
