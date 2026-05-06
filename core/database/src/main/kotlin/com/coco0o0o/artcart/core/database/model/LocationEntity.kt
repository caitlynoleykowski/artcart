package com.coco0o0o.artcart.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coco0o0o.artcart.core.model.data.ArtLocation

@Entity(
    tableName = "locations",
)
data class LocationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val rating: Float = 0.0f, // TODO: Why float, I guess cause we can?
    val url: String = "",
    val photoUrl: String = "",
    val primaryTypeName: String = "",
    val isFavorite: Boolean = false,
)

fun LocationEntity.asDomainModel() = ArtLocation(
    id = id,
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude,
    rating = rating,
    url = url,
    photoUrl = photoUrl,
    primaryTypeName = primaryTypeName,
    isFavorite = isFavorite,
)
