package com.coco0o0o.artcart.feature.locationdetails.impl.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.coco0o0o.artcart.core.navigation.Navigator
import com.coco0o0o.artcart.feature.locationdetails.api.navigation.LocationDetailsNavKey
import com.coco0o0o.artcart.feature.locationdetails.api.navigation.navigateToLocationDetails
import com.coco0o0o.artcart.feature.locationdetails.impl.LocationScreen
import com.coco0o0o.artcart.feature.locationdetails.impl.LocationViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.locationDetailsEntry(navigator: Navigator) {
    entry<LocationDetailsNavKey>(
        metadata = ListDetailSceneStrategy.detailPane(),
    ) { key ->
        val id = key.id
        LocationScreen(
            showBackButton = true,
            onBackClick = { navigator.goBack() },
            onLocationClick = navigator::navigateToLocationDetails,
            viewModel = hiltViewModel<LocationViewModel, LocationViewModel.Factory>(
                key = id,
            ) { factory ->
                factory.create(id)
            },
        )
    }
}
