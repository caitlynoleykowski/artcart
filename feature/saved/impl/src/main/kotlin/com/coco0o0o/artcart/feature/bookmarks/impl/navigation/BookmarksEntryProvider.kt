/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coco0o0o.artcart.feature.saved.impl.navigation

import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.coco0o0o.artcart.core.navigation.Navigator
import com.coco0o0o.artcart.feature.saved.api.navigation.SavedNavKey
import com.coco0o0o.artcart.feature.saved.impl.SavedScreen2
import com.coco0o0o.artcart.feature.saved.impl.SavedViewModel2
import com.coco0o0o.artcart.feature.locationdetails.api.navigation.navigateToLocationDetails

fun EntryProviderScope<NavKey>.savedEntry(navigator: Navigator) {
    entry<SavedNavKey> {
        SavedScreen2(
            onLocationClick = navigator::navigateToLocationDetails,
        )
    }
}

// TODO: Why is this here?
val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState state should be initialized at runtime")
}
