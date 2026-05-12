package com.coco0o0o.artcart.locations.impl

import androidx.lifecycle.SavedStateHandle
import com.coco0o0o.artcart.core.domain.GetFavoriteableLocationsUseCase
import com.coco0o0o.artcart.core.domain.RefreshLocationsUseCase
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.model.data.FavoriteableArtLocation
import com.coco0o0o.artcart.core.testing.repository.TestLocationsRepository
import com.coco0o0o.artcart.core.testing.util.MainDispatcherRule
import com.coco0o0o.artcart.feature.locations.api.navigation.LocationsNavKey
import com.coco0o0o.artcart.feature.locations.impl.ArtNearbyUiState
import com.coco0o0o.artcart.feature.locations.impl.LocationsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 *
 * These tests use Robolectric because the subject under test (the ViewModel) uses
 * `SavedStateHandle.toRoute` which has a dependency on `android.os.Bundle`.
 *
 * TODO: Remove Robolectric if/when AndroidX Navigation API is updated to remove Android dependency.
 *  See https://issuetracker.google.com/340966212.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LocationsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val locationsRepository = TestLocationsRepository()
    private val getFavoriteableLocationsUseCase = GetFavoriteableLocationsUseCase(
        locationsRepository = locationsRepository,
    )
    private val refreshLocationsUseCase = RefreshLocationsUseCase(
        locationsRepository = locationsRepository,
    )
    private lateinit var viewModel: LocationsViewModel

    @Before
    fun setup() {
        viewModel = LocationsViewModel(
            savedStateHandle = SavedStateHandle(
                route = LocationsNavKey(initialTopicId = testInputTopics[0].artLocation.id),
            ),
            getFollowableArtLocationsUseCase = getFavoriteableLocationsUseCase,
            refreshLocationsUseCase = refreshLocationsUseCase,
            locationsRepository = locationsRepository,
            LocationsNavKey(initialTopicId = testInputTopics[0].artLocation.id),
        )
    }

    @Test
    fun uiState_whenInitialized_thenShowLoading() = runTest {
        assertEquals(ArtNearbyUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun uiState_whenLocationsAreLoading_thenShowLoading() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertEquals(ArtNearbyUiState.Loading, viewModel.uiState.value)
    }

    // TODO: add test post loading

    private val testLocations = listOf(
        FavoriteableArtLocation(
            ArtLocation(
                id = "0",
                name = "testname0",
                url = "testimageurl0",
            ),
            isFavorited = true,
        ),
        FavoriteableArtLocation(
            ArtLocation(
                id = "1",
                name = "testname1",
                url = "testimageurl1",
            ),
            isFavorited = false,
        ),
        FavoriteableArtLocation(
            ArtLocation(
                id = "2",
                name = "testname2",
                url = "testimageurl2",
            ),
            isFavorited = false,
        ),
    )
}
