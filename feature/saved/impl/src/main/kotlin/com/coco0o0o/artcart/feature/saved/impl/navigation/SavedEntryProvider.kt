package com.coco0o0o.artcart.feature.saved.impl.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.coco0o0o.artcart.core.navigation.Navigator
import com.coco0o0o.artcart.feature.saved.api.navigation.SavedNavKey
import com.coco0o0o.artcart.feature.saved.impl.SavedScreen
import com.coco0o0o.artcart.feature.locationdetails.api.navigation.navigateToLocationDetails

fun EntryProviderScope<NavKey>.savedEntry(navigator: Navigator) {
    entry<SavedNavKey> {
        SavedScreen(
            onLocationClick = navigator::navigateToLocationDetails,
        )
    }
}

// TODO: Why is this here?
val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState state should be initialized at runtime")
}
