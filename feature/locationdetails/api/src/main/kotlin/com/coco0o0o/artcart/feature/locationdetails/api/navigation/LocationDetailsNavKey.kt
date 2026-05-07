package com.coco0o0o.artcart.feature.locationdetails.api.navigation

import androidx.navigation3.runtime.NavKey
import com.coco0o0o.artcart.core.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class LocationDetailsNavKey(val id: String) : NavKey

fun Navigator.navigateToLocationDetails(
    topicId: String,
) {
    navigate(LocationDetailsNavKey(topicId))
}
