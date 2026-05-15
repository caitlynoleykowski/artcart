package com.coco0o0o.artcart.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.coco0o0o.artcart.feature.locations.api.R
import com.coco0o0o.artcart.feature.locations.api.navigation.LocationsNavKey
import com.coco0o0o.artcart.feature.saved.api.navigation.SavedNavKey
import com.coco0o0o.artcart.feature.saved.api.R as savedR

data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    @DrawableRes val titleDrawableResId: Int?,
)

val SAVED = TopLevelNavItem(
    selectedIcon = Icons.Rounded.ShoppingCart,
    unselectedIcon = Icons.Outlined.ShoppingCart,
    iconTextId = savedR.string.feature_saved_api_title,
    titleTextId = savedR.string.feature_saved_api_title,
    titleDrawableResId = null,
)

val LOCATIONS = TopLevelNavItem(
    selectedIcon = Icons.Rounded.LocationCity,
    unselectedIcon = Icons.Rounded.LocationCity,
    iconTextId = R.string.feature_locations_api_title,
    titleTextId = R.string.feature_locations_api_app_title,
    titleDrawableResId = com.coco0o0o.artcart.core.designsystem.R.drawable.art_cart_logo
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    LocationsNavKey to LOCATIONS,
    SavedNavKey to SAVED,
)
