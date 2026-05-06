package com.coco0o0o.artcart.feature.locations.impl.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.coco0o0o.artcart.core.navigation.Navigator
import com.coco0o0o.artcart.feature.locations.api.navigation.LocationsNavKey
import com.coco0o0o.artcart.feature.locations.impl.LocationsDetailPlaceholder
import com.coco0o0o.artcart.feature.locations.impl.LocationsScreen
import com.coco0o0o.artcart.feature.locations.impl.LocationsViewModel
import com.coco0o0o.artcart.feature.locationdetails.api.navigation.navigateToLocationDetails

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
            // TODO: This event should either be provided by the ViewModel or by the navigator, not both
            onLocationClick = navigator::navigateToLocationDetails,

            // TODO: This should be dynamically calculated based on the rendering scene
            //  See https://github.com/android/nav3-recipes/commit/488f4811791ca3ed7192f4fe3c86e7371b32ebdc#diff-374e02026cdd2f68057dd940f203dc4ba7319930b33e9555c61af7e072211cabR89
            shouldHighlightSelectedTopic = false,
            viewModel = viewModel,
        )
    }
}
