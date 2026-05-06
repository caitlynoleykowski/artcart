package com.coco0o0o.artcart.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.coco0o0o.artcart.feature.saved.api.navigation.SavedNavKey
import com.coco0o0o.artcart.feature.locations.api.R
import com.coco0o0o.artcart.feature.locations.api.navigation.LocationsNavKey
import com.coco0o0o.artcart.feature.saved.api.R as savedR

/**
 * Type for the top level navigation items in the application. Contains UI information about the
 * current route that is used in the top app bar and common navigation UI.
 *
 * @param selectedIcon The icon to be displayed in the navigation UI when this destination is
 * selected.
 * @param unselectedIcon The icon to be displayed in the navigation UI when this destination is
 * not selected.
 * @param iconTextId Text that to be displayed in the navigation UI.
 * @param titleTextId Text that is displayed on the top app bar.
 */
data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
)

val BOOKMARKS = TopLevelNavItem(
    selectedIcon = Icons.Rounded.ShoppingCart,
    unselectedIcon = Icons.Outlined.ShoppingCart,
    iconTextId = savedR.string.feature_saved_api_title,
    titleTextId = savedR.string.feature_saved_api_title,
)

val INTERESTS = TopLevelNavItem(
    selectedIcon = Icons.Rounded.LocationCity,
    unselectedIcon = Icons.Rounded.LocationCity,
    iconTextId = R.string.feature_locations_api_title,
    titleTextId = R.string.feature_locations_api_title,
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    LocationsNavKey to INTERESTS,
    SavedNavKey to BOOKMARKS,
)
