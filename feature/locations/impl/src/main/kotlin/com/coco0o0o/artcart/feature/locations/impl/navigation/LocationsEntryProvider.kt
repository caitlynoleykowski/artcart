package com.coco0o0o.artcart.feature.locations.impl.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.coco0o0o.artcart.core.navigation.Navigator
import com.coco0o0o.artcart.feature.locationdetails.api.navigation.navigateToLocationDetails
import com.coco0o0o.artcart.feature.locations.api.navigation.LocationsNavKey
import com.coco0o0o.artcart.feature.locations.impl.LocationsDetailPlaceholder
import com.coco0o0o.artcart.feature.locations.impl.LocationsScreen
import com.coco0o0o.artcart.feature.locations.impl.LocationsViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.locationsEntry(navigator: Navigator) {
    entry<LocationsNavKey>(
        metadata = ListDetailSceneStrategy.listPane {
            LocationsDetailPlaceholder()
        },
    ) { key ->
        val viewModel = hiltViewModel<LocationsViewModel, LocationsViewModel.Factory> {
            it.create(key)
        }
        LocationsScreen(
            onLocationClick = navigator::navigateToLocationDetails,
            viewModel = viewModel,
        )
    }
}
