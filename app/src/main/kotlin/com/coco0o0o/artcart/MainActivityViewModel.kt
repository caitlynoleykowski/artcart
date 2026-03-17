/*
 * Copyright 2022 The Android Open Source Project
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

package com.coco0o0o.artcart

import androidx.lifecycle.ViewModel
import com.coco0o0o.artcart.MainActivityUiState.Success
import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    locationsRepository: LocationsRepository, // TODO: Remove this as well as maybe the whole viewmodel
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = MutableStateFlow(Success)
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data object Success : MainActivityUiState {
        override val shouldDisableDynamicTheming = true

        override val shouldUseAndroidTheme: Boolean = false

        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
    }

    /**
     * Returns `true` if the state wasn't loaded yet and it should keep showing the splash screen.
     */
    fun shouldKeepSplashScreen() = this is Loading

    /**
     * Returns `true` if the dynamic color is disabled.
     */
    val shouldDisableDynamicTheming: Boolean get() = true

    /**
     * Returns `true` if the Android theme should be used.
     */
    val shouldUseAndroidTheme: Boolean get() = false

    /**
     * Returns `true` if dark theme should be used.
     */
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
}
