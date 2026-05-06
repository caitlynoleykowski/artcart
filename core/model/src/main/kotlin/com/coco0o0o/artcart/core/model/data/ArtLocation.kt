package com.coco0o0o.artcart.core.model.data

data class ArtLocation(
    val id: String,
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val rating: Float = 0.0f,
    val url: String = "",
    val photoUrl: String = "",
    val primaryTypeName: String = "",
    val isFavorite: Boolean = false,
)
